/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.util.ArrayList;

/**
 *
 * @author luis
 */
public class Player {
    
    String name;
    int[] deliv;
    int[][] sent;

    public Player(String name, int n) {
        this.name = name;
        this.deliv = new int[n];
        this.sent = new int[n][n];
    }
    
    

    
    
    
}
