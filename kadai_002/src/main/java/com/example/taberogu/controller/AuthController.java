package com.example.taberogu.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taberogu.entity.User;
import com.example.taberogu.entity.VerificationToken;
import com.example.taberogu.event.SignupEventPublisher;
import com.example.taberogu.form.SignupForm;
import com.example.taberogu.form.SubscriptionForm;
import com.example.taberogu.repository.UserRepository;
import com.example.taberogu.security.UserDetailsImpl;
import com.example.taberogu.service.StripeService;
import com.example.taberogu.service.UserService;
import com.example.taberogu.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
	private final UserRepository userRepository;
    private final UserService userService;  
    private final SignupEventPublisher signupEventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final StripeService stripeService;
    
    public AuthController(UserRepository userRepository, UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService, StripeService stripeService) {        
        this.userRepository = userRepository;
    	this.userService = userService; 
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.stripeService = stripeService;
    }  
    
    @GetMapping("/login")
    public String login() {        
        return "auth/login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {        
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }    
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {      
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }    
        
        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }        
        
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        
        User createdUser = userService.create(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。"); 

        return "redirect:/";
    }    
    
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        
        if (verificationToken != null) {
            User user = verificationToken.getUser();  
            userService.enableUser(user);
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);            
        } else {
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "auth/verify";         
    }   
    
    @GetMapping("/subscription")
    public String subscription(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, HttpServletRequest httpServletRequest, Model model) {        
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        SubscriptionForm subscriptionForm = new SubscriptionForm(user.getId(), user.getRole());
        
        String sessionId = stripeService.subscriptionStripeSession(user, subscriptionForm, httpServletRequest);
        
        
        model.addAttribute("subscriptionForm", subscriptionForm);
        model.addAttribute("sessionId", sessionId);
        
        
        return "auth/subscription";
    }
    
    /*@PostMapping("/subscription/update")
    public String subscription(@ModelAttribute @Validated SubscriptionForm subscriptionForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "auth/subscription";
        }
        
        userService.subscription(subscriptionForm);
        redirectAttributes.addFlashAttribute("successMessage", "有料会員登録しました。");
        
        return "redirect:/login";
    }  */
    
    @GetMapping("/cancel")
    public String cancel(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        SubscriptionForm subscriptionForm = new SubscriptionForm(user.getId(), user.getRole());
        
        model.addAttribute("subscriptionForm", subscriptionForm);
        
        return "auth/cancel";
    }
    
    @PostMapping("/cancel/update")
    public String cancel(@ModelAttribute @Validated SubscriptionForm subscriptionForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "auth/cancel";
        }
        
        userService.cancel(subscriptionForm);
        redirectAttributes.addFlashAttribute("successMessage", "有料会員解約しました。");
        
        return "redirect:/login";
    }  
    
 
}