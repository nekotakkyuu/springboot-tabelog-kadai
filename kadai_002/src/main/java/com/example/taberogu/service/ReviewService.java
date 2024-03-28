package com.example.taberogu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReviewEditForm;
import com.example.taberogu.form.ReviewRegisterForm;
import com.example.taberogu.repository.ReviewRepository;

@Service
public class ReviewService {
	
    private final ReviewRepository reviewRepository;  
    
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;  
    }    
    
    @Transactional
    public void create(Restaurant restaurant, User user, ReviewRegisterForm reviewRegisterForm) { 
        Review review = new Review();       
                
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setContent(reviewRegisterForm.getContent()); 
        review.setEvaluation(reviewRegisterForm.getEvaluation()); 
        
        reviewRepository.save(review);
    }   
    
    @Transactional
    public void update(Restaurant restaurant, User user, ReviewEditForm reviewEditForm) { 
        Review review = reviewRepository.getReferenceById(reviewEditForm.getReview_id());     
                
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setContent(reviewEditForm.getContent()); 
        review.setEvaluation(reviewEditForm.getEvaluation()); 
        
        reviewRepository.save(review);
    }   
 
}
