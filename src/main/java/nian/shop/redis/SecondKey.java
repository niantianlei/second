package nian.shop.redis;

public class SecondKey extends BasePrefix{

	private SecondKey(String prefix) {
		super(prefix);
	}
	private SecondKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static SecondKey isGoodsOver = new SecondKey("go");
	public static SecondKey secondKillPath = new SecondKey(60, "skp");
	public static SecondKey secondKillVerifyCode = new SecondKey(300, "skvc");
}
