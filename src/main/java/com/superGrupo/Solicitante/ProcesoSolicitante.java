package com.superGrupo.Solicitante;

import org.zeromq.ZMQ;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class ProcesoSolicitante {
    public static void main( String[] args ) throws Exception 
    {

        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:9999");
        LocalDateTime now1 = LocalDateTime.now();

        try{
            File myObj = new File("provider/ps.txt");
            Scanner myReader = new Scanner(myObj);

            requester.setReceiveTimeOut(400);

            boolean gestor1=true;
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                data = data + ","+ now1;
                byte[] reply;

                do{
                    try{
                        requester.send(data.getBytes(), 0);
                    }
                    catch(Exception e){
                        System.out.println("No hay gestor");
                    }
                
                    reply = requester.recv(0);
                    if(reply!=null){
                        String respuesta = new String(reply, StandardCharsets.UTF_8);
                        System.out.println("RESPUESTA: " + respuesta);
                    }
                    else {
                        gestor1=false;
                        System.out.println("Intenta con otro gestor");
                    }
                }while(reply==null);  
            }
            myReader.close();
        } catch (FileNotFoundException e){
            System.out.println("An error ocurred.");
            e.printStackTrace();
        }

        requester.close();
        context.term();
    }
}
