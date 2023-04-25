package com.example.proyectodas2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Modificar extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private ImageView imageView;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    String currentPhotoPath;
    StorageReference storageReference;
    private int requestCode;
    private int resultCode;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_modificar);

        if (ContextCompat.checkSelfPermission(Modificar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Modificar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Modificar.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        imageView= (ImageView) findViewById (R.id.imageView);
        storageReference = FirebaseStorage.getInstance ().getReference ();

//capturando los valores ingresados por el usuario en variables Java de tipo String
        EditText nombre =findViewById (R.id.Nombre);
        EditText apell =findViewById (R.id.Apellidos);
        EditText usu =findViewById (R.id.Usuario);
        EditText contraseña1 = findViewById (R.id.PasswordInicial);
        EditText contraseña2 = findViewById (R.id.PasswordConfirmada);
        EditText direccion = findViewById (R.id.Direccion);
        EditText telefono = findViewById (R.id.Telefono);
        EditText correo = findViewById (R.id.Email);
        EditText clubes = findViewById (R.id.Club);

        ImageButton anadirFoto= findViewById (R.id.imageButton);
        anadirFoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
            Toast.makeText (Modificar.this, "Imagen de la camara ha sido seleccionada", Toast.LENGTH_SHORT).show ();
            preguntarPermisosCamara();
            }
        });
        Button volver = findViewById(R.id.Cancelar);
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Menu_Principal.class);
                startActivity(intent);
            }
        });


        Button modificar = findViewById(R.id.modificar);
        modificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nombreU = nombre.getText().toString();
                String apellido = apell.getText().toString();
                String usuario = usu.getText().toString();
                String contra1 = contraseña1.getText().toString ();
                String contraConfirmada = contraseña2.getText().toString ();
                String dir = direccion.getText().toString ();
                String tele = telefono.getText().toString ();
                String email = correo.getText().toString ();
                String club = clubes.getText().toString ();
                //validaciones
                if (nombreU.isEmpty() || apellido.isEmpty() || tele.isEmpty() || email.isEmpty()  || contra1.isEmpty() || contraConfirmada.isEmpty() || usuario.isEmpty() || dir.isEmpty() || club.isEmpty() ){ //si algun campo esta vacio
                    AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                    builder.setTitle("Hay algún campo vacio");
                    builder.setMessage("Para continuar con el registro llene todos los campos solicitados");
                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(v.getContext (), Modificar.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    /*Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                            Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast*/
                } else {
                    if(contieneSoloLetras(nombreU)==false){
                        AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                        builder.setTitle("Nombre  incorrecto");
                        builder.setMessage("El nombre no puede contener numero");
                        builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(v.getContext (), Modificar.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else {
                        if(contieneSoloLetras(apellido)==false){
                            AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                            builder.setTitle("Apellidos incorrectos");
                            builder.setMessage("Los apellidos no pueden contener numeros");
                            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(v.getContext (), Modificar.class);
                                    startActivity(intent);
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            if (validartelefono (tele) == false) {

                                AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                builder.setTitle ("Número de telefono incorrecto");
                                builder.setMessage ("El numero de télefono debe tener 9 digitos");
                                builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent (v.getContext (), Modificar.class);
                                        startActivity (intent);
                                    }
                                });
                                AlertDialog alert = builder.create ();
                                alert.show ();
                            } else {
                                Pattern pattern = Patterns.EMAIL_ADDRESS;
                                if (pattern.matcher (email).matches () == false) { //no cumple el correo
                                    AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                    builder.setTitle ("Correo Electrónico incorrecto");
                                    builder.setMessage ("Ingrese un Email Valido");
                                    builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent (v.getContext (), Modificar.class);
                                            startActivity (intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create ();
                                    alert.show ();
                                } else {
                                    if (contra1.length () < 8) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                        builder.setTitle ("Contraseña incorrecta");
                                        builder.setMessage ("Ingrese una contraseña de 8 dígitos mínimo");
                                        builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent (v.getContext (), Modificar.class);
                                                startActivity (intent);
                                            }
                                        });
                                        AlertDialog alert = builder.create ();
                                        alert.show ();
                                    } else {
                                        if (validarpassword (contra1) == false) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                            builder.setTitle ("Contraseña incorrecta");
                                            builder.setMessage ("La contraseña debe tener números y letras");
                                            builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent (v.getContext (), Modificar.class);
                                                    startActivity (intent);
                                                }
                                            });
                                            AlertDialog alert = builder.create ();
                                            alert.show ();
                                        } else {
                                            if (contra1.equals (contraConfirmada)) {

                                                anadirUsuario (nombreU, apellido, usuario, contra1, dir, tele, email, club);
                                                AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                                builder.setTitle ("Registro Correcto");
                                                builder.setMessage ("El usuario se ha registrado correctamente");
                                                builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent (v.getContext (), MainActivity.class);
                                                        startActivity (intent);
                                                    }
                                                });
                                                AlertDialog alert = builder.create ();
                                                alert.show ();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder (Modificar.this);
                                                builder.setTitle ("Contraseñas Incorrectas");
                                                builder.setMessage ("Las contraseñas ingresadas no coinciden");
                                                builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent (v.getContext (), Modificar.class);
                                                        startActivity (intent);
                                                    }
                                                });
                                                AlertDialog alert = builder.create ();
                                                alert.show ();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void preguntarPermisosCamara() {
        if (ContextCompat.checkSelfPermission(Modificar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Modificar.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult (requestCode,permissions,grantResults);
        if (requestCode == CAMERA_PERM_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else {
                Toast.makeText(Modificar.this, "Camara", Toast.LENGTH_SHORT).show();
            }
        }
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File (currentPhotoPath);
                imageView.setImageURI (Uri.fromFile (f));
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
                imageView.setImageURI (contentUri);

                uploadImageToFirebase (imageFileName, contentUri);


            }

        }


    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
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
                    }
                });

                Toast.makeText(Modificar.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Modificar.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    


    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                return false;
            }
        }
        return true;
    }
    public boolean validartelefono(String telefono) {
        if (telefono.length() != 9){
            return false;
        } else{
            for (int x = 0; x < telefono.length(); x++) {
                char c = telefono.charAt(x);
                if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean validarpassword(String password) {
        boolean numeros = false;
        boolean letras = false;
        for (int x = 0; x < password.length(); x++) {
            char c = password.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                letras = true;
            }
            if ((c >= '0' && c <= '9') ) {
                numeros = true;
            }

        }
        if (numeros == true && letras ==true){
            return true;
        }
        return false;
    }
    public void anadirUsuario(String nombre, String apellido,String usuario, String contra,String dir, String tele,String email, String club){
        Data param =new Data .Builder ()
                .putString ("param","Registrar")
                .putString ("Nombre", nombre)
                .putString ("Apellido",apellido)
                .putString ("Usuario", usuario)
                .putString ("pass",contra)
                .putString ("Direccion", dir)
                .putString ("NTelefono",tele)
                .putString ("Email",email)
                .putString ("Club",club).build ();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(Modificar.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(Modificar.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe (Modificar.this, new Observer<WorkInfo> () {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                                Toast.makeText (getApplicationContext (),"Jaimitada",Toast.LENGTH_LONG).show ();
                            }
                        }
                    }
                });

    }

}