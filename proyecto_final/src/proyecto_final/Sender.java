/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
public class Sender extends Thread {

    MulticastSocket s;
    InetAddress group;
    int port;
    int id;
    int[] clock;

    public Sender(Process p) {
        this.port = p.port;
        this.id = p.id;
        this.clock = p.clock;
        try {
            group = InetAddress.getByName(p.ip); // destination multicast group 
            s = new MulticastSocket(port);
            s.joinGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void send(Message message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            byte[] data = baos.toByteArray();
            DatagramPacket messageOut = new DatagramPacket(data, data.length, group, port);
            s.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {

        try {
            while (true) {
                String currentDate = "Hola soy: " + id;
                // Update clock
                clock[id]++;
                Message message = new Message(currentDate, clock);
                send(message);
                //System.out.println("Proceso: " + id + " envia: " + message);
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            s.close();
        }
    }
}
