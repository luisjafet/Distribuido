/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_final;

import java.io.Serializable;

/**
 *
 * @author luisjafet
 */
public class Message implements Serializable{
    
    String content;
    int[] clock;
    
    public Message(String content, int[] clock){
        this.content = content;
        this.clock = clock;
    }

   
    public void setContent(String content) {
        this.content = content;
    }

   

    public void setClock(int[] clock) {
        this.clock = clock;
    }

    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contenido: " + content +  " ");
        sb.append("Reloj: " +  " ");
        
        for(int i=0; i<clock.length; i++){
            sb.append(clock[i] + ", ");
        }
        
        return sb.toString();
    }
    
    
    
   
}
