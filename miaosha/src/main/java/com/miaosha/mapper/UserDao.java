package com.miaosha.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.miaosha.entity.User;

public interface UserDao {

	@Select("SELECT * FROM USER WHERE id = #{id}")
	public User getUserById(int id);
	
	@Insert("INSERT INTO USER (id, name) values (#{id}, #{name})")
	public int insert(User user);
	
}
