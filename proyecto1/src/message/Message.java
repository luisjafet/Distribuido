/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.*;

/**
 *
 * @author luisjafet
 */
public class Message implements Serializable {

    public String name;
    public String text;
    public String channel;
    public boolean content;

    public Message(String name, String channel, String text, boolean content) {
        this.name = name;
        this.text = text;
        this.channel = channel;
        this.content = content;
    }
}
