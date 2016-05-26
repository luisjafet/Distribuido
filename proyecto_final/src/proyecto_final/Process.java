/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

/**
 *
 * @author luisjafet
 */
public class Process {

    int id;
    int n;
    int[] deliv;
    int[][] sent;

    int port;
    String ip;

    Receiver receiver;
    Sender sender;

    public Process(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.receiver = new Receiver(ip, port, id);
        this.sender = new Sender(ip, port, id);
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
