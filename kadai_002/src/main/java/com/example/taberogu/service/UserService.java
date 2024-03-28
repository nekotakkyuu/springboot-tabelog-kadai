package com.example.taberogu.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taberogu.entity.Role;
import com.example.taberogu.entity.User;
import com.example.taberogu.form.SignupForm;
import com.example.taberogu.form.SubscriptionForm;
import com.example.taberogu.form.UserEditForm;
import com.example.taberogu.repository.RoleRepository;
import com.example.taberogu.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
    }    
    
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setEnabled(false);        
        
        return userRepository.save(user);
    }  
    
    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());
        
        user.setName(userEditForm.getName());
        user.setEmail(userEditForm.getEmail());      
        
        userRepository.save(user);
    }   
    
    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);  
        return user != null;
    } 
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }     
    
    // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true); 
        userRepository.save(user);
    }  
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());      
    }  
    
    @Transactional
    public void subscription(Integer userId) {
    	
        User user = userRepository.getReferenceById(userId);
        Role role = roleRepository.findByName("ROLE_SUBSCRIPTION");
        
        user.setRole(role);
        user.setEnabled(true); 
          
        userRepository.save(user);
    }   

    @Transactional
    public void cancel(SubscriptionForm subscriptionForm) {
        User user = userRepository.getReferenceById(subscriptionForm.getUser_id());
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        user.setRole(role);
        user.setEnabled(true); 
          
        userRepository.save(user);
    }
  

}
