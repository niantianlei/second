package nian.shop.VO;

import java.util.Date;

import nian.shop.entity.Goods;

public class GoodsVo extends Goods{
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	private Double secondPrice;
	
	public Double getSecondPrice() {
		return secondPrice;
	}
	public void setSecondPrice(Double secondPrice) {
		this.secondPrice = secondPrice;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
