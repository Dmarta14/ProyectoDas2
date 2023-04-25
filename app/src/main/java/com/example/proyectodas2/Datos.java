package com.example.proyectodas2;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;

public class Datos extends AppCompatActivity implements Serializable {
    public TextView titulo, materiales, explicacion, descripcion1;
    public Button boton, verVideo;

    public ImageView imageView;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 1;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_datos);
        ListaElementos elementos = (ListaElementos) getIntent ().getSerializableExtra ("Elementos");
        String descripcion = (String) getIntent ().getSerializableExtra ("descripcion");
        String materiales1 = (String) getIntent ().getSerializableExtra ("materiales");
        String explicacion1 = (String) getIntent ().getSerializableExtra ("explicacion");
        Integer imaId = (Integer) getIntent ().getSerializableExtra ("imagenes");

        titulo = findViewById (R.id.tituloDatos);
        descripcion1 = findViewById (R.id.tituloVideo1);
        materiales = findViewById (R.id.MaterialesExplicacion);
        explicacion = findViewById (R.id.ExplicacionDelEjercicio);
        imageView = findViewById (R.id.imView);
        titulo.setText (elementos.getName ());
        descripcion1.setText (descripcion);
        materiales.setText (materiales1);
        explicacion.setText (explicacion1);
        imageView.setImageResource (imaId);

        verVideo = findViewById (R.id.VerVideo);
        verVideo.setOnClickListener (new View.OnClickListener () {
                                         @Override
                                         public void onClick(View view) {

                                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ){
                                                mostrarNotificacion ();
                                             }
                                             else {
                                                 mostrarNuevaNotificación ();
                                             }

        }
        });
        boton= findViewById (R.id.Volver);
        boton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Ejercicios.class);
                intent.putExtra ("ListaElementos", elementos);

                startActivity(intent);
            }
        });



    }
    private void mostrarNotificacion(){
        CharSequence name ="Notificacion";
        NotificationChannel notificationChannel = new NotificationChannel (CHANNEL_ID,name,NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel (notificationChannel);
        mostrarNuevaNotificación();
    }

    private void mostrarNuevaNotificación(){
        String link = (String) getIntent ().getSerializableExtra ("link");
         Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse (link));
        pendingIntent = PendingIntent.getActivity (this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder (getApplicationContext (), CHANNEL_ID)
                .setSmallIcon (R.drawable.notificacion)
                .setContentTitle ("Ver video en youtube ")
                .setContentText ("Pulsa para ver el video en youtube")
                .setPriority (NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent (pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from (getApplicationContext ());
        if (ActivityCompat.checkSelfPermission (getApplicationContext (), POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions ( Datos.this,new  String[]{POST_NOTIFICATIONS},11);
           }
        notificationManager.notify (NOTIFICACION_ID, builder.build ());

    }

    private void setPendingIntent(Class<?> clsActivity){
        Intent intent =new Intent (this,clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create (this);
        stackBuilder.addParentStack (clsActivity);
        stackBuilder.addNextIntent (intent);
        pendingIntent = stackBuilder.getPendingIntent (1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}