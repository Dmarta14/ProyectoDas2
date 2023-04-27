package com.example.proyectodas2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

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
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        TextView usua =findViewById (R.id.UsuarioIni);
        TextView contrasena1 = findViewById (R.id.PasswordIni);



        Button iniciar_sesion = findViewById(R.id.Acceder);
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String usuario = usua.getText().toString();
                String contra1 = contrasena1.getText().toString ();
                obtenerUsuario (usuario, contra1);
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


    public void obtenerUsuario(String usuario, String contra){
        Data param =new Data .Builder ()
                .putString ("param","Acceder")
                .putString ("usuario", usuario)
                .putString ("contrasena",contra).build ();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(MainActivity.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe (MainActivity.this, new Observer<WorkInfo> () {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                                Toast.makeText (getApplicationContext (),"ERROR",Toast.LENGTH_LONG).show ();
                            }else{
                                Data d = workInfo.getOutputData();
                                boolean b = d.getBoolean("result",false);
                                if(b){
                                    Toast.makeText(getApplicationContext(), "existe un usuario", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),Menu_Principal.class);
                                    intent.putExtra("usuario",usuario); //pasando el mail como parametro
                                    //abrir el activity del menu de opciones
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                                    builder.setTitle("Usuario o contraseña incorrectos");
                                    builder.setMessage("Introduce el usuario o contraseña correctamente o registrese en caso de no tener un usuario creado");
                                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                }
                        }
                    }
                }
        });
    }
}
