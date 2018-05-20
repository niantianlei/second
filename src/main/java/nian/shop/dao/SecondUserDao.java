package nian.shop.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import nian.shop.entity.SecondUser;

@Mapper
public interface SecondUserDao {
	@Select("select * from second_user where id = #{id}")
	public SecondUser getById(@Param("id")long id);
}
