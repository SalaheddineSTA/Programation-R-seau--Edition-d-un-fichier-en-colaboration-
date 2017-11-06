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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author saleh
 */
public class ServerThread implements Runnable{
    public SocketToServer s;
     public Socket socket;
     JSONParser parser;
     JSONObject obj;
     String response;
     JPanel panel;
     JFrame frame;

     
     static final  public  HashMap<String,String> information= new HashMap<String,String>();
     static final JSONObject projectJSON = new JSONObject() ;
     static final JSONObject projectExistedJSON = new JSONObject() ;
     static final JSONObject FilesJSON = new JSONObject() ;
     static final JSONObject ContentsJSON = new JSONObject() ;
     static final JSONObject createdJSON = new JSONObject() ;
     static final JSONObject ZipJSON = new JSONObject() ;
     static final JSONObject filecreatJSON = new JSONObject() ;
     static final JSONObject UsersJSON = new JSONObject() ;
     public HashMap<String,String> getInfor(){
         return information;
     }
     public JSONObject getusers(){
         return UsersJSON;
     }
     public JSONObject checkFileC(){
         return filecreatJSON;
     }
      public JSONObject getZIP(){
         return ZipJSON;
     }
     public JSONObject checkEdited(){
         return createdJSON;
     }
     public JSONObject getProj(){
         return projectJSON;
     }
      public JSONObject getListProj(){
         return projectExistedJSON;
     }
      public JSONObject getFiles(){
         return FilesJSON;
     }
      public JSONObject getFileContents(){
         return ContentsJSON;
     }
     
        public String message;
        public ServerThread(Socket s,String msg,JPanel panel,JFrame frame){
            this.socket=s;
            this.message=msg;
            this.panel=panel;
            this.frame=frame;
            }
     
        public ServerThread(){
            
            }
    @Override
    
    public void run() {
 
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            try{
             while(true){ 
                 
               BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
               PrintWriter pw = new PrintWriter(this.socket.getOutputStream(),true);              
               pw.println(this.message);
               String message_from_server = "";
               message_from_server = br.readLine();
               if(message_from_server!=null){
                 parser = new JSONParser();
                 try {
                     obj =(JSONObject) parser.parse(message_from_server);
                     System.out.println(obj.toJSONString());
                     response = obj.get("response").toString();
                     switch(response){
                         case "email exist": new Response().ErrorRegestration("Email already exist",this.panel);
                                           return;
                                           // break;
                         case "user exist": new Response().ErrorRegestration("User already exist",this.panel);
                                            return;
                         case "registred" : new Response().SuccessRegestration("successeful regestration",this.panel);
                                           return;
                         case "logined":    response = obj.get("username").toString();
                                            information.put("username", response);
                                            response = obj.get("email").toString();
                                            information.put("email", response);
                                            response = obj.get("id").toString();
                                            information.put("id", response);
                                            response = obj.get("session").toString();
                                            information.put("session", response);
                                            new main1().setVisible(true);
                                            frame.setVisible(false);
                                           return;
                         case "projects" :  obj =(JSONObject) parser.parse(obj.get("projects").toString());
                                            projectJSON.putAll(obj);
                                            return;
                         case "listprojects" : obj =(JSONObject) parser.parse(obj.get("projects").toString());
                                               
                                               projectExistedJSON.putAll(obj);
                                               return;
                         case "listfiles" :     obj =(JSONObject) parser.parse(obj.get("files").toString());
                                                   FilesJSON.clear();
                                                FilesJSON.putAll(obj);
                                                return;
                         case "filecontents" :  obj =(JSONObject) parser.parse(obj.get("contents").toString());
                                                ContentsJSON.putAll(obj);
                                                return;
                         case "created": createdJSON.putAll(obj);
                                         new Response().SuccessCreation("File Created",this.panel);
                                         return;
                         case "zip":    obj =(JSONObject) parser.parse(obj.get("projects").toString());
                                        ZipJSON.putAll(obj);
                                         return;
                         case "fileCreate" : obj =(JSONObject) parser.parse(obj.get("files").toString());
                                             filecreatJSON.putAll(obj);
                                             return;
                        case "edited" :    
                                             return;
                        case "added" :    
                                             return;
                        case "users" :  obj =(JSONObject) parser.parse(obj.get("users").toString());
                                        UsersJSON.putAll(obj);
                                            return;                      
                        case "user or password":
                                                return;
                        default: System.out.println("DEFAULLLLLLLLT ");
                                       return;
                     }
                     
                     
                 } catch (ParseException ex) {
                     Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                 }
               }
             }
             }
                catch(IOException e){
                    e.printStackTrace();
                }  
}
}
