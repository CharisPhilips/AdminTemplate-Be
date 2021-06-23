package com.kilcote.dao.system.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kilcote.entity.system.User;

@Repository("userJpaRepository")
public interface UserJpaRepository extends JpaRepository<User, Long> {
    User getOne(Long id);
	@SuppressWarnings("unchecked")
	public User save(User entity);
    List<User> findAll();
}
