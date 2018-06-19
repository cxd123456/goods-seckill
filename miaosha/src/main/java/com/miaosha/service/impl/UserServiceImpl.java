package com.miaosha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miaosha.entity.User;
import com.miaosha.mapper.UserDao;
import com.miaosha.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public User getUserById(Integer id) {
		return userDao.getUserById(id);
	}

	@Transactional
	@Override
	public void Insert() {
		User user = new User();
		user.setId(2);
		user.setName("lisi");
		
		userDao.insert(user);
		
		user.setId(1);
		user.setName("wangwu");
		
		userDao.insert(user);
		
	}

}
