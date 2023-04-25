package com.example.proyectodas2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        BaseDeDatos baseDeDatos = new BaseDeDatos (getApplicationContext ());
        SQLiteDatabase db = baseDeDatos.getReadableDatabase ();

        TextView usua =findViewById (R.id.UsuarioIni);
        TextView contraseña1 = findViewById (R.id.PasswordIni);



        Button iniciar_sesion = findViewById(R.id.Acceder);
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String usuario = usua.getText().toString();
                String contraseña = contraseña1.getText ().toString ();
                Cursor cursor= baseDeDatos.obtenerUsuario(usuario,contraseña);
                if(cursor != null) {
                    Intent intent = new Intent(v.getContext(),Menu_Principal.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                    builder.setTitle("Usuario o contraseña incorrectos");
                    builder.setMessage("Introduce el usuario o contraseña correctamente o registrese en caso de no tener un usuario creado");
                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(v.getContext (), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        Button registrar = findViewById(R.id.Regristrarse);
        registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Registro.class);
                startActivity(intent);
            }
        });
    }
    public void cambiarIdioma(View view) {//Metodo para cambiar idioma
        boolean isChecked = ((Switch) view).isChecked();//si esta seleccionado estaremos en castellano, sino en inglés
        if (isChecked) {
            setLocale("es");
        } else {
            setLocale("en");
        }

    }
    private void setLocale(String languageCode) {//metodo para cambiar la configuración del idioma en funcion de los ficheros srings.xml
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

}