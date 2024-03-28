package com.example.taberogu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Role findByName(String name); 
}
