package com.example.taberogu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	public Favorite findByUserAndRestaurant(User user, Restaurant restaurant);
}

