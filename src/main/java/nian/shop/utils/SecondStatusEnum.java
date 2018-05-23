package nian.shop.utils;
/**
* @author created by NianTianlei
* @createDate on 2018年5月23日 下午9:08:54
*/
public enum SecondStatusEnum {
	NO_PAY(0, "新建未支付"),
	PAID(1, "已支付"),
	NO_SHIP(2, "未发货"),
	SHIPPED(3, "已发货"),
	RECEIVED(4, "已收货"),
	SIGNED(5, "已签收"),
	REFUNDED(6, "已退款"),
	FINISHED(7, "已完成"),
	;
	private int status;
	private String info;
	private SecondStatusEnum(int status, String info) {
		this.info = info;
		this.status = status;
	}
	public int getStatus() {
		return status;
	}

	public String getInfo() {
		return info;
	}
}
