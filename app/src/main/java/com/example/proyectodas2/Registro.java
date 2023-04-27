package com.example.proyectodas2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.regex.Pattern;


public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_registro);



        BaseDeDatos baseDeDatos = new BaseDeDatos (getApplicationContext ());
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


        Button volver = findViewById(R.id.Cancelar);
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        Button registrar = findViewById(R.id.registrar);
        registrar.setOnClickListener(new View.OnClickListener() {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                    builder.setTitle("Hay algún campo vacio");
                    builder.setMessage("Para continuar con el registro llene todos los campos solicitados");
                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(v.getContext (), Registro.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    /*Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                            Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast*/
                } else {
                    if(contieneSoloLetras(nombreU)==false){
                        AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                        builder.setTitle("Nombre  incorrecto");
                        builder.setMessage("El nombre no puede contener numero");
                        builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(v.getContext (), Registro.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else {
                        if(contieneSoloLetras(apellido)==false){
                            AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                            builder.setTitle("Apellidos incorrectos");
                            builder.setMessage("Los apellidos no pueden contener numeros");
                            builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(v.getContext (), Registro.class);
                                    startActivity(intent);
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            if (validartelefono (tele) == false) {

                                AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                                builder.setTitle ("Número de telefono incorrecto");
                                builder.setMessage ("El numero de télefono debe tener 9 digitos");
                                builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent (v.getContext (), Registro.class);
                                        startActivity (intent);
                                    }
                                });
                                AlertDialog alert = builder.create ();
                                alert.show ();
                            } else {
                                Pattern pattern = Patterns.EMAIL_ADDRESS;
                                if (pattern.matcher (email).matches () == false) { //no cumple el correo
                                    AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                                    builder.setTitle ("Correo Electrónico incorrecto");
                                    builder.setMessage ("Ingrese un Email Valido");
                                    builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent (v.getContext (), Registro.class);
                                            startActivity (intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create ();
                                    alert.show ();
                                } else {
                                    if (contra1.length () < 8) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                                        builder.setTitle ("Contraseña incorrecta");
                                        builder.setMessage ("Ingrese una contraseña de 8 dígitos mínimo");
                                        builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent (v.getContext (), Registro.class);
                                                startActivity (intent);
                                            }
                                        });
                                        AlertDialog alert = builder.create ();
                                        alert.show ();
                                    } else {
                                        if (validarpassword (contra1) == false) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                                            builder.setTitle ("Contraseña incorrecta");
                                            builder.setMessage ("La contraseña debe tener números y letras");
                                            builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent (v.getContext (), Registro.class);
                                                    startActivity (intent);
                                                }
                                            });
                                            AlertDialog alert = builder.create ();
                                            alert.show ();
                                        } else {
                                            if (contra1.equals (contraConfirmada)) {

                                                anadirUsuario (nombreU, apellido, usuario, contra1, dir, tele, email, club);
                                                AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
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
                                                AlertDialog.Builder builder = new AlertDialog.Builder (Registro.this);
                                                builder.setTitle ("Contraseñas Incorrectas");
                                                builder.setMessage ("Las contraseñas ingresadas no coinciden");
                                                builder.setPositiveButton ("Volver", new DialogInterface.OnClickListener () {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent (v.getContext (), Registro.class);
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
                .putString ("nombre", nombre)
                .putString ("apellido",apellido)
                .putString ("usuario", usuario)
                .putString ("contrasena",contra)
                .putString ("direccion", dir)
                .putString ("ntelefono",tele)
                .putString ("email",email)
                .putString ("club",club).build ();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(Registro.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(Registro.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe (Registro.this, new Observer<WorkInfo> () {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                                Toast.makeText (getApplicationContext (),"ERROR",Toast.LENGTH_LONG).show ();
                            }
                        }
                    }
                });

    }
}