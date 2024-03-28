package com.example.taberogu.service;

import org.springframework.stereotype.Service;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {
	
	private final FavoriteRepository favoriteRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
		this.favoriteRepository = favoriteRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void create(Integer restaurantId, Integer userId) {
		Favorite favorite = new Favorite();
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		User user = userRepository.getReferenceById(userId);
		
		favorite.setRestaurant(restaurant);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	}

}