/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import interfaces.IChatClient;
import interfaces.IChatServer;
import java.io.File;
import java.io.FileWriter;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.*;
import message.Message;

/**
 *
 * @author luisjafet
 */
public class ChatServer extends UnicastRemoteObject implements IChatServer {

    Hashtable<String, IChatClient> chatters = new Hashtable<String, IChatClient>();

    public ChatServer() throws RemoteException {
    }

    public synchronized void login(String name, String channel, IChatClient nc) throws RemoteException {
        chatters.put(name, nc);
        Enumeration entChater = chatters.elements();
        while (entChater.hasMoreElements()) {
            ((IChatClient) entChater.nextElement()).receiveEnter(name, channel);
        }
        //File file = new File(channel);
        System.out.println("Client " + name + " has logged in channel: " + channel);
    }

    public synchronized void logout(String name, String channel) throws RemoteException {
        System.out.println("Client " + name + " has logged out of channel: " + channel);
        Enumeration entChater = chatters.elements();
        while (entChater.hasMoreElements()) {
            ((IChatClient) entChater.nextElement()).receiveExit(name, channel);
        }
        chatters.remove(name);
    }

    public synchronized void send(Message message) throws RemoteException {
        Enumeration entChater = chatters.elements();
//        FileWriter writer;
        while (entChater.hasMoreElements()) {

//            writer = new FileWriter(message.canal + ".txt", true);
//            writer. write(message.text + "-");
//            writer.close();

            ((IChatClient) entChater.nextElement()).receiveMessage(message);
        }
        System.out.println("Message from: " + message.name + " in channel: " + message.channel + "\n" + message.text);
    }

    public static void main(String[] args) {
        System.setProperty("java.security.policy", "/home/luisjafet/itam/distribuido/proyecto1/RMIProject/src/server/server.policy");
        String serverURL = new String("///ChatServer");
        try {
            LocateRegistry.createRegistry(1099);
            ChatServer server = new ChatServer();
            Naming.rebind(serverURL, server);
            System.out.println("Chat server ready");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
