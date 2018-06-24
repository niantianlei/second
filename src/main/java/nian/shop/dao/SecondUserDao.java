package nian.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import nian.shop.entity.SecondUser;

@Mapper
public interface SecondUserDao {
	@Select("select * from second_user where id = #{id}")
	public SecondUser getById(@Param("id")long id);

	@Update("update second_user set password = #{password} where id = #{id}")
	public void update(SecondUser toBeUpdate);
	
	@Select("select * from second_user")
	public List<SecondUser> queryAllSecondUser();
}
