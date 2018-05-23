package nian.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondUser;
import nian.shop.service.GoodsService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.ValidatorUtil;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	SecondUserService userService;
	@Autowired
	RedisService redisService;
	@Autowired
	GoodsService goodsService;
	
/*    @RequestMapping("/to_list")
    public String list(HttpServletResponse response, Model model,
    		@CookieValue(value=SecondUserService.COOKIE_NAME_TOKEN, required=false)String cookieToken, 
    		@RequestParam(value=SecondUserService.COOKIE_NAME_TOKEN, required=false)String paramToken) {
        if(ValidatorUtil.isEmpty(cookieToken) == ValidatorUtil.isEmpty(paramToken)) {
        	return "login";
        }
        String token = ValidatorUtil.isEmpty(paramToken) ? cookieToken : paramToken;
        SecondUser user = userService.getByToken(response, token);
        model.addAttribute("user", user);
    	return "goods_list";
    }*/
    @RequestMapping("/to_list")
    public String list(Model model, SecondUser user) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
    	model.addAttribute("goodsVoList", goodsVoList);
        return "goods_list";
    }
    
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, SecondUser user,
    		@PathVariable("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	model.addAttribute("goods", goods);
    	
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	
    	int miaoshaStatus = 0;
    	int remainSeconds = 0;
    	if(now < startAt ) {//秒杀还没开始，倒计时
    		miaoshaStatus = 0;
    		remainSeconds = (int)((startAt - now )/1000);
    	}else  if(now > endAt){//秒杀已经结束
    		miaoshaStatus = 2;
    		remainSeconds = -1;
    	}else {//秒杀进行中
    		miaoshaStatus = 1;
    		remainSeconds = 0;
    	}
    	model.addAttribute("miaoshaStatus", miaoshaStatus);
    	model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
