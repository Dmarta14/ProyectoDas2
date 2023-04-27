package com.example.proyectodas2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicioFirebase extends Worker {

    public ServicioFirebase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        // Recojo el token
        String token = getInputData().getString("token");

        String dir = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/dmarta002/WEB/Firebase.php";
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
            paramJson.put("token", token);


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
        return ListenableWorker.Result.failure();
    }
}