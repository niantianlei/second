package nian.shop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nian.shop.dao.UserDao;
import nian.shop.entity.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public User getUserById(int id) {
		return userDao.getById(id);
	}
	
	@Transactional
	public boolean tx() {
		// TODO Auto-generated method stub
		User u1 = new User();
		u1.setId(2);
		u1.setName("123");
		userDao.insert(u1);

		User u2 = new User();
		u2.setId(1);
		u2.setName("u1");
		userDao.insert(u2);
		return true;
	}

}
