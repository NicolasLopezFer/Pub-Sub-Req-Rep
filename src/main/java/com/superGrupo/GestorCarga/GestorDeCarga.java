package com.superGrupo.GestorCarga;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.zeromq.ZMQ;

import org.zeromq.ZContext;
import org.zeromq.SocketType;

import com.superGrupo.Actores.ActorPrestamo;

public class GestorDeCarga {
    public static void main( String[] args ) throws Exception
    {
        //  Socker context
        ZMQ.Context context = ZMQ.context(1);
        ZContext context2 = new ZContext();

        //  Socker preparation
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        ZMQ.Socket publisher = context2.createSocket(SocketType.PUB);


        responder.bind("tcp://*:9999");
        publisher.bind("tcp://*:10001");
        
        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            String ps = new String(request, StandardCharsets.UTF_8);
            String[] partes = ps.split(",");
            
            
            String respuesta = "";

            if(partes[0].equals("D")){
                LocalDateTime now = LocalDateTime.now();
                //Envio de respuesta devolver a cliente
                respuesta = "La biblioteca esta recibiendo el libro";
                responder.send(respuesta.getBytes(), 0);
                
                //PUB SUB CON ACTOR DEVOLUCION
                publisher.sendMore("Devolucion");
                publisher.send(partes[1]+","+partes[2]+","+now.toString()); //Se necesita la sede?

                
            } else if(partes[0].equals("R")){
                
                //Envio de respuesta renovar a cliente
                DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();

                String fecha = dft.format(now.plusDays(7)).toString();
                respuesta = "La nueva fecha de retorno es:" + fecha;
                responder.send(respuesta.getBytes(), 0);

                String sede="Sede1";
                //PUB SUB CON ACTOR RENOVACION
                publisher.sendMore("Renovacion");
                publisher.send(partes[1]+","+partes[2]+","+fecha+","+sede);
                
                
            } else if(partes[0].equals("P")){
                DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                String fecha= dft.format(now.plusDays(7)).toString();
                String sede="Sede1";

                //Realizar prestamo y validacion
                ActorPrestamo aP = new ActorPrestamo();

                String respuestaAux = aP.realizarPrestamo(partes[1], partes[2],fecha,sede);

                //Envio de respuesta prestamo a cliente
                //respuesta = "Prestamo";
                responder.send(respuestaAux.getBytes(), 0);
                LocalDateTime now2 = LocalDateTime.now();

                System.out.println("Tiempo de proceso prestamo: " + Duration.between(LocalDateTime.parse(partes[3]), now2));
            }
            else{
                respuesta = "No se recibió el tipo de solicitud";
                responder.send(respuesta.getBytes(), 0);
            }
            //Thread.sleep(1000);

            // Send reply back to client-
        }
        responder.close();
        context2.close();
        context.term();


        
    }
}
