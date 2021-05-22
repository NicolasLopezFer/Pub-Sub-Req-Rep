package com.superGrupo.GestorCarga;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.zeromq.ZMQ;

import org.zeromq.ZContext;
import org.zeromq.SocketType;
import java.util.StringTokenizer;

public class GestorDeCarga {
    public static void main( String[] args ) throws Exception
    {
        // ZMQ.Context context = ZMQ.context(1);

        // //  Socket to talk to clients
        // ZMQ.Socket responder = context.socket(ZMQ.REP);
        // responder.bind("tcp://*:9999");
        
        // while (!Thread.currentThread().isInterrupted()) {
        //     // Wait for next request from the client
        //     byte[] request = responder.recv(0);
        //     String ps = new String(request, StandardCharsets.UTF_8);
        //     String[] partes = ps.split(",");
            
        //     //System.out.println("Solicitud: " + partes[0] + "  Usuario: " + partes[1] + "   Libro: " + partes[2]);
            
            
        //     String respuesta = "";

        //     if(partes[0].equals("D")){
        //         respuesta = "La biblioteca esta recibiendo el libro";
        //     } else if(partes[0].equals("R")){
        //         DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //         LocalDateTime now = LocalDateTime.now();
        //         respuesta = "La nueva fecha de retorno es:" + dft.format(now.plusDays(7));

        //     } else if(partes[0].equals("P")){
        //         respuesta = "Prestamo";
        //     }

        //     // Do some 'work'
        //     Thread.sleep(1000);

        //     // Send reply back to client-
        //     System.out.println("Se esta enviando: " + respuesta);
        //     responder.send(respuesta.getBytes(), 0);
        // }
        // responder.close();
        // context.term();


        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            System.out.println("Collecting updates from weather server");
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5556");

            //  Subscribe to zipcode, default is NYC, 10001
            String filter = (args.length > 0) ? args[0] : "10001 ";
            subscriber.subscribe(filter.getBytes(ZMQ.CHARSET));

            //  Process 100 updates
            int update_nbr;
            long total_temp = 0;
            for (update_nbr = 0; update_nbr < 100; update_nbr++) {
                //  Use trim to remove the tailing '0' character
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, " ");
                int zipcode = Integer.valueOf(sscanf.nextToken());
                int temperature = Integer.valueOf(sscanf.nextToken());
                int relhumidity = Integer.valueOf(sscanf.nextToken());

                total_temp += temperature;
            }

            System.out.println(
                String.format(
                    "Average temperature for zipcode '%s' was %d.",
                    filter,
                    (int)(total_temp / update_nbr)
                )
            );
        }
    }
}
