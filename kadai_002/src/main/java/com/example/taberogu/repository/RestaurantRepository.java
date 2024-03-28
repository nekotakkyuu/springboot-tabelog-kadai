package com.example.taberogu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	  
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByCategoryIdOrderByCreatedAtDesc(Integer category, Pageable pageable);
    public Page<Restaurant> findByCategoryIdOrderByPriceAsc(Integer category, Pageable pageable);
    public Page<Restaurant> findByPriceLikeOrderByCreatedAtDesc(String price, Pageable pageable);
    public Page<Restaurant> findByPriceLikeOrderByPriceAsc(String price, Pageable pageable); 
    public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);
    
    public List<Restaurant> findTop10ByOrderByCreatedAtDesc();
}
