package nian.shop.entity;

import lombok.Data;

@Data
public class SecondOrder {
	private Long id;
	private Long userId;
	private Long orderId;
	private Long goodsId;
	
}
