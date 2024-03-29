package com.example.proyectodas2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Modificar extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    Button cameraBtn,galleryBtn,volver;
    String currentPhotoPath;
    StorageReference storageReference;

    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        selectedImage = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.camara);
        galleryBtn = findViewById(R.id.galeria);
        volver = findViewById (R.id.volver);
        storageReference = FirebaseStorage.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED) {
                //PEDIR EL PERMISO
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
                           }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        volver.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Menu_Principal.class);
                startActivity(intent);
            }
        });

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent ();
            } else {
                Toast.makeText (this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show ();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File (currentPhotoPath);
                selectedImage.setImageURI (Uri.fromFile (f));
                Log.d ("tag", "ABsolute Url of Image is " + Uri.fromFile (f));

                Intent mediaScanIntent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile (f);
                mediaScanIntent.setData (contentUri);
                this.sendBroadcast (mediaScanIntent);

                uploadImageToFirebase (f.getName (), contentUri);


            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData ();
                String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format (new Date ());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt (contentUri);
                Log.d ("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI (contentUri);

                uploadImageToFirebase (imageFileName, contentUri);


            }

        }


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("fcm", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        // Conseguir el token
                                        token = task.getResult();

                                        // Log and toast
                                        Log.d("fcm", "El token del dispositivo: " + token);
                                        // Despues de obtener el token se manda un POST a un PHP con el token para que el PHP se lo mande a Firebase y finalmente se mande una notificacion al dispositivo
                                        // para agradecer al usuario que haya fotografiado el logo de la universidad

                                        // Se realiza la identificacion
                                        Data data = new Data.Builder()
                                                .putString("token", token).build();
                                        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(Notificacion.class)
                                                .setInputData(data).build();
                                        WorkManager.getInstance(Modificar.this).enqueue(otwr);
                                        WorkManager.getInstance(Modificar.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Modificar.this, new Observer<WorkInfo>() {
                                            @Override
                                            public void onChanged(@Nullable WorkInfo workInfo) {
                                                if (workInfo != null && workInfo.getState().isFinished()) {
                                                    String resultado = workInfo.getOutputData().getString("result");
                                                    // Si el php devuelve que se ha identificado CORRECTAMENTE
                                                    Log.d("fcm", "Firebase.php devuelve "+resultado);

                                                }
                                            }
                                        });
                                    }
                                });
                    }
                });
                Toast.makeText(Modificar.this, "Firebase upload SUCCEED", Toast.LENGTH_SHORT).show();
            }
            // El onFailureListener se activa cuando falla
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Modificar.this, "Firebase upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

            // Create the File where the photo should go
        File photoFile = null;

        try {

            photoFile = createImageFile();
        } catch (IOException ex) {

        }
            // Continue only if the File was successfully created
        if (photoFile != null) {

            Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.proyectodas2.fileprovider",
                        photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }

    }
}