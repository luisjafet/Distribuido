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
    int[] clock = {1,2,3};
    
    public Message(String content){
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" + "content=" + content + ", clock=" + clock + '}';
    }
    
    
    
   
}
