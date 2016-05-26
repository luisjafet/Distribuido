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

    public Receiver(String ip, int port, int id) {
        this.port = port;
        this.id = id;
        try {
            group = InetAddress.getByName(ip); // destination multicast group 
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
                System.out.println("Message is: " + message);
            } else {
                System.out.println("The received object is not of type Message!");
            }

        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println("No object could be read from the received UDP datagram.");
        }

        return message.toString();

    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("proceso: " + id + " recibe: " + receive());
            }
        } finally {
            s.close();
        }
    }
}
