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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastSenderPeer {

    public static void main(String args[]) {
        MulticastSocket s = null;

        try {
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            //s.setTimeToLive(10);
            System.out.println("Messages' TTL (Time-To-Live): " + s.getTimeToLive());

            while (true) {
                String currentDate = (new Date()).toString();
                byte[] m = currentDate.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
                s.send(messageOut);
                Thread.sleep(2000);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastSenderPeer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
}
