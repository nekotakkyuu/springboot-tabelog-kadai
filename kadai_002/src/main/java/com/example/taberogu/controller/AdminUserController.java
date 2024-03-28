package com.example.taberogu.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.User;
import com.example.taberogu.form.UserEditForm;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;  
    private final UserService userService;
    
    public AdminUserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;  
        this.userService = userService;
    }    
    
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
    		@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        Page<User> userPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByNameLikeOrEmailLike("%" + keyword + "%", "%" + keyword + "%", pageable);                   
        } else {
            userPage = userRepository.findAll(pageable);
        }        
        
        model.addAttribute("userPage", userPage);        
        model.addAttribute("keyword", keyword);                
        
        return "admin/users/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        User user = userRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        
        return "admin/users/show";
    } 
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") int id, Model model) {        
        User user = userRepository.getReferenceById(id);  
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "admin/users/edit";
    }   
    
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        if (bindingResult.hasErrors()) {
            return "admin/users/edit";
        }
        
        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/admin/users";
    }   

}
