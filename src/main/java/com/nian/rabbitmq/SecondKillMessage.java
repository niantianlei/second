package com.nian.rabbitmq;

import lombok.Data;
import nian.shop.entity.SecondUser;

@Data
public class SecondKillMessage {
	private SecondUser user;
	private long goodsId;

}
