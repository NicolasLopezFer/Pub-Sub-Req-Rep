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
        System.setProperty("java.rmi.server.hostname", "25.80.215.15");

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Context context2 = ZMQ.context(1);

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        ZMQ.Socket requester2 = context2.socket(ZMQ.REQ);

        requester.connect("tcp://25.16.253.86:10005");
        requester2.connect("tcp://25.12.85.91:10006");

        LocalDateTime now1 = LocalDateTime.now();
        boolean gestor1 = true;
        boolean gestor2 = true;
        
        try{
            File myObj = new File("provider/ps.txt");
            Scanner myReader = new Scanner(myObj);

            requester.setReceiveTimeOut(500);

            String sede="Sede1";
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                data = data + ","+ now1;
                byte[] reply;

                if(gestor1 == true && gestor2 == false){
                    gestor1 = false;
                    gestor2 = true;
                } else if(gestor1 == false && gestor2 == true){
                    gestor1 = true;
                    gestor2 = false;
                }

                

                do{
                    reply = null;
                    if(gestor1){
                        try{
                            requester.send(data.getBytes(), 0);
                        }
                        catch(Exception e){
                            System.out.println("Gestor sede 1 no activo");
                        }
                        reply = requester.recv(0);
                        if(reply!=null){
                            gestor2 = false;
                            gestor1 = true;
                            String respuesta = new String(reply, StandardCharsets.UTF_8);
                            System.out.println("RESPUESTA: " + respuesta);
                        }
                        else {
                            gestor2 = true;
                            gestor1 = false;
                            sede="Sede2";
                        }
                    }

                    if(gestor2){
                        try{
                            requester2.send(data.getBytes(), 0);
                        }
                        catch(Exception e){
                            System.out.println("Gestor sede 2 no activo");
                        }
                        reply = requester2.recv(0);
                        if(reply!=null){
                            gestor1 = false;
                            gestor2 = true;
                            String respuesta = new String(reply, StandardCharsets.UTF_8);
                            System.out.println("RESPUESTA: " + respuesta);
                        }
                        else {
                            gestor1 = true;
                            gestor2 = false;
                            sede="Sede2";
                        }
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
