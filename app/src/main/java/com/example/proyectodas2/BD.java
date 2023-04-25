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

import kotlinx.coroutines.scheduling.CoroutineScheduler;

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

                String nombre = getInputData ().getString ("Nombre");
                String apellido = getInputData ().getString ("Apellido");
                String usuario = getInputData ().getString ("Usuario");
                String pass = getInputData ().getString ("pass");
                String direccion = getInputData ().getString ("Direccion");
                String telefono = getInputData ().getString ("NTelefono");
                String email = getInputData ().getString ("Email");
                String club = getInputData ().getString ("Club");

                try {
                    URL dest = new URL (dir);
                    Log.d ("dest", "dest" + dest);
                    urlConnection = (HttpURLConnection) dest.openConnection ();
                    Log.d ("dest", "dest" + urlConnection);
                    urlConnection.setConnectTimeout (5000);
                    urlConnection.setReadTimeout (5000);
                    String parametros= "Nombre="+nombre+"&Apellido="+apellido+"&Usuario="+usuario+"&pass="+pass+"&Direccion="+direccion+"&NTelefono="+telefono+"&Email="+email+"&Club="+club;
                    Log.d("conexion", "Parametros: " + parametros);
                    urlConnection.setRequestMethod ("POST");
                    urlConnection.setDoOutput (true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametros);
                    out.close ();

                    int statusCode = urlConnection.getResponseCode ();
                    Log.d("conexion", "Codigo Respuesta " + statusCode);
                    Log.d("conexion", "Respuesta:" + urlConnection.getResponseMessage());

                    if (statusCode == 200) {
                        Log.d("conexion", "JAIMITADA?");
                        BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line, result = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }
                        inputStream.close();


                        Data data = new Data.Builder().putString("result", result).build();
                        return Result.success (data);
                    }
                } catch (Exception e) {
                    Log.e ("EXCEPTION", "doWork: ", e);
                }
                break;
            }
            case "Acceder":{
                String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/dmarta002/WEB/comprobarUsuario.php";
                HttpURLConnection urlConnection = null;

                String usuario = getInputData ().getString ("Usuario");
                String contraseña = getInputData ().getString ("Contraseña");

                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("usuario", usuario)
                            .appendQueryParameter("contraseña", contraseña);
                    String params = builder.build().getEncodedQuery();

                    direccion += "?" + params;
                    URL dest = new URL (direccion);
                    urlConnection = (HttpURLConnection) dest.openConnection ();
                    urlConnection.setConnectTimeout (5000);
                    urlConnection.setReadTimeout (5000);
                    urlConnection.setRequestMethod ("GET");

                    int statusCode = urlConnection.getResponseCode ();
                    if (statusCode == 200) {
                        BufferedInputStream inputStream =
                                new BufferedInputStream (urlConnection.getInputStream ());
                        BufferedReader bufferedReader =
                                new BufferedReader (new InputStreamReader (inputStream,
                                        "UTF-8"));
                        String line;
                        StringBuilder result = new StringBuilder ();
                        while ((line = bufferedReader.readLine ()) != null) {
                            result.append (line);
                        }
                        inputStream.close ();

                        JSONParser parser = new JSONParser ();
                        JSONObject json = (JSONObject) parser.parse (result.toString ());
                        Log.i ("JSON", "doWork: " + json);

                        JSONArray array = (JSONArray) json.get ("result");
                        int len = 0;
                        if (array.length () > 0) {
                            len = array.length ();
                            Log.i ("JSON", "doWork: " + len);
                            Data.Builder b = new Data.Builder ();
                            return Result.success (b.putInt ("len", len).build ());
                        }
                    }
                }catch(Exception e){
                        Log.e ("EXCEPTION", "doWork: ", e);
                    }
                }
            break;
        }
        return Result.failure();
    }

    public static Data createParam(String[] keys, Object[] params) {
        Data.Builder b = new Data.Builder();
        for (int i = 0; i < keys.length; i++) {
            if (params[i] instanceof Integer) {
                b.putInt(keys[i], (Integer) params[i]);
            } else if (params[i] instanceof String) {
                b.putString(keys[i], (String) params[i]);
            }
        }
        return b.build();
    }
}
