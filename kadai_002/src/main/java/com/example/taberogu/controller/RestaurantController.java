package com.example.taberogu.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReservationInputForm;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.security.UserDetailsImpl;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;        
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    
    public RestaurantController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, FavoriteRepository favoriteRepository) {
        this.restaurantRepository = restaurantRepository;            
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
    }     
    
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "category", required = false) Integer category,
                        @RequestParam(name = "price", required = false) String price,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Restaurant> restaurantPage;
                
        if (keyword != null && !keyword.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%",  pageable);
            } 
        } else if (category != null) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByCategoryIdOrderByPriceAsc(category, pageable);
            } else {
                restaurantPage = restaurantRepository.findByCategoryIdOrderByCreatedAtDesc(category, pageable);
            }
        } else if (price != null && !price.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByPriceLikeOrderByPriceAsc("%" + price + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByPriceLikeOrderByCreatedAtDesc("%" + price + "%", pageable);
            } 
        } else {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);   
            } 
        }                
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("price", price);
        model.addAttribute("order", order);
        
        return "restaurants/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        User user;
        List<Review> reviews = reviewRepository.findByRestaurant(restaurant);
        
        if (userDetailsImpl != null) { 
    		user = userDetailsImpl.getUser();
    		Favorite favorite = favoriteRepository.findByUserAndRestaurant(user, restaurant);
    		model.addAttribute("favorite", favorite);
    		}
        
        model.addAttribute("restaurant", restaurant);     
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("reviews", reviews);
        
        return "restaurants/show";
    }   
    
}
