package nian.shop.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.OrderInfo;
import nian.shop.entity.SecondOrder;
import nian.shop.entity.SecondUser;
import nian.shop.redis.SecondKey;
import nian.shop.utils.MD5Util;
import nian.shop.utils.UUIDUtil;

@Service
public class SecondKillService {
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo secondKill(SecondUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean res = goodsService.reduceStock(goods);
		if(res) {
			return orderService.createOrder(user, goods);
		} else {
			setGoodsOver(goods.getId());
			return null;
		}
	}


	public long getSecondKillResult(Long userId, long goodsId) {
		SecondOrder order = orderService.getSecondOrderByUserIdandGoodsId(userId, goodsId);
		if(order != null) {
			return order.getOrderId();
		} else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private void setGoodsOver(Long goodsId) {
		redisService.set(SecondKey.isGoodsOver, "" + goodsId, true);
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.isExist(SecondKey.isGoodsOver, "" + goodsId);
	}


	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}
	
	
	public boolean checkPath(SecondUser user, long goodsId, String path) {
		if(user == null || path == null) {
			return false;
		}
		String pathOld = redisService.get(SecondKey.secondKillPath, "" + user.getId() + "_" + goodsId, String.class);
		return path.equals(pathOld);
	}

	public String createSecondKillPath(SecondUser user, long goodsId) {
		if(user == null || goodsId <= 0) {
			return null;
		}
		String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
    	redisService.set(SecondKey.secondKillPath, "" + user.getId() + "_" + goodsId, str);
		return str;
	}

	public BufferedImage createVerifyCode(SecondUser user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		int width = 95;
		int height = 32;
		//画图
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//拿到graphics对象，
		Graphics g = image.getGraphics();
		//设置颜色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		//设置颜色
		g.setColor(new Color(50, 50, 50));
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		//随机值
		for (int i = 0; i < 300; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(30, 80, 30));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calculate(verifyCode);
		redisService.set(SecondKey.secondKillVerifyCode, user.getId() + "," + goodsId, rnd);
		//输出图片	
		return image;
	}

	public boolean checkVerifyCode(SecondUser user, long goodsId, int verifyCode) {
		if(user == null || goodsId <= 0) {
			return false;
		}
		Integer codeOld = redisService.get(SecondKey.secondKillVerifyCode, user.getId() + "," + goodsId, Integer.class);
		if(codeOld == null || codeOld - verifyCode != 0 ) {
			return false;
		}
		redisService.delete(SecondKey.secondKillVerifyCode, user.getId() + "," + goodsId);
		return true;
	}
	

	private static char[] ops = new char[] {'+', '-', '*'};

	//产生验证公式
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
	    int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		int num4 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		char op3 = ops[rdm.nextInt(3)];
		String exp = ""+ num1 + op1 + num2 + op2 + num3 + op3 + num4;
		return exp;
	}
	public static void main(String[] args) {
		System.out.println(calculate("1+3*5"));
	}
	
	private static int calculate(String s) {
        if(s == null || s.length() == 0) return 0;
        int len = s.length();
        Stack<Integer> stack = new Stack<>();
        int num = 0;
        char sign = '+';
        for(int i = 0; i < len; i++) {
            if(Character.isDigit(s.charAt(i))) {
                num = num * 10 + s.charAt(i) - '0';
            }
            if(!Character.isDigit(s.charAt(i)) && ' ' != s.charAt(i) || i == len - 1) {
                if(sign == '-') {
                    stack.push(-num);
                } else if (sign == '+') {
                    stack.push(num);
                } else if (sign == '*') {
                    stack.push(stack.pop() * num);
                } else if (sign == '/') {
                    stack.push(stack.pop() / num);
                }
                sign = s.charAt(i);
                num = 0;
            }
        }
        int res = 0;
        for(int i : stack) {
            res += i;
        }
        return res;
    }
}
