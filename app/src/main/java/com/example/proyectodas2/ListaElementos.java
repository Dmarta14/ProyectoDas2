package com.example.proyectodas2;

import java.io.Serializable;


public class ListaElementos implements Serializable {
    //public String color;
    public String name;
    public int foId;

    public int[] idFotos;

    public String[] descripcion,materiales,explicacion,links;


    public ListaElementos(String name, int fotoId, int[] fotos, String[] descrip, String[] mats, String[] expli, String[] link) {
        this.name = name;
        this.foId = fotoId;
        idFotos=fotos;
        descripcion = descrip;
        materiales = mats;
        explicacion = expli;
        links=link;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getMateriales(int pos){
        return materiales[0];
    }
    public String getDescripcion(int pos){
        return materiales[0];
    }
    public String getExplicacion(int pos){
        return materiales[0];
    }

}
