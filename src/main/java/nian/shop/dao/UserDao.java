package nian.shop.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import nian.shop.entity.User;

/**
 * @author Niantianlei
 * @date 2018年5月17日 下午2:16:20
 */
@Mapper
public interface UserDao {
	
	@Select("select * from user where id = #{id}")
	public User getById(@Param("id")int id);

	@Insert("insert into user(id,name)values(#{id}, #{name})")
	public int insert(User user);
}
   