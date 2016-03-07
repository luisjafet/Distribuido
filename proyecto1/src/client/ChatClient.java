/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import interfaces.IChatClient;
import interfaces.IChatServer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import message.Message;

/**
 *
 * @author luisjafet
 */
public class ChatClient extends UnicastRemoteObject implements IChatClient {

    public String name;
    public boolean content;
    public ArrayList<String> channels;
    IChatServer server;
    String serverURL;

    public ChatClient(String name, String channel, String url, boolean content) throws RemoteException {
        this.name = name;
        serverURL = url;
        this.content = content;
        channels = new ArrayList<String>();
        login(channel);
    }

    private void login(String channel) {
        try {
            if (!channels.contains(channel)) {
                server = (IChatServer) Naming.lookup("rmi://" + serverURL + "/ChatServer");
                server.login(name, channel, this); // callback object
                channels.add(channel);
            } else {
                System.out.println("\n-----Your are subscribed to " + channel + "-----\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(String channel) {
        try {
            if (channels.contains(channel)) {
                server.logout(name, channel);
                channels.remove(channel);
            } else {
                System.out.println("\n-----Your aren't subscribed to  " + channel + "-----\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(String text, String channel, boolean content) {
        try {
            if (channels.contains(channel)) {
                server.send(new Message(name, channel, text, content));
            } else {
                System.out.println(name + " you are not subscribed to " + channel + "\n");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void receiveEnter(String name, String channel) {
        if (channels.contains(channel)) {
            System.out.println("\n-----Log in " + name + "-----\n");
        }
    }

    public void receiveExit(String name, String channel) {
        if (channels.contains(channel)) {
            System.out.println("\n-----Log out " + name + "-----\n");

            if (name.equals(this.name)) {
                System.exit(0);
            } else {
                //System.out.println(this.name + " -- type a message: ");
            }// si???
        }
    }

    public void receiveMessage(Message message) {
        if (content) {
            for (int i = 0; i < channels.size(); i++) {
                if (message.text.toLowerCase().contains(channels.get(i))) {
                    System.out.println("Message from " + message.name + " in " + message.channel + ": " + message.text + "\n");
                    break;
                }
            }
        } else if (channels.contains(message.channel)) {
            System.out.println("Message from " + message.name + " in " + message.channel + ": " + message.text + "\n");
        }
    }

    public static String askForString(String letrero) {
        StringBuffer strDato = new StringBuffer();
        String strCad1ena = "";
        try {
            System.out.print(letrero);
            BufferedInputStream bin = new BufferedInputStream(System.in);
            byte bArray[] = new byte[256];
            int numCaracteres = bin.read(bArray);
            while (numCaracteres == 1 && bArray[0] < 32) {
                numCaracteres = bin.read(bArray);
            }
            for (int i = 0; bArray[i] != 13 && bArray[i] != 10 && bArray[i] != 0; i++) {
                strDato.append((char) bArray[i]);
            }
            strCad1ena = new String(strDato);
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
        return strCad1ena;
    }

    public static void main(String[] args) {
        String strCad1, strCad2, clientName, channel, url;
        boolean content = false;
        try {
            clientName = askForString("Enter the client's name: ");
            channel = askForString("Enter the channel/topic/content's name: ");
            url = askForString("Enter the url: ");
            if (askForString("Enter 'Y' ot 'y' for a content-based client, otherwise it would be channel/topic: ").toLowerCase().equals("y")) {
                content = true;
                System.out.println("Set for content");
            }else{
                System.out.println("Set for channel/topic");
            }
            ChatClient client = new ChatClient(clientName, channel, url, content);
            System.out.println("This are the valid command:");
            System.out.println("subscribe   --subscribe to a channel/topic/content");
            System.out.println("unsubscribe --unsubscribe to a channel/topic/content");
            System.out.println("all         --get all message of a channel/topic/content");
            System.out.println("quit        --exit the program");
            strCad1 = askForString(clientName + " -- type a message/command: ");
            boolean next = true;
            while (!strCad1.equals("quit")) {
                strCad2 = askForString(clientName + " -- type a channel/topic/content: ");
                switch (strCad1) {
                    case "subscribe":
                        client.login(strCad2);
                        next = false;
                        break;
                    case "unsubscribe":
                        client.logout(strCad2);
                        next = false;
                        break;
                    case "all":
                        client.send(strCad1, strCad2, content);
                        next = false;
                        break;
                    default:
                        break;
                }
                if (next) {
                    client.send(strCad1, strCad2, content);
                    next = true;
                }
                next = true;
                strCad1 = askForString(clientName + " -- type a message/command: ");
            }
            System.out.println("Local console " + client.name + ", going down");
            for (int i = 0; i < client.channels.size(); i++) {
                client.logout(client.channels.get(i));
            }
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
