import java.util.UUID;
import java.security.MessageDigest;
import java.math.BigInteger;

public class Crypt {
	public String md5(String str){
		try{
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(str.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			str = hashtext;
		}catch(Exception e ){
			e.printStackTrace();
		}
		return str;
	}
	public String uniqueId(){
		return md5(UUID.randomUUID().toString());
	}
	
}
