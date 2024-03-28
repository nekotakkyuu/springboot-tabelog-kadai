package com.example.taberogu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Role;
import com.example.taberogu.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email);
	public Page<User> findByNameLikeOrEmailLike(String nameKeyword, String emailKeyword, Pageable pageable);
	public User findByRoleId(Role roleId);
}
