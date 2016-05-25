/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.util.Arrays;

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
        Player sender1 = new Player(0, 5, port, ip, true);
        Player sender2 = new Player(1, 5, port, ip, true);
        Player sender3 = new Player(2, 5, port, ip, true);
        Player receiver1 = new Player(3, 5, port, ip, false);
        Player receiver2 = new Player(4, 5, port, ip, false);

        receiver1.start();
        receiver2.start();

        sender1.start();
        sender2.start();
       

    }

}
