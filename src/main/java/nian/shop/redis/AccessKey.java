package nian.shop.redis;
/**
* @author created by NianTianlei
* @createDate on 2018年6月4日 下午9:26:39
*/
public class AccessKey extends BasePrefix{

	public static AccessKey access = new AccessKey(5, "access");

	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}
	
}
