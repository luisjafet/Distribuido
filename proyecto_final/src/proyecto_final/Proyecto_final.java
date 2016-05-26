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
        int n = 5;
        String ip = "228.5.6.7";
        int port = 6789;
       
        Process p1 = new Process(ip, port, 10);
        Process p2 = new Process(ip, port, 20);
        
        p1.startProcess();
        p2.startProcess();

    }

}
