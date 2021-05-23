package com.superGrupo.Actores;

import java.time.LocalDate;
import java.util.ArrayList;

import com.superGrupo.Entidades.Libro;
import com.superGrupo.Entidades.Prestamo;
import com.superGrupo.Entidades.Usuario;
import com.superGrupo.Services.DBService;

public class ActorPrestamo {
    
    public String realizarPrestamo(String idUsuario, String isbnLibro, String fecha, String sede){
        
        System.out.println("Entra a prestar");
        System.out.println(idUsuario);
        System.out.println(isbnLibro);
        
        ArrayList<Usuario> usuarios = DBService.leerUsuarios();
        System.out.println("Size Usuarios:"+usuarios.size());
        ArrayList<Libro> libros =DBService.leerLibros();
        System.out.println("Size Libros:"+libros.size());
        ArrayList<Prestamo> prestamos =DBService.leerPrestamos();
        System.out.println("Size Prestamos:"+prestamos.size());

        Usuario usuario=DBService.validarUsuario(idUsuario, usuarios);
        System.out.println("Usuario: "+usuario);
        if(usuario==null)
            return "El usuario no se encuentra registrado en la biblioteca";
        Libro libro = DBService.validarLibro(isbnLibro, libros);
        if(libro==null)
            return "El libro no se encuentra registrado en la biblioteca";
        
        Prestamo p=new Prestamo();
        p.setOperacion("P");
        p.setIsbnLibro(isbnLibro);
        p.setIdUsuario(idUsuario);
        p.setFechaEntrega(LocalDate.parse(fecha));
        p.setSede(sede);

        int cantidadPrestada = cantidadPrestadaLibro(isbnLibro,prestamos);
        int disponibles = libro.getCantidadTotal()-cantidadPrestada;
        System.out.println("Disp"+disponibles);
        if(disponibles<1)
            return "No existe disponibilidad del libro solicitado para prestamo.";
        prestamos.add(p);

        DBService.actualizarPrestamo(prestamos);
        return "Prestamo realizado correctamente.";
        
    }

    public static int cantidadPrestadaLibro(String isbnLibro,ArrayList<Prestamo> prestamos){
        int conteo=0;
        for(Prestamo prestamo:prestamos){
            if(prestamo.getIsbnLibro().equals(isbnLibro))
                conteo++;
        }
        return conteo;
    }
}
