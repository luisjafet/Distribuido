/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import interfaces.IChatClient;
import interfaces.IChatServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        File file = new File(channel);
        System.out.println("Client " + name + " has logged in: " + channel);
    }

    public synchronized void logout(String name, String channel) throws RemoteException {
        System.out.println("Client " + name + " has logged out of: " + channel);
        Enumeration entChater = chatters.elements();
        while (entChater.hasMoreElements()) {
            ((IChatClient) entChater.nextElement()).receiveExit(name, channel);
        }
        chatters.remove(name);
    }

    public synchronized void send(Message message) throws RemoteException {
        Enumeration entChater = chatters.elements();
        String textRead;
        StringBuilder textSend = new StringBuilder();
        FileWriter writer;
        FileReader reader;
        try {
            while (entChater.hasMoreElements()) {
                if (message.text.equals("all")) {
                    reader = new FileReader(message.channel);
                    BufferedReader content = new BufferedReader(reader);
                    while ((textRead = content.readLine()) != null) {
                        textSend.append(textRead + "\n");
                    }
                    message.text = textSend.toString();
                } else {
                    writer = new FileWriter(message.channel, true);
                    if (message.content) {
                        if (message.text.toLowerCase().contains(message.channel)) {
                            writer.write("Message from " + message.name + " in " + message.channel + ": " + message.text + "\n");
                        }
                    } else {
                        writer.write("Message from " + message.name + " in " + message.channel + ": " + message.text + "\n");
                    }
                    writer.close();
                }
                ((IChatClient) entChater.nextElement()).receiveMessage(message);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Message from " + message.name + " in " + message.channel + ": " + message.text + "\n");
    }

    public static void main(String[] args) {
        String osName = System.getProperty("os.name").toLowerCase();
        String serverPolicyFile = "server.policy";
        if (osName.contains("win")) {
            serverPolicyFile = "file:server.policy";
        }

        System.setProperty("java.security.policy", serverPolicyFile);
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
