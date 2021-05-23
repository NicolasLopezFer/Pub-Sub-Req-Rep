package com.superGrupo.Actores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.superGrupo.Entidades.Libro;
import com.superGrupo.Entidades.Prestamo;
import com.superGrupo.Entidades.Usuario;
import com.superGrupo.Services.DBService;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class ActorRenovacion {
    public static void main(String[] args) throws Exception{
        ZContext context = new ZContext();

        Socket subscriber = context.createSocket(SocketType.SUB);
        subscriber.connect("tcp://localhost:10001");
        subscriber.subscribe("Renovacion".getBytes(ZMQ.CHARSET));

        while(!Thread.currentThread().isInterrupted()){
            String topic = subscriber.recvStr();
            String data = subscriber.recvStr();
            
            System.out.println(data);
            String[] partesAux = data.split(",");
            
            String usuario = partesAux[0];
            String libro = partesAux[1];
            String fecha = partesAux[2];
            String sede = partesAux[3];
            
            ArrayList<Usuario> usuarios = DBService.leerUsuarios();
            ArrayList<Libro> libros =DBService.leerLibros();
            ArrayList<Prestamo> prestamos =DBService.leerPrestamos();

            Usuario user=DBService.validarUsuario(usuario, usuarios);
            if(user==null){
                System.out.println("El usuario no se encuentra registrado en la biblioteca");
                return;
            }
            Libro book = DBService.validarLibro(libro, libros);
            if(book==null){
                System.out.println("El usuario no se encuentra registrado en la biblioteca");
                return;
            }

            System.out.println(usuario);
            System.out.println(libro);

            procesoRenovacion(usuario,libro,fecha,sede,prestamos);
            DBService.actualizarPrestamo(prestamos);
        }

        context.close();
    }

    public static void procesoRenovacion(String usuario,String libro,String fecha, String sede, ArrayList<Prestamo> prestamos){
        Prestamo p=DBService.validarPrestamo(libro, usuario,prestamos);
        
        if(p==null){
            System.out.println("Error: El prestamo que desea renovar no esta registrado en la biblioteca");
            return;
        }
        //Se debe validar que para la renovacion no se haya pasado de la fecha de entrega?
        p.setOperacion("P");
        p.setFechaEntrega(LocalDate.parse(fecha));
        p.setSede(sede);
        int index= prestamos.indexOf(p);
        prestamos.set(index,p);
        System.out.println("Renovación realizada correctamente.");
    }

    
}
