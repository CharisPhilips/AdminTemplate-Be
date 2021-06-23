package com.kilcote.dao.system.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kilcote.entity.system.User;

@Mapper
public interface UserMapper {
	void insertUser(User user);
	List<User> findByUserid(String userid);
	List<User> findAllUsers();
	void updateUser(User user);
	void removeUser(Integer idUser);
}
