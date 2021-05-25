
package com.superGrupo.Services;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.superGrupo.Entidades.Libro;
import com.superGrupo.Entidades.Prestamo;
import com.superGrupo.Entidades.Usuario;

/**
 * Hello world!
 *
 */
public class DBService 
{
    static String dirUsuarios= "./database/USUARIOS.txt";
    static String dirLibros= "./database/LIBROS.txt";
    static String dirPrestamos= "./database/PRESTAMOS.txt";
    
    public static ArrayList<Usuario> leerUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        File archivo = new File(dirUsuarios);

        try {
            Scanner s = new Scanner(archivo);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] properties = line.split(",");
                Usuario user = new Usuario();
                user.setIdentificacion(properties[0]);
                user.setNombreCompleto(properties[1]);
                user.setEmail(properties[2]);
                user.setTelefono(properties[3]);

                usuarios.add(user);
            }
            s.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return usuarios;
    }

    public static ArrayList<Libro> leerLibros() {
        
        ArrayList<Libro> libros = new ArrayList<Libro>();

        File archivo = new File(dirLibros);

        try {
            Scanner s = new Scanner(archivo);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] properties = line.split(",");
                // System.out.println("line: "+line);
                //System.out.println("properties:"+properties);
                Libro libro = new Libro();
                libro.setIsbn(properties[0]);
                libro.setNombre(properties[1]);
                libro.setCantidadTotal(Integer.parseInt(properties[2]));

                libros.add(libro);
            }
            s.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return libros;
    }

    public static ArrayList<Prestamo> leerPrestamos() {
        ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();

        File archivo = new File(dirPrestamos);

        try {
            Scanner s = new Scanner(archivo);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] properties = line.split(",");
                Prestamo prestamo = new Prestamo();
                prestamo.setOperacion(properties[0]);
                prestamo.setIsbnLibro(properties[1]);
                prestamo.setIdUsuario(properties[2]);
                prestamo.setFechaEntrega(LocalDate.parse(properties[3]));
                prestamo.setSede(properties[4]);
                prestamos.add(prestamo);
            }
            s.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return prestamos;
    }

    public static Libro validarLibro(String isbn,ArrayList<Libro> libros){
        for(Libro libro:libros){
            if(libro.getIsbn().equals(isbn))
                return libro;
        }
        return null;
    }

    public static Usuario validarUsuario(String idUsuario,ArrayList<Usuario> usuarios){
        for(Usuario user:usuarios){
            if(user.getIdentificacion().equals(idUsuario))
                return user;
        }
        return null;
    }

    public static Prestamo validarPrestamo(String isbnLibro,String idUsuario,ArrayList<Prestamo> prestamos){
        for(Prestamo prestamo:prestamos){
            if(prestamo.getIdUsuario().equals(idUsuario) && prestamo.getIsbnLibro().equals(isbnLibro))
                return prestamo;
        }
        return null;
    }

    public static boolean actualizarPrestamo(ArrayList<Prestamo> prestamos){

        try {
            FileWriter fichero = new FileWriter(dirPrestamos);
            
			for (Prestamo p : prestamos) {
                String linea=p.getOperacion()+","+p.getIsbnLibro()+","+ p.getIdUsuario()+ ","+p.getFechaEntrega().toString()+","+p.getSede();
				fichero.write(linea + "\n");
			}
			fichero.close();
            return true;
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
        return false;
    }
}