package nian.shop.redis;

public class SecondUserKey extends BasePrefix{

	private static final int TOKEN_EXPIRE_TIME = 3600*24 * 2;
	private SecondUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static SecondUserKey token = new SecondUserKey(TOKEN_EXPIRE_TIME, "tk");
}
