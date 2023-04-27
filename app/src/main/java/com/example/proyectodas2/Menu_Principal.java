package com.example.proyectodas2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Menu_Principal extends AppCompatActivity {

    List<ListaElementos> lista_elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        lista_elementos= new ArrayList<> ();
        lista_elementos.add (new ListaElementos ("Formaciones",R.drawable.conocimientos,new int[]{R.drawable.formacion_1_4_4_2,R.drawable.formacion_1_4_3_3,R.drawable.formacion_1_3_4_1_2},new String[]{"Sistema 1-4-4-2","Sistema 1-4-3-3","Sistema 1-3-4-1-2"},new String[]{"El sístema 1-4-4-2 es un sístema más de caracter defensivo que hace que tu equipo se sienta defensivamente más protegido que utilizando sistemas como el 1-4-3-3 o el 1-3-4-1-2","Con este sistema el entrenador busca que su equipo tenga la posesion de balón y busque realizar ataques más organizados","Es un sistema muy ofensivo con el que se busca estar la mayoría del partido en campo contrario"},new String[]{"Necesitarás defensas que entiendan muy bien el juego y capaces ordenar al equipo desde dentro.\n En cuanto a los delanteros, estos deben ser fuertes y ganadores de duelos aéreos además de tener un buen olfato de gol. \n La línea de centrocampistas tienen que estar dispuestos a realizar ayudas en todo el campo  ","Esta formación debes tener mediocentros con un buen trato de balón capaces de dar buenos balones a los extremos para buscar rápidamente un 1VS1","Para utilizar esta formación principalmente tus defensas deben ser rápidos para poder defender en campo contrario. \n Y los mediocentros deben ser muy móviles dentro del campo para intentar sorprender con su posición"},new String[]{"https://youtu.be/T0Qe8YGL4y0","https://youtu.be/wKzGH3CHzgM","https://youtu.be/rZqtqdcCUtc"}));
        lista_elementos.add (new ListaElementos ("Calentamiento",R.drawable.calentamiento,new int[]{R.drawable.rondo_clasico_calentamiento,R.drawable.rueda_pases,R.drawable.rueda_de_pases2},new String[]{"Rondo Clásico","Rueda de Pases 1","Rueda de pases 2"},new String[]{"4 chinos separados a 10-12m formando un cuadrado, balones y 10 jugadores","5 chinos para marcar las postas, balones y 11 jugadores","3 chinos para marcar las postas, balones y 7 jugadores"},new String[]{"Rondo clásico en el que situamos a 8 jugadores por fuera(2 en cada lateral) y 2 jugadores robando. \n Finalidad conseguir intercambiar el mayor número de pases sin que los jugadores que esten robando intercepten el balón, en caso que lo hagan el último en realizar el pase cambia su posición por el jugador que más tiempo lleve dentro robando. ","Consiste en realizar únicamente acciones de control y pase. Como marca la imagen","Consiste en realizar únicamente acciones de control y pase. Como marca la imagen"},new String[]{"https://youtu.be/KltLjXu-FwQ","https://youtu.be/fyCdeorQkFA","https://youtu.be/4-7YUc-sq7Y"}));
        lista_elementos.add (new ListaElementos ("Posesiones",R.drawable.posesiones,new int[]{R.drawable.posesion3zonas,R.drawable.posesionclasica,R.drawable.dosvsuno},new String[]{"Posesión en 3 Zonas","Posesión Clásica","Posesión 2vs1"},new String[]{"8 chinos formando un rectángulo divido en 3 zonas, balones y 15 jugadores dividos en 3 equipos de 5 jugadores","4 chinos formando un cuadrado de 40x40m, balones y 12 jugadores dividos en 2 equipos de 6 jugadores","4 chinos formando un cuadrado de 15x15m, balones y 9 jugadores dividos en 3 equipos de 3 jugadores"},new String[]{"Cada equipo se situa en una región del rectángulo, el equipo situado en el medio es el encargado de robar el balón. Los otros 2 equipos deben dar entre 5-8 pases para poder pasar el balón a la zona contrario donde estará esperando el otro equipo que no roba.\n En caso que se produzca un roba del equipo situado en la franja central o perdida del equipo poseedor de la posesión hacen cambio de roles el que estaba defendiendo pasar al campo donde estaba el equipo que ha perdido el balón, y este a defender ","Posesión cuya finalidad es que ambos equipos mantengan la posesión del balón el mayor tiempo posible a través de pases","Dos equipos jugan dentro del rectángulo/cuadrado establecido y siempre tienen la ayuda del 3er equipo situado en los lados opuestos del cuadrado y un jugador dan apoyo por dentro del mismo "},new String[]{"https://youtu.be/T0Qe8YGL4y0","https://youtu.be/AgnS3MO381Y","https://youtu.be/Kjs6aC89RxA"}));
        lista_elementos.add (new ListaElementos ("Finalizaciones",R.drawable.finalizaciones,new int[]{R.drawable.finalizacionesclasica,R.drawable.finalizacion_centro,R.drawable.finalizacion3},new String[]{"Finalización saliendo del palo","Finalización por centro","Finalición haciendo una jugada combinada"},new String[]{"2 chinos situados en la frontal del área Y balones","8 chinos 4 de ellos situados en las bandas y balones","Rueda de pases 2"},new String[]{"Un jugador sale del palo y corre hasta el chino situado enfrete suya fuera del area y recibe un pase de un compañero situado en palo opuesto y finaliza la acción","Los jugadores situados en el centro juegan un pase a los jugadores de la banda, que estos hacen una pared y acaban con un centro","Es una rueda de pases que acaba en tiro"},new String[]{"https://youtu.be/osRIPrg9UO4","https://youtu.be/6j2Sf77iz8U","https://youtu.be/0sC_DOwWaPY"}));
        lista_elementos.add (new ListaElementos ("Estrategia",R.drawable.estrategia,new int[]{R.drawable.estrategia1,R.drawable.estrategia2,R.drawable.estrategia3},new String[]{"Saque en corto","Volea Rechace","Puerta Atras"},new String[]{"11 jugadores y un balón","11 jugadores y un balón","11 jugadores y un balón"},new String[]{"Dos jugadores van al corner a sacar, el sacador saca en corto y doble, el receptor le devuelve le pase y el sacador la pone al segundo palo","El sacador saca un centro plano hacia el jugador del rechace y este acaba de volea","El sacador juega un pase raso al jugador situado con el portero, que este corre hacia el punto de penalty para recibir el centro"},new String[]{"https://youtu.be/8fae_z3-5o4","https://youtu.be/pDH7WZla8SI","https://youtu.be/QvhyTw6VsDI"}));




        ListAdapter listAdapter = new ListAdapter (lista_elementos, this, new ListAdapter.OnItemClickListener () {
            @Override
            public void onItemClick(ListaElementos lista) {
                moveToDescription(lista);
            }
        });
        RecyclerView recyclerView = findViewById (R.id.listRecycleView);
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        recyclerView.setAdapter (listAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.desplegable, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent=new Intent(getApplicationContext(),Modificar.class);
                startActivity(intent); //solicitamos que habra el menu
                finish(); //cerrando la activity
                return true;
            case R.id.item3:
                SharedPreferences sharedPref = this.getSharedPreferences("correo_electronico", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();
                Intent intentt = new Intent(this, MainActivity.class);
                startActivity(intentt);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void moveToDescription(ListaElementos lista){
        Intent intent = new Intent (this, Ejercicios.class);
        intent.putExtra ("ListaElementos", lista);
        startActivity (intent);
    }
}