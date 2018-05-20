package nian.shop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(src == null || src.length() == 0) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	/*public static void main(String[] args) {
		System.out.println(isMobile("13812345678"));
		System.out.println(isMobile("1381234567"));
	}*/
}
