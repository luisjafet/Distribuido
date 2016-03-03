/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.RemoteException;
import message.Message;

/**
 *
 * @author luisjafet
 */
public interface IChatServer extends java.rmi.Remote {

    void login(String name, String channel, IChatClient newClient) throws RemoteException;

    void logout(String name, String channel) throws RemoteException;

    void send(Message message) throws RemoteException;
}
