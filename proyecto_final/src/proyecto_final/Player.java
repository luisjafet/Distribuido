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
import java.util.logging.Level;
import java.net.SocketException;
import java.util.logging.Logger;

/**
 *
 * @author luis
 */
public class Player extends Thread {

    int id;
    int n;
    boolean sender;
    int[] deliv;
    int[][] sent;

    MulticastSocket s;
    InetAddress group;
    int port;

    public Player(int id, int n, int port, String ip, boolean sender) {
        this.id = id;
        this.n = n;
        this.sender = sender;
        this.deliv = new int[n];
        this.sent = new int[n][n];
        this.port = port;

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

    public void run() {
        if (sender) {
            send("I'm sender " + id);
        } else {
            System.out.println(receive());
        }
    }

    public void send(String message) {
        // Rule 1
        for (int i = 0; i < n; i++) {
            sent[id][i]++;
        }

        message += "CONTROL" + matrixToString(sent) + "CONTROL" + id;
        byte[] m = message.getBytes();
        DatagramPacket messageOut = new DatagramPacket(m, m.length, group, port);

        try {
            s.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receive() {
        String message = "";
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            // Reception of (M, ST_m) to the NC system
            s.receive(messageIn);
            String receive_message = new String(messageIn.getData());
//            System.out.println("________Message: " + receive_message + " from: " + messageIn.getAddress());
            String[] parts = receive_message.split("CONTROL");
//            System.out.println("___parts[0]: " + parts[0]);
//            System.out.println("___parts[1]: " + parts[1]);
//            System.out.println("___parts[2]: " + parts[2]);
            message = parts[0];
            int[][] st = stringToMatrix(parts[1], n);
            int from = Integer.parseInt(parts[2].trim());

            // Rule 2
            boolean waiting = true;
            int accomplished = 0;

            while (waiting) {
                
                //accomplished = 0;
                for (int i = 0; i < n; i++) {
                    if (deliv[i] >= st[i][id]) {
                        accomplished++;
                        System.out.println("player " + id + "is waiting");
//                        System.out.println("deliv[i] " + deliv[i]);
//                        System.out.println("st[i][id] " + st[i][id]);
                    }
                }
                
                if (accomplished == n) {
                    waiting = false;
                    System.out.println("player " + id + "is NOOOOOOT waiting");
                } else {
                    s.receive(messageIn);
                    receive_message = new String(messageIn.getData());
                    System.out.println("********Message: " + receive_message + " from: " + messageIn.getAddress());
                    parts = receive_message.split("CONTROL");
                    message += "," + parts[0];
                    st = stringToMatrix(parts[1], n);
                }
            }
            // Delivery of M to the C system
            System.out.println("C System " + id + " : message: " + receive_message + " from: " + messageIn.getAddress());
            deliv[from]++;
            sent[from][id]++;
            int max = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sent[i][j] = Math.max(sent[i][j], st[i][j]);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    public void logout() {
        if (s != null) {
            s.close();
        }
    }

    // Some helpers
    private String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] a : matrix) {
            for (int b : a) {
                sb.append(b + "");
            }
        }
//        System.out.println("matrix");
//        System.out.println(sb.toString());
        return sb.toString();
    }

    private int[][] stringToMatrix(String str, int n) {
        int[][] matrix = new int[n][n];
        int c = 0;
        String a = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a = str.charAt(c) + "";
                matrix[i][j] = Integer.parseInt(a.trim());
                c++;
            }
        }
        return matrix;
    }

}
