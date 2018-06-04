package nian.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.alibaba.fastjson.JSON;

import nian.shop.DTO.ResultDTO;
import nian.shop.VO.GoodsDetailVo;
import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.redis.GoodsKey;
import nian.shop.service.GoodsService;
import nian.shop.service.OrderService;
import nian.shop.service.RedisService;
import nian.shop.service.SecondKillService;
import nian.shop.service.SecondUserService;
import nian.shop.utils.ValidatorUtil;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	SecondUserService userService;
	@Autowired
	RedisService redisService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	OrderService orderService;
	
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
	
    /*@RequestMapping("/to_list")
    public String list(Model model, SecondUser user) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
    	model.addAttribute("goodsVoList", goodsVoList);
        return "goods_list";
    }*/
	
    //页面缓存
    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SecondUser user) {
    	model.addAttribute("user", user);
    	
    	//取缓存
    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
    	if(!ValidatorUtil.isEmpty(html)) {
    		return html;
    	}
    	List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
    	model.addAttribute("goodsVoList", goodsVoList);
//    	 return "goods_list";
    	SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
    	//手动渲染
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
    	if(!ValidatorUtil.isEmpty(html)) {
    		redisService.set(GoodsKey.getGoodsList, "", html);
    		
    	}
    	
    	return html;
    }
    
    @RequestMapping(value="/to_detail2/{goodsId}", produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,SecondUser user,
    		@PathVariable("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	
    	//取缓存
    	String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
    	if(!ValidatorUtil.isEmpty(html)) {
    		return html;
    	}
    	//手动渲染
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
//        return "goods_detail";
    	
    	SpringWebContext ctx = new SpringWebContext(request,response,
    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
    	if(!ValidatorUtil.isEmpty(html)) {
    		redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
    	}
    	return html;
    }
    
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public ResultDTO<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, SecondUser user,
    		@PathVariable("goodsId")long goodsId) {

    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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
    	GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
    	goodsDetailVo.setGoods(goods);
    	goodsDetailVo.setUser(user);
    	goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
    	goodsDetailVo.setRemainSeconds(remainSeconds);
    	//新增        首次秒杀field
    	goodsDetailVo.setIsFirst(isFirst(user, goodsId));
    	log.info(JSON.toJSONString("商品详情信息: " + goodsDetailVo));
    	return ResultDTO.success(goodsDetailVo);
    }
    
    //判断是否为首次秒杀
    private int isFirst(SecondUser user, long goodsId) {
    	SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(user.getId(), goodsId);
    	if(order == null) {
    		return 1;
    	} else {
    		return 0;
    	}
    }
/*    @RequestMapping("/to_detail/{goodsId}")
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
    }*/
}
