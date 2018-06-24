package nian.shop.redis;

public class GoodsKey extends BasePrefix {

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	private GoodsKey(String prefix) {
		super(prefix);
	}
	
	public GoodsKey(int expireSeconds, boolean addRandom, String prefix) {
		
		super(expireSeconds, addRandom, prefix);
	}

	public static GoodsKey getGoodsList = new GoodsKey(30, "gl");
	public static GoodsKey getGoodsDetail1 = new GoodsKey(60, "gd");
	public static GoodsKey getSecondKillGoodsStock = new GoodsKey("gs");
}
