/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.io.IOException;
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
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receive() {
        String message = "";
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            s.receive(messageIn);
            message = new String(messageIn.getData());
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
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
