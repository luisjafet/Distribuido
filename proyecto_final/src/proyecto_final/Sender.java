/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisjafet
 */
public class Sender extends Thread {

    DatagramSocket s;
    InetAddress group;
    int port;
    int id;

    public Sender(String ip, int port, int id) {
        this.port = port;
        this.id = id;
        try {
            group = InetAddress.getByName(ip); // destination multicast group 
            s = new DatagramSocket(port);

            //s.joinGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String message) {
        byte[] m = message.getBytes();
        DatagramPacket messageOut = new DatagramPacket(m, m.length, group, port);

        try {
            s.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        String currentDate = (new Date()).toString();
        try {
            while (true) {
                send(currentDate);
                System.out.println("_________________________");
                System.out.println("proceso: " + id + " envia: " + currentDate);
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            s.close();
        }
    }
}
