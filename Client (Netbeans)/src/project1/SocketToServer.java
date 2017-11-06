/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author saleh
 */
public class SocketToServer {
    
        public Socket socket;
        public String message;
        public Thread t;
        
        public Socket CreateSocket(String ipAdr,int port){
        Socket s=null;
        try{
        //s = new Socket(ipAdr,port);
        s= ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(ipAdr, port);
        if(!s.isConnected()) System.out.println("waiting for connexion");
        }
        catch (IOException ex) {
            Logger.getLogger(SocketToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s;
    }
    public void KillSocket(Socket s){
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void send(Socket s,String msg,JPanel panel, JFrame frame){ 
           t= new Thread(new ServerThread(s,msg,panel,frame));
           t.start();
        }
    public void killthread(Thread tt){
        //tt.stop();
    }
            
}
