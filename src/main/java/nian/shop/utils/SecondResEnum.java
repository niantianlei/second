package nian.shop.utils;

public enum SecondResEnum {
	SECOND_KILL_OVER("库存不足"),
	SECOND_KILL_REPEAT("禁止重复秒杀"),
	;
	private String msg;
	private SecondResEnum(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
}
