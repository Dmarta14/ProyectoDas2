package com.example.proyectodas2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class Ejercicios extends AppCompatActivity implements Serializable {

    TextView titulo,tituloImagen1,tituloImagen2,tituloImagen3;
    ImageView foto1,foto2,foto3;

    Button boton1,boton2,boton3,volver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_ejercicios);

        ListaElementos elementos = (ListaElementos) getIntent ().getSerializableExtra ("ListaElementos");
        titulo = findViewById (R.id.titulo);
        titulo.setText (elementos.getName ());
        tituloImagen1= findViewById (R.id.tituloImagen1);
        tituloImagen2= findViewById (R.id.tituloImagen2);
        tituloImagen3= findViewById (R.id.tituloImagen3);
        tituloImagen1.setText (elementos.descripcion[0]);
        tituloImagen2.setText (elementos.descripcion[1]);
        tituloImagen3.setText (elementos.descripcion[2]);
        foto1 = findViewById (R.id.imagenView1);
        foto2 = findViewById (R.id.imagenView2);
        foto3 = findViewById (R.id.imagenView3);
        foto1.setImageResource (elementos.idFotos[0]);
        foto2.setImageResource (elementos.idFotos[1]);
        foto3.setImageResource (elementos.idFotos[2]);
        boton1=findViewById (R.id.Datos1);
        boton2=findViewById (R.id.Datos2);
        boton3=findViewById (R.id.Datos3);

        boton1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent (view.getContext (), Datos.class);
                intent.putExtra ("descripcion",elementos.descripcion[0]);
                intent.putExtra ("materiales",elementos.materiales[0]);
                intent.putExtra ("explicacion",elementos.explicacion[0]);
                intent.putExtra ("imagenes",elementos.idFotos[0]);
                intent.putExtra ("link",elementos.links[0]);
                intent.putExtra ("Elementos",elementos);

                startActivity (intent);

            }
        });

        boton2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent (view.getContext (), Datos.class);
                intent.putExtra ("descripcion",elementos.descripcion[1]);
                intent.putExtra ("materiales",elementos.materiales[1]);
                intent.putExtra ("explicacion",elementos.explicacion[1]);
                intent.putExtra ("imagenes",elementos.idFotos[1]);
                intent.putExtra ("link",elementos.links[1]);
                intent.putExtra ("Elementos",elementos);

                startActivity (intent);

            }
        });

        boton3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent (view.getContext (), Datos.class);
                intent.putExtra ("descripcion",elementos.descripcion[2]);
                intent.putExtra ("materiales",elementos.materiales[2]);
                intent.putExtra ("explicacion",elementos.explicacion[2]);
                intent.putExtra ("imagenes",elementos.idFotos[2]);
                intent.putExtra ("link",elementos.links[2]);
                intent.putExtra ("Elementos",elementos);

                startActivity (intent);

            }
        });
        volver= findViewById (R.id.Volver);
        volver.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Menu_Principal.class);
                intent.putExtra ("ListaElementos", elementos);

                startActivity(intent);
            }
        });
    }
}