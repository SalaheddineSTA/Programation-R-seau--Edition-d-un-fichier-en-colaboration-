import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;


public class Connection {
	private Mysql db;
	Crypt c = new Crypt();
	public Connection(Mysql db){
		this.db = db;
	}
	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
	private CharSequence praga;
	public String login(String user, String password ){
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String> datas = new ArrayList<String>();
		String req = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ? ";
		data.add(user);
		data.add(password);
		if( user == ""){
			return "user empty";
		}
		if( password == "" ){ 
			return "password empty";
		}
		if(db.getNum(req, data) > 0 ){ 
			datas.add(c.uniqueId());
			datas.add(user);
			datas.add(password);
			db.sendRequest("UPDATE `user` SET `session`=? WHERE `username`= ? AND `password`= ? ", datas);
			return "logined";
		}else{ 
			return "user or password";
		}
	}
	public void getUserInfo(String user, String password ,Map<String, String>  out ){
		ArrayList<String> data = new ArrayList<String>();
		String req = "SELECT * FROM `user` WHERE `username` = ? AND `password` = ? ";
		data.add(user);
		data.add(password);
		db.fetch(req, data , out);
	}
	public String register(String user,String password,String email){
		ArrayList<String> data = new ArrayList<String>();
		data.add(user);
		if( user == ""){
			return "user empty";
		}
		if( password == "" ){ 
			return "password empty";
		}
		if(!isValidEmailAddress(email) ){ 
			return "email not valid";
		}
		if( db.getNum("SELECT * FROM `user` WHERE `username` = ? ", data ) > 0 ) { 
			return "user exist";
		}
		data.clear();
		data.add(email);
		if( db.getNum("SELECT * FROM `user` WHERE `email` = ? ", data ) > 0 ) { 
			return "email exist";
		}
		data.clear();
		data.add(user);
		data.add(password);
		data.add(email);
		db.sendRequest("INSERT INTO `user`( `username`, `password`, `email`) VALUES (?,?,?)", data);
		return "registred";
	}
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	public String addProjects(JSONObject data ){
		String ids ="";
		if(data.get("name").toString() == ""  ){
			return "name empty";
		}
		if(data.get("type").toString() == ""){
			return("type empty");
		}
		ArrayList<String> datas = new ArrayList<String>();
		ArrayList<String> datass = new ArrayList<String>();
		datas.add(data.get("type").toString());
		if( db.getNum("SELECT * FROM `type` WHERE `id` = ?" , datas) == 0 ){
			return "type not exist";
		}
		datas.add(data.get("name").toString());
		datas.add(timeToStr());
		datas.add(data.get("id").toString());
		
		ids = db.sendRequestWithLastId("INSERT INTO `projects`( `type` , `name`, `date`, `user`) VALUES (?,?,?,?)" , datas );
		
		datass.add(data.get("id").toString());
		datass.add(ids);
		db.sendRequest("INSERT INTO `shared`(`user` , `project`) VALUES (?,?)", datass);
		boolean success = (new File("Projects/"+ids)).mkdirs();
		PrintWriter writer;
		try {
			writer = new PrintWriter("Projects/"+ids+"/example.c", "UTF-8");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "created";
	}
	public ArrayList<Map<String, String>> getProjectMap(String user ){
		ArrayList<Map<String, String>> out  = new ArrayList<Map<String, String>>();
		ArrayList<String> data = new ArrayList<String>();
		data.add(user);
		db.fetchAll("SELECT * FROM `shared` WHERE `user` = ? " , data , out);
		return out;
	}
	public String getProject(String user ){
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		ArrayList<Map<String, String>> out  = new ArrayList<Map<String, String>>();
		out = getProjectMap(user);
		ArrayList<String> data2 = new ArrayList<String>();
		Map<String, String> out2 = new HashMap<String, String>();
		for(Map<String, String>  f : out){
			data2.clear();
			data2.add(f.get("project"));
			db.fetch("SELECT * FROM `projects` WHERE `id` = ?", data2, out2);
			jsontmp.put(f.get("project"),out2.get("name"));
		}
		try{
			jsontmp.writeJSONString(jsonOut);
		}catch(Exception e){
			
		}
		return jsonOut.toString();
	}
	public String getFiles(String user,String ProjectId){
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(ProjectId);
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			File directory = new File("Projects/"+ProjectId);
			File[] fList = directory.listFiles();
			int i=0;
			try{
				for (File file : fList) {
					if( file.isFile() && file.getName().endsWith("~") == false)
						jsontmp.put(file.getName(),"file");
					else if( file.isDirectory() && file.getName().endsWith("~") == false )
						jsontmp.put(file.getName(),"directory");
					i++;
				}
			}catch(Exception e){		
				
			}
			
			try{
				jsontmp.writeJSONString(jsonOut);
			}catch(Exception e){		
				
			}
		}
		
		return jsonOut.toString();
	}
	public JSONObject getType(){
		JSONObject json = new JSONObject();
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<Map<String, String>>  out = new ArrayList<Map<String, String>>();
		db.fetchAll("SELECT * FROM `type` ", data, out);
		for(Map aMap: out){
			json.put(aMap.get("id"), aMap.get("name"));
		}
		return json;
	}
	public String getFileConents(String user,String projectId, String filename ){
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(projectId);
		String content = "";
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			try{
			if( illegalFileName(filename) == false){
				byte[] encoded = Files.readAllBytes(Paths.get("Projects/"+projectId+"/"+filename));
				content = new String(encoded);
			}
				
			}catch(Exception e){
				
			}
		}
		jsontmp.put(filename, content);
		try{
			jsontmp.writeJSONString(jsonOut);
		}catch(Exception e){		
		}
		return jsonOut.toString();
	}
	public String CreatFile(String user,String projectId,String fileName){
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(projectId);
		jsontmp.clear();
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			// compress project 
			PrintWriter writer;
			try {
				writer = new PrintWriter("Projects/"+projectId+"/"+fileName, "UTF-8");
				writer.close();
				jsontmp.put("file", "created");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
			
		}
		return jsonOut.toString();
	}
	public String getZipProject(String user,String projectId){
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(projectId);
		jsontmp.clear();
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			// compress project 
			new Compress("Projects/"+projectId+".zip" , "Projects/"+projectId);
			File f = new File("Projects/"+projectId+".zip");
			try {
				byte[] bytes = Files.readAllBytes(f.toPath());
				jsontmp.put("file", DatatypeConverter.printBase64Binary(bytes));
				jsontmp.writeJSONString(jsonOut);
			} catch (IOException e) {
				
			}
		}
		return jsonOut.toString();
	}
	public String SetContents(String user,String projectId, String filename ,String ctx){
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(projectId);
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			File f = new File("Projects/"+projectId+"/"+filename);
			File ex = new File("Projects/"+projectId+"/"+filename+"~");
			if(f.exists() ) { 
				
				boolean success = f.renameTo(ex);
			}
			PrintWriter writer;
			try {
				writer = new PrintWriter(f, "UTF-8");
				writer.println(ctx);
			    writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		String content = "edited";
		return "";
	}
	public String getUsers(){
		JSONObject json = new JSONObject();
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<Map<String, String>>  out = new ArrayList<Map<String, String>>();
		db.fetchAll("SELECT * FROM `user` ", data, out);
		for(Map aMap: out){
			json.put(aMap.get("id"), aMap.get("username"));
		}
		return json.toJSONString();
	}
	
	public String addUserToProject(String user,String userto , String idp){
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String> datas = new ArrayList<String>();
		JSONObject jsontmp = new JSONObject();
		StringWriter jsonOut = new StringWriter();
		data.add(user);
		data.add(idp);
		jsontmp.clear();
		if( db.getNum("SELECT * FROM `shared` WHERE  `user` = ? AND `project` = ?" , data) > 0 ){
			datas.add(idp);
			datas.add(userto);
			db.sendRequest("INSERT INTO `shared`(`project`, `user`) VALUES (?,?)", datas);
			return "added";
		}
		return "error";
	}
	public boolean illegalFileName(String fileName){
		for(char  now : ILLEGAL_CHARACTERS){
			if( fileName.indexOf(now) != -1 ) return true;
		}
		return false;
	}
	public String timeToStr(){
			System.setProperty("javax.net.ssl.trustStore" , "encrypted.store");
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		    return sdf.format(cal.getTime()).toString();
	}
	
	public boolean isConnected(String id , String session){
		ArrayList<String> datas = new ArrayList<String>();
		datas.add(id);
		datas.add(session);
		
		if( db.getNum("SELECT * FROM `user` WHERE `id` = ? AND `session` = ? ", datas) > 0 ){
			return true;
		}
		return false;
	}
	
}
