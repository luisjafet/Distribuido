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
public class Proyecto_final {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ip = "228.5.6.7";
        int port = 6789;
        int n = 4;
        Process p1 = new Process(ip, port, 0, n);
        Process p2 = new Process(ip, port, 1, n);
        Process p3 = new Process(ip, port, 2, n);
        Process p4 = new Process(ip, port, 3, n);
        p1.startProcess();
        p2.startSender();
        p3.startSender();
        p4.startSender();

    }

}
