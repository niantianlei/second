package nian.shop.entity;

import java.util.Date;

import lombok.Data;

@Data
public class SecondGoods {
	private Long id;
	private Long goodsId;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
}
