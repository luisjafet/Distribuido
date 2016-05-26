/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisjafet
 */
public class Receiver extends Thread {

    MulticastSocket s;
    InetAddress group;
    int port;
    int id;
    int[] clock;
    ArrayList<Message> queue;

    public Receiver(Process p) {
        this.port = p.port;
        this.id = p.id;
        this.clock = p.clock;
        this.queue = p.queue;
        try {
            group = InetAddress.getByName(p.ip); // destination multicast group 
            s = new MulticastSocket(port);
            s.joinGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receive() {
        Message message = null;
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            s.receive(messageIn);

            //Deserialze object
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object readObject = ois.readObject();
            if (readObject instanceof Message) {
                message = (Message) readObject;
            } else {
                System.out.println("The received object is not of type Message!");
            }

        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println("No object could be read from the received UDP datagram.");
        }
        queue.add(message);
        if (!queue.isEmpty()) {
            Message m = getMinMessage();
            dispatch(m);
        }

        return message.toString();

    }

    public void dispatch(Message m) {
        int count = 0;

        for (int i = 0; i < clock.length; i++) {
            if (clock[i] + 1 == m.clock[i]) {
                count++;
            }
        }
        //System.out.println("count: " + count);
        if (count == 1 || count == 0) {
            updateClock(m);
            queue.remove(m);
            System.out.println("------Proceso " + id + " Mensaje recibido: " + m.toString());
        }

    }

    public Message getMinMessage() {
        int min = 0;
        int count = 0;
        Message aux = new Message(queue.get(0).content, queue.get(0).clock);
        for (int i = 1; i < queue.size(); i++) {
            for (int j = 0; j < clock.length; j++) {
                if (queue.get(i).clock[j] <= aux.clock[j]) {
                    count++;
                }
                if (count == clock.length) {
                    aux.setContent(queue.get(i).content);
                    aux.setClock(queue.get(i).clock);
                    min = i;
                }
            }
        }
        return queue.get(min);

    }

    public void updateClock(Message m) {
        for (int i = 0; i < clock.length; i++) {
            clock[i] = Math.max(clock[i], m.clock[i]);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                //System.out.println("proceso: " + id + " recibe: " + receive());
                receive();
            }
        } finally {
            s.close();
        }
    }

}
