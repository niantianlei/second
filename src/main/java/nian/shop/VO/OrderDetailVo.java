package nian.shop.VO;

import lombok.Data;
import nian.shop.entity.OrderInfo;

@Data
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;

}
