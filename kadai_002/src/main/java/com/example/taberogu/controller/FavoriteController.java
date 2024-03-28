package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.Favorite;
import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;
import com.example.taberogu.repository.FavoriteRepository;
import com.example.taberogu.repository.RestaurantRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final RestaurantRepository restaurantRepository;
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository, FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.restaurantRepository = restaurantRepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("favoritePage", favoritePage);
		
		return "favorites/index";
	}
	
    @PostMapping("/favorites/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        favoriteRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
        
        return "redirect:/favorites";
    }  
	

	@PostMapping("/restaurants/{id}/favorites/create")
	public String create(@PathVariable(name = "id") Integer id,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes)
	{
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		favoriteService.create(restaurant.getId(), user.getId());
		redirectAttributes.addFlashAttribute("successMessage", "お気に入り登録しました。");
		
		return "redirect:/restaurants/{id}";
	}
	
	
	@PostMapping("/restaurants/{id}/favorites/delete")
	public String delete(@PathVariable(name = "id") Integer id,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes)
	{
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();
		
		Favorite favorite = favoriteRepository.findByUserAndRestaurant(user, restaurant);
		favoriteRepository.delete(favorite);
		
		redirectAttributes.addFlashAttribute("successMessage", "お気に入り解除しました。");
		
		return "redirect:/restaurants/{id}";
	}

}
