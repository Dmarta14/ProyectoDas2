package com.example.proyectodas2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "base_de_datos_proyecto.db"; //definiendo el nombre dela Bdd
    private static final int VERSION_BASE_DE_DATOS = 3; //definiendo la version de la BDD

    //Constructor
    public BaseDeDatos(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // definiendo estructura de la tabla Usuario
        String query = "CREATE TABLE Entrenadores(id INTEGER PRIMARY KEY AUTOINCREMENT,Nombre VARCHAR(50),Apellido VARCHAR(50) ,Usuario VARCHAR(30),Contraseña VARCHAR(30),Direccion VARCHAR(100), Nº_Telefono INT, E_mail VARCHAR(50), Club VARCHAR(50) )";
        db.execSQL (query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS Entrenadores";//elimincacion de la version anterior de la tabla usuarios se puede usar otro comando Dll como alter table
        db.execSQL(query);//Ejecucion del codigo para crear la tabla usuaios con su nueva estructura
        onCreate(db);
    }

    public Cursor obtenerUsuario(String usua, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor usuario = miBdd.rawQuery("select * from Entrenadores where " +
                "Usuario='" + usua + "' and Contraseña = '" + password + "';", null);

        //validar si existe o no la consulta
        if (usuario.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return usuario; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion de usuaio -> no existe o porque el email y password son incorrectos
            return null; //devuelvo null si no hay
        }
    }
    public void agregarUsuario(String nombre, String apellido, String usuario,String password,String dir,String telefono, String email,String club) {
        SQLiteDatabase miBdd = this.getWritableDatabase(); //llamando a la base de datos en el objeto mi Ddd
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into Entrenadores(Nombre,Apellido, Usuario, Contraseña, Direccion,Nº_Telefono,E_mail,Club) " +
                    "values('"+ nombre + "','" + apellido + "','" + usuario + "','" + password + "','" + dir +"','" + telefono + "','" + email + "','" + club + "')");    //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
        }
    }
}