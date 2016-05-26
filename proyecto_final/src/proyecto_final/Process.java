/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.util.ArrayList;

/**
 *
 * @author luisjafet
 */
public class Process {

    int id;
    int n;
    int[] clock;
    ArrayList<Message> queue;

    int port;
    String ip;

    Receiver receiver;
    Sender sender;

    public Process(String ip, int port, int id, int n) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.clock = new int[n];
        this.queue = new ArrayList<Message>();
        this.receiver = new Receiver(this);
        this.sender = new Sender(this);
    }

    public void startProcess() {
        receiver.start();
        sender.start();
    }

    public void startSender() {
        sender.start();
    }

    public void startReceiver() {
        receiver.start();
    }

}
