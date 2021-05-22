package com.superGrupo.Solicitante;

import org.zeromq.ZMQ;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ProcesoSolicitante {
    public static void main( String[] args ) throws Exception 
    {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://25.16.253.86:9999");

        try{
            File myObj = new File("provider/ps.txt");
            Scanner myReader = new Scanner(myObj);

            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                requester.send(data.getBytes(), 0);

                byte[] reply = requester.recv(0);
                String respuesta = new String(reply, StandardCharsets.UTF_8);
                System.out.println("RESPUESTA: " + respuesta);
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
