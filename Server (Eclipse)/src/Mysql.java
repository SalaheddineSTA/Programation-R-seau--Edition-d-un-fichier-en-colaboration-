import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Statement;


public class Mysql {
	private String server;
	private String dbUser;
	private String dbName;
	private String dbPassword;
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ResultSetMetaData rsmd;
	public  Mysql(String server ,String  dbUser,String dbName,String dbPassword){
		this.server = server;
		this.dbUser = dbUser;
		this.dbName = dbName;
		this.dbPassword = dbPassword;
		
	}
	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 connect = DriverManager.getConnection("jdbc:mysql://"+server+"/"+dbName+"?"
                                     + "user="+dbUser+"&password="+dbPassword);
			 connect.createStatement();
		}catch (Exception e) {

			e.printStackTrace();
		}
	}
	public void sendRequest(String req , ArrayList<String> data ){
		try{ 
			preparedStatement = connect.prepareStatement(req);
			for(int i=0; i < data.size() ; i++ ){
				preparedStatement.setString( i+1 , data.get(i));
			}
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String sendRequestWithLastId(String req , ArrayList<String> data){
		String id="";
		try{ 
			preparedStatement = connect.prepareStatement(req , Statement.RETURN_GENERATED_KEYS);
			for(int i=0; i < data.size() ; i++ ){
				preparedStatement.setString( i+1 , data.get(i));
			}
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			
			if (rs.next()){
				rsmd = rs.getMetaData();
				id = rs.getString(1).toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	public void fetchAll(String req , ArrayList<String> data ,  ArrayList<Map<String, String>> out){
		
		try{ 
			preparedStatement = connect.prepareStatement(req);
			for(int i=0; i < data.size() ; i++ ){
				preparedStatement.setString( i+1 , data.get(i));
			}
			
			resultSet = preparedStatement.executeQuery();
			
			
			while(resultSet.next()){
				rsmd = resultSet.getMetaData();
			
				Map<String, String> aMap = new HashMap<String, String>();
				for(int j = 1; j <= rsmd.getColumnCount() ; j++){
					aMap.put(rsmd.getColumnName(j), resultSet.getString(rsmd.getColumnName(j)));
				}
				out.add(aMap);
			}
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}
	public void fetch(String req , ArrayList<String> data ,  Map<String, String> out){
		
		try{ 
			preparedStatement = connect.prepareStatement(req);
			for(int i=0; i < data.size() ; i++ ){
				preparedStatement.setString( i+1 , data.get(i));
			}
			
			
			resultSet = preparedStatement.executeQuery();
			

			while(resultSet.next()){
				rsmd = resultSet.getMetaData();
				for(int j = 1; j <= rsmd.getColumnCount() ; j++){
					out.put(rsmd.getColumnName(j), resultSet.getString(rsmd.getColumnName(j)));
				}
				break;
			}
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	public int getNum(String req , ArrayList<String> data){
		int num =0;
		try{ 
			preparedStatement = connect.prepareStatement(req);
			for(int i=0; i < data.size() ; i++ ){
				preparedStatement.setString( i+1 , data.get(i));
			}
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				num++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return num;
	}

}
