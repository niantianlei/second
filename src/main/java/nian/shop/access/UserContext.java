package nian.shop.access;

import nian.shop.entity.SecondUser;

public class UserContext {
	
	private static ThreadLocal<SecondUser> userHolder = new ThreadLocal<>();
	
	public static void setUser(SecondUser user) {
		userHolder.set(user);
	}
	
	public static SecondUser getUser() {
		return userHolder.get();
	}

}
