package second;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nian.shop.App;
import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondUser;
import nian.shop.service.GoodsService;
import nian.shop.service.OrderService;

/**
* @author created by NianTianlei
* @createDate on 2018年5月29日 上午9:36:47
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestService {
	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	
	
	@Test
	public void test() {
		/*//秒杀逻辑
		SecondUser user = new SecondUser();
		user.setId(13812345678l);
		user.setPassword("30c07928a3d6539c2367dd664179644b");
		GoodsVo goods = new GoodsVo();
		goods.setId(1L);
		goods.setSecondPrice(0.1);
		goodsService.reduceStock(goods);*/
		
		//common-lang3版本冲突？
		String s = "";
		System.out.println(StringUtils.isEmpty(s));
	}
}
