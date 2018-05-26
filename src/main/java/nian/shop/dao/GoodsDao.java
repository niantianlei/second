package nian.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import nian.shop.VO.GoodsVo;
import nian.shop.entity.SecondGoods;


@Mapper
public interface GoodsDao {
	
	@Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.second_price from second_goods sg left join goods g on sg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,sg.stock_count, sg.start_date, sg.end_date,sg.second_price from second_goods sg left join goods g on sg.goods_id = g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

	@Update("update second_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	public int reduceStock(SecondGoods g);
	
}
