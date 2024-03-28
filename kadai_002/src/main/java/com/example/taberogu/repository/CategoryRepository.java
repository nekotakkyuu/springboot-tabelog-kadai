package com.example.taberogu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	public Page<Category> findByNameLike(String keyword, Pageable pageable);
}
