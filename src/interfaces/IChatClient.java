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
public interface IChatClient extends java.rmi.Remote {

    void receiveEnter(String name, String channel) throws RemoteException;

    void receiveExit(String name, String channel) throws RemoteException;

    void receiveMessage(Message message) throws RemoteException;
}
