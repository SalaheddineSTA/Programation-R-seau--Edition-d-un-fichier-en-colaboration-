import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.net.ssl.SSLServerSocketFactory;
import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class Server {
	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore" , "encrypted.store");
		System.setProperty("javax.net.ssl.keyStorePassword" , "sta+6us+praga");	
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		Thread t = new Thread(new Listen());
		t.start();
	}
}

class Listen implements Runnable {
	ServerSocket socketserver;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Mysql db = new Mysql("localhost","root","mivhub" , "root");	
	Map<String, String>  outSql = new HashMap<String,String>();
	Connection connect = new Connection(db);
	
	public void run() {
		db.Connect();
		try{
			socketserver = ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(1994 , 0, null);
			System.out.println("Server listen in Port: 1994");
			while(true){
				socket = socketserver.accept();
				System.out.println("Noobs connected");
				in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream() , true);
				String message_distant = "";
				try{
					message_distant = in.readLine();
				}catch(Exception e){
					//e.printStackTrace();
				}
			    if( message_distant != null && message_distant != ""){
			      System.out.println(message_distant);
			      JSONParser parser = new JSONParser();
			      JSONObject jsons = new JSONObject();
			      
			      try{ 
			    	  JSONObject json = (JSONObject) parser.parse(message_distant);
			    	  String action = json.get("Action").toString();
			    	  String rep;
			    	  StringWriter jsonOut = new StringWriter();
			    	  StringWriter jsonOuts = new StringWriter();
			    	  switch (action){
			    	  	case "Signup":
			    	  		rep = connect.register(json.get("username").toString(), 
			    	  		json.get("password").toString(), json.get("email").toString());
			    	  		json.clear();
			    	  		json.put("response", rep);
			    	  		json.writeJSONString(jsonOut);
			    	  		
			    	  		out.println(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  	break;
			    	  	case "SignIn":
			    	  		rep = connect.login(json.get("username").toString(), 
			    	  				json.get("password").toString());
			    	  		if( rep == "logined" ){
			    	  			connect.getUserInfo(json.get("username").toString(), json.get("password").toString() , outSql);
			    	  		}
			    	 
			    	  		json.clear();
			    	  		json.put("response", rep);
			    	  		if( rep == "logined" ){
			    	  			json.put("id", outSql.get("id"));
			    	  			json.put("username", outSql.get("username"));
			    	  			json.put("session", outSql.get("session"));
			    	  			json.put("email",outSql.get("email"));
			    	  		}
			    	  		
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "CreationProject":
			    	  		
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			
			    	  			jsons = connect.getType();
								jsons.writeJSONString(jsonOuts);
								json.clear();
								json.put("response" , "projects");
								json.put("projects", jsonOuts.toString());
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "DownloadZIP":
			    	  		
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			
			    	  			String repos = connect.getZipProject(json.get("id").toString() , json.get("idP").toString());
								json.put("response" , "zip");
								json.put("projects", repos);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "GetProject":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.getProject(json.get("id").toString());
								json.clear();
								json.put("response" , "listprojects");
								json.put("projects", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "GetFiles":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.getFiles(json.get("id").toString(),json.get("idP").toString());
								json.clear();
								json.put("response" , "listfiles");
								json.put("files", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "GetUser":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.getUsers();
								json.clear();
								json.put("response" , "listusers");
								json.put("files", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "AddUser":
			    	  		if(connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed = connect.addUserToProject(json.get("id").toString(), json.get("user").toString() , json.get("idP").toString());
			    	  			json.clear();
			    	  			json.put("response", "added");
			    	  			json.put("info", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    		case "SetContents":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.SetContents(json.get("id").toString(),json.get("idP").toString() , json.get("filename").toString() , json.get("ctx").toString());
								json.clear();
								json.put("response" , "edited");
								//json.put("files", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    		case "GetUsers":
			    			if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.getUsers();
								json.clear();
								json.put("response" , "users");
								json.put("users", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    		break;
			    	  	case "GetContents":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			outed = connect.getFileConents(json.get("id").toString(),json.get("idP").toString() , json.get("fileName").toString());
								json.clear();
								json.put("response" , "filecontents");
								json.put("contents", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "CreateFile":
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){
			    	  			String outed;
			    	  			connect.CreatFile(json.get("id").toString(),json.get("idP").toString() , json.get("fileName").toString());
								outed = connect.getFiles(json.get("id").toString(),json.get("idP").toString());
			    	  			json.clear();
								json.put("response" , "fileCreate");
								json.put("files", outed);
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  	break;
			    	  	case "CreationProjectData" : 
			    	  		if( connect.isConnected(json.get("id").toString(), json.get("session").toString())){	
			    	  			
			    	  			rep = connect.addProjects(json);
			    	  			json.clear();
								json.put("response" ,rep );
			    	  		}else{
			    	  			json.clear();
			    	  			json.put("response", "notconnected");
			    	  		}
			    	  		json.writeJSONString(jsonOut);
			    	  		System.out.println(jsonOut);
			    	  		out.println(jsonOut);
			    	  }
			      }catch(Exception e){
			    	  e.printStackTrace();
			      }
			    }
			    socket.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
