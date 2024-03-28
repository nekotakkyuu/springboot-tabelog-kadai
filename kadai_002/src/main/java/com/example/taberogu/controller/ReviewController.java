package com.example.taberogu.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.Review;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.ReviewEditForm;
import com.example.taberogu.form.ReviewRegisterForm;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.repository.ReviewRepository;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.ReviewService;

@Controller
@RequestMapping("/restaurants/{restaurant_id}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
		this.reviewService = reviewService;
		
	}
	
    @GetMapping("/register")        
    public String register(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@ModelAttribute ReviewRegisterForm reviewRegisterForm, @PathVariable(name = "restaurant_id") Integer restaurant_id, Model model) {
    	User user = userDetailsImpl.getUser();
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
    	List<Review> reviews = reviewRepository.findByRestaurant(restaurant);
 
    	
    	model.addAttribute("restaurant", restaurant); 
        model.addAttribute("reviewRegisterForm", reviewRegisterForm);
        model.addAttribute("reviews", reviews);

        
        return "reviews/register";
    }
    
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,
    		RedirectAttributes redirectAttributes, @PathVariable(name = "restaurant_id") Integer restaurant_id, Model model) {   
    	
    	User user = userDetailsImpl.getUser();
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
    	
        if (bindingResult.hasErrors()) {
        	model.addAttribute("restaurant", restaurant);
            return "reviews/register";
        }
        
        reviewService.create(restaurant, user, reviewRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");    
        
        return "redirect:/restaurants/{restaurant_id}";
    }   

    @GetMapping("/{review_id}/edit")
    public String edit(@PathVariable(name = "restaurant_id") Integer restaurant_id,
    		@PathVariable(name = "review_id") Integer review_id, Model model,
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
    	User user = userDetailsImpl.getUser();
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
    	Review review = reviewRepository.getReferenceById(review_id);

        ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getRestaurant(), review.getUser(), review.getEvaluation(), review.getContent());
        
        model.addAttribute("restaurant", restaurant); 
        model.addAttribute("review", review);
        model.addAttribute("reviewEditForm", reviewEditForm);
        
        return "reviews/edit";
    }    
    
    @PostMapping("/{review_id}/update")
    public String update(@PathVariable(name = "restaurant_id") Integer restaurant_id,
    		             @PathVariable(name = "review_id") Integer review_id,
                         @ModelAttribute @Validated ReviewEditForm reviewEditForm,
                         BindingResult bindingResult,UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes,
                         Model model) 
    {    
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
        User user = userDetailsImpl.getUser();
        Review review = reviewRepository.getReferenceById(review_id);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("review", review);
            return "reviews/edit";
        }
        
        
        reviewService.update(restaurant, user, reviewEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
        
        return "redirect:/restaurants/{restaurant_id}";
    }    
    
    
    @GetMapping("/{review_id}/delete")
    public String delete(@PathVariable(name = "review_id") Integer review_id, RedirectAttributes redirectAttributes) {     
        reviewRepository.deleteById(review_id);
                
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        
        return "redirect:/restaurants/{restaurant_id}";
    }    
    
}
