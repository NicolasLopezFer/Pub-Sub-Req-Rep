package com.superGrupo.Actores;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.superGrupo.Entidades.Libro;
import com.superGrupo.Entidades.Prestamo;
import com.superGrupo.Entidades.Usuario;
import com.superGrupo.Services.DBService;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class ActorDevolucion {
    public static void main(String[] args) throws Exception{
        ZContext context = new ZContext();

        Socket subscriber = context.createSocket(SocketType.SUB);
        subscriber.connect("tcp://localhost:10001");
        subscriber.subscribe("Devolucion".getBytes(ZMQ.CHARSET));

        while(!Thread.currentThread().isInterrupted()){
            String topic = subscriber.recvStr();
            String data = subscriber.recvStr();
            
            String[] partesAux = data.split(",");

            String usuario = partesAux[0];
            String libro = partesAux[1];
            LocalDateTime now = LocalDateTime.parse(partesAux[2]);
            //Se necesita sede?
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

            procesoDevolucion(usuario,libro,prestamos);
            DBService.actualizarPrestamo(prestamos);
            System.out.println("Tiempo de devolucion: " + Duration.between(now, LocalDateTime.now()));
        }

        context.close();
    }

    public static void procesoDevolucion(String usuario,String libro,ArrayList<Prestamo> prestamos){
        Prestamo prestamo=DBService.validarPrestamo(libro, usuario,prestamos);
        boolean deleted=true;
        try{
            deleted=prestamos.remove(prestamo);
        }catch(Exception e){
            System.out.println("Error: El prestamo al que desea hacer la devoluci??n no esta registrado en la biblioteca");
        }
        if (!deleted)
            System.out.println("Error: El prestamo al que desea hacer la devoluci??n no esta registrado en la biblioteca");
        else System.out.println("Devoluci??n realizada correctamente.");
    }
}
