package com.superGrupo.Entidades;

public class Libro {
    String isbn;
    String nombre;
    int cantidadTotal;
    

    public Libro(long id, String isbn, String nombre, int cantidadTotal) {
        this.isbn = isbn;
        this.nombre = nombre;
        this.cantidadTotal = cantidadTotal;
    }

    
    public Libro() {
        
    }


    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCantidadTotal() {
        return cantidadTotal;
    }
    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    
    
}