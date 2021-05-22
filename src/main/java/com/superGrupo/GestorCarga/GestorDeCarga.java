package com.superGrupo.GestorCarga;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.zeromq.ZMQ;

public class GestorDeCarga {
    public static void main( String[] args ) throws Exception
    {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:9999");

        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            String ps = new String(request, StandardCharsets.UTF_8);
            String[] partes = ps.split(",");
            
            //System.out.println("Solicitud: " + partes[0] + "  Usuario: " + partes[1] + "   Libro: " + partes[2]);

            
            String respuesta = "";

            if(partes[0].equals("D")){
                respuesta = "La biblioteca esta recibiendo el libro";
            } else if(partes[0].equals("R")){
                DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                respuesta = "La nueva fecha de retorno es:" + dft.format(now.plusDays(7));

            } else if(partes[0].equals("P")){
                respuesta = "Prestamo";
            }

            // Do some 'work'
            Thread.sleep(1000);

            // Send reply back to client-
            System.out.println("Se esta enviando: " + respuesta);
            responder.send(respuesta.getBytes(), 0);
        }
        responder.close();
        context.term();
    }
}
