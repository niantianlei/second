package nian.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nian.rabbitmq.SecondKillMessage;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.GoodsVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.redis.GoodsKey;
import nian.shop.redis.OrderKey;
import nian.shop.redis.SecondKey;
import nian.shop.service.GoodsService;
import nian.shop.service.MqSender;
import nian.shop.service.OrderService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondKillService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.SecondResEnum;

@Controller
@RequestMapping("/second")
public class SecondController implements InitializingBean {

	@Autowired
	SecondUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SecondKillService secondKillService;
	
	@Autowired
	MqSender mqSender;
	
	private Map<Long, Boolean> localOverMap = new HashMap<>();
	
    @PostMapping("/do_secondKill")
    @ResponseBody
    public ResultDTO<String> secondKill(Model model, SecondUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return ResultDTO.fail("session错误");
    	}
    	/*
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		return ResultDTO.fail("秒杀结束");
    	}
    	//判断是否已经秒杀到了
    	SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		//model.addAttribute("errmsg", SecondResEnum.SECOND_KILL_REPEAT.getMsg());
    		return ResultDTO.fail("重复秒杀错误");
    	}
    	//减库存 下订单 写入秒杀订单
    	OrderInfo orderInfo = secondKillService.secondKill(user, goods);
        return ResultDTO.success(orderInfo);
        */
    	
    	//内存标记，减少redis访问
    	boolean over = localOverMap.get(goodsId);
    	if(over) {
    		return ResultDTO.fail("秒杀已结束");
    	}
    	//预减库存
    	long stock = redisService.decr(GoodsKey.getSecondKillGoodsStock, "" + goodsId);//10
    	if(stock < 0) {
    		 localOverMap.put(goodsId, true);
    		return ResultDTO.fail("秒杀已结束");
    	}
    	//判断是否已经秒杀到了
    	SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return ResultDTO.fail("重复秒杀错误");
    	}
    	//入队
    	SecondKillMessage sm = new SecondKillMessage();
    	sm.setUser(user);
    	sm.setGoodsId(goodsId);
    	mqSender.sendSecondKillMessage(sm);
    	return ResultDTO.success("成功进入排队中");
    	
    }
    
    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @GetMapping("/result")
    @ResponseBody
    public ResultDTO<Long> miaoshaResult(Model model, SecondUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return ResultDTO.fail("session错误");
    	}
    	long result = secondKillService.getSecondKillResult(user.getId(), goodsId);
    	return ResultDTO.success(result);
    }
    
    /**
     * 系统初始化
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		if(goodsList == null) {
			return;
		}
		for(GoodsVo goods : goodsList) {
			redisService.set(GoodsKey.getSecondKillGoodsStock, "" + goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
	}
	
	/**
	 * 数据初始化
	 * @param model
	 * @return
	 */
	@GetMapping("/reset")
    @ResponseBody
    public ResultDTO<Boolean> reset(Model model) {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		for(GoodsVo goods : goodsList) {
			goods.setStockCount(10);
			redisService.set(GoodsKey.getSecondKillGoodsStock, ""+goods.getId(), 10);
			localOverMap.put(goods.getId(), false);
		}
		redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
		redisService.delete(SecondKey.isGoodsOver);
		secondKillService.reset(goodsList);
		return ResultDTO.success(true);
	}
}
