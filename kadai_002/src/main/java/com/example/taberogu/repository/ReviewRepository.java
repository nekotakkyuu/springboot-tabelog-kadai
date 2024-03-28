package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	List<Review> findByRestaurant(Restaurant restaurant);
}
