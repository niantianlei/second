package nian.shop.redis;

public class SecondKey extends BasePrefix{

	private SecondKey(String prefix) {
		super(prefix);
	}
	public static SecondKey isGoodsOver = new SecondKey("go");
}
