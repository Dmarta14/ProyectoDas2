package com.example.proyectodas2;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class BD extends Worker {
    public BD(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super (context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String action = getInputData().getString("param");
        assert action != null;
        Log.d ("parametro",":" + action);
        switch (action){
            case "Registrar": {

                String dir = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/dmarta002/WEB/agregarUsuario.php";
                HttpURLConnection urlConnection = null;

                String nombre = getInputData ().getString ("nombre");
                String apellido = getInputData ().getString ("apellido");
                String usuario = getInputData ().getString ("usuario");
                String pass = getInputData ().getString ("contrasena");
                String direccion = getInputData ().getString ("direccion");
                String telefono = getInputData ().getString ("ntelefono");
                String email = getInputData ().getString ("email");
                String club = getInputData ().getString ("club");

                try {
                    URL dest = new URL (dir);
                    Log.d ("dest", "dest" + dest);
                    urlConnection = (HttpURLConnection) dest.openConnection ();
                    Log.d ("dest", "dest" + urlConnection);
                    urlConnection.setConnectTimeout (5000);
                    urlConnection.setReadTimeout (5000);
                    urlConnection.setRequestMethod ("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");

                    JSONObject paramJson = new JSONObject();
                    /*String parametros= "Nombre="+nombre+"&Apellido="+apellido+"&Usuario="+usuario+"&Contrasena="+pass+"&Direccion="+direccion+"&NTelefono="+telefono+"&Email="+email+"&Club="+club;
                    Log.d("conexion", "Parametros: " + parametros);*/
                    paramJson.put("nombre",nombre);
                    paramJson.put("apellido",apellido);
                    paramJson.put("usuario",usuario);
                    paramJson.put("contrasena",pass);
                    paramJson.put("direccion",direccion);
                    paramJson.put("ntelefono",telefono);
                    paramJson.put("email",email);
                    paramJson.put("club",club);

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    //out.print(parametros);
                    out.print(paramJson.toString());

                    out.close ();
                    Log.d("Prueba","Titooos"+paramJson);

                    int statusCode = urlConnection.getResponseCode ();
                    Log.d("conexion", "Codigo Respuesta " + statusCode);
                    Log.d("conexion", "Respuesta:" + urlConnection.getResponseMessage());

                    if (statusCode == 200) {
                        Log.d("conexion", "Entra aqui");
                        BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line;
                        StringBuilder result = new StringBuilder();
                        Log.d("respuesta","Titooos ha entrado2");
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line);
                            Log.d("respuesta","Titooos ha entrado3");
                        }
                        Log.d("respuesta","Titooos"+result);
                        inputStream.close();
                        Log.d("respuesta","Titooos ha entrado5");
                        boolean exito = result.toString().equals("true");

                        Data.Builder b = new Data.Builder();
                        return Result.success(b.putBoolean("result", exito).build());
                    }
                } catch (Exception e) {
                    Log.e ("EXCEPTION", "doWork: ", e);
                }
                break;
            }
            case "Acceder":{
                String dir = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/dmarta002/WEB/comprobarUsuario.php";
                HttpURLConnection urlConnection = null;

                String usuario = getInputData ().getString ("usuario");
                String pass = getInputData ().getString ("contrasena");


                try {
                    URL dest = new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("usuario", usuario);
                    paramJson.put("contrasena",pass);

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    Log.d("Prueba","Titooos"+paramJson);
                    Log.d("respuesta","Titooos"+statusCode);
                    if (statusCode == 200) {
                        BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line;
                        StringBuilder result = new StringBuilder();
                        Log.d("respuesta","Titooos ha entrado2");
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line);
                            Log.d("respuesta","Titooos ha entrado3");
                        }
                        Log.d("respuesta","Titooos: "+result);
                        inputStream.close();
                        Log.d("respuesta","Titooos ha entrado5");
                        boolean exito = result.toString().equals("true");

                        Data.Builder b = new Data.Builder();
                        return Result.success(b.putBoolean("result", exito).build());
                    }
                } catch (Exception e) {
                    Log.e ("EXCEPTION", "doWork: ", e);
                }
                break;
        }

    }
        return Result.failure();

}
}
