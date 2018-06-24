package nian.shop.redis;

import java.util.Random;

public abstract class BasePrefix implements KeyPrefix{
	
	private int expireSeconds;
	
	private String prefix;
	
	public BasePrefix(String prefix) {//0代表永不过期
		this(0, prefix);
	}
	
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	public BasePrefix(int expireSeconds, boolean addRandom, String prefix) {
		if(addRandom) {
			Random random = new Random(System.currentTimeMillis());
			int extend = expireSeconds / 5;
			int ran = random.nextInt(extend);
			int finalExtend = extend / 2 - ran;
			this.expireSeconds = expireSeconds + finalExtend;
		} else {
			this.expireSeconds = expireSeconds;
		}
		this.prefix = prefix;
	}
	
	public int expireSeconds() {//默认0代表永不过期
		return expireSeconds;
	}

	public String getPrefix() {
		String className = getClass().getSimpleName();
		return className+":" + prefix;    //类名：prefix
	}

}
