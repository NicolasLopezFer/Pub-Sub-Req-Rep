package com.superGrupo.Entidades;

import java.time.LocalDate;

public class Prestamo {
    String operacion;
    String isbnLibro;
    String idUsuario;
    LocalDate fechaEntrega;
    String sede;


    public Prestamo(String operacion,long id, String isbnLibro, String idUsuario, LocalDate fechaPrestamo,LocalDate fechaEntrega,String sede) {
        this.operacion=operacion;
        this.isbnLibro = isbnLibro;
        this.idUsuario = idUsuario;
        this.fechaEntrega = fechaEntrega;
        this.sede = sede;
    }


    public Prestamo() {
    }


    public String getOperacion() {
        return operacion;
    }


    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }


    public String getIsbnLibro() {
        return isbnLibro;
    }


    public void setIsbnLibro(String isbnLibro) {
        this.isbnLibro = isbnLibro;
    }


    public String getIdUsuario() {
        return idUsuario;
    }


    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getSede() {
        return sede;
    }


    public void setSede(String sede) {
        this.sede = sede;
    }


    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }


    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    
    
    
}

