package com.example.taberogu.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.repository.RestaurantRepository;

@Controller
public class HomeController {
    private final RestaurantRepository restaurantRepository;        
    
    public HomeController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;            
    }   
	@GetMapping("/")
    public String index(Model model) {
        List<Restaurant> newRestaurants = restaurantRepository.findTop10ByOrderByCreatedAtDesc();
        model.addAttribute("newRestaurants", newRestaurants);
        
		return "index";
	}
}
