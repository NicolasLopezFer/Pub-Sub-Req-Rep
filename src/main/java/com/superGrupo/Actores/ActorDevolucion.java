package com.superGrupo.Actores;

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
            String data = subscriber.recvStr();
            System.out.println(data);
        }

        context.close();
    }
}
