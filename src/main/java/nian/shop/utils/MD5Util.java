package nian.shop.utils;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * 
 * @author Niantianlei
 * @date 2018年5月18日 下午3:13:59
 */
public class MD5Util {
	public static String md5(String input) {
		return DigestUtils.md5Hex(input);
	}
	
	private static final String SALT = "f9064abc";
	
	//第一次md5
	public static String addSaltProcess(String input) {
		String output = "" + SALT.substring(0, 2) + input + SALT.charAt(6) + SALT.charAt(4);
		return md5(output);
	}
	//第二次md5
	public static String constructDBPassword(String input, String salt) {
		String output = "" + salt.substring(0, 2) + input + salt.charAt(6) + salt.charAt(4);
		return md5(output);
	}
	
	
	public static String FromInputToOutput(String input, String saltInDB) {
		String str = addSaltProcess(input);
		String output = constructDBPassword(str, saltInDB);
		return output;
	}
	public static void main(String[] args) {
		//产生salt
		String uuuid = UUID.randomUUID().toString().substring(0, 8);
		System.out.println(uuuid);
		
		System.out.println(addSaltProcess("123456")); //4672d95aab2010d7501dee60f1ef1ee9
		System.out.println(constructDBPassword(addSaltProcess("123456"), "32165498"));
	}
	
}
