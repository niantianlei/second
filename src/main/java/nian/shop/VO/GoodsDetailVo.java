package nian.shop.VO;

import lombok.Data;
import nian.shop.entity.SecondUser;

@Data
public class GoodsDetailVo {
	private int miaoshaStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private SecondUser user;
	
}
