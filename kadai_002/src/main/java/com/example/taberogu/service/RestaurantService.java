 package com.example.taberogu.service;
 
 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.form.RestaurantEditForm;
import com.example.taberogu.form.RestaurantRegisterForm;
import com.example.taberogu.repository.RestaurantRepository;
 
 @Service
 public class RestaurantService {
     private final RestaurantRepository restaurantRepository; 
     
     public RestaurantService(RestaurantRepository restaurantRepository) {
         this.restaurantRepository = restaurantRepository; 
     }    
     
     @Transactional
     public void create(RestaurantRegisterForm restaurantRegisterForm) {
         Restaurant restaurant = new Restaurant();        
         MultipartFile imageFile = restaurantRegisterForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             restaurant.setImageName(hashedImageName);
         }
         
         restaurant.setName(restaurantRegisterForm.getName());                
         restaurant.setDescription(restaurantRegisterForm.getDescription());
         restaurant.setOpeningHours(restaurantRegisterForm.getOpeningHours());
         restaurant.setPrice(restaurantRegisterForm.getPrice());
         restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
         restaurant.setAddress(restaurantRegisterForm.getAddress());
         restaurant.setRegularHoliday(restaurantRegisterForm.getRegularHoliday());
         restaurant.setPhoneNumber(restaurantRegisterForm.getPhoneNumber());
         restaurant.setCategory(restaurantRegisterForm.getCategory());
                     
         restaurantRepository.save(restaurant);
     }  
     
     @Transactional
     public void update(RestaurantEditForm restaurantEditForm) {
         Restaurant restaurant = restaurantRepository.getReferenceById(restaurantEditForm.getId());
         MultipartFile imageFile = restaurantEditForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             restaurant.setImageName(hashedImageName);
         }
         
         restaurant.setName(restaurantEditForm.getName());                
         restaurant.setDescription(restaurantEditForm.getDescription());
         restaurant.setOpeningHours(restaurantEditForm.getOpeningHours());
         restaurant.setPrice(restaurantEditForm.getPrice());
         restaurant.setPostalCode(restaurantEditForm.getPostalCode());
         restaurant.setAddress(restaurantEditForm.getAddress());
         restaurant.setRegularHoliday(restaurantEditForm.getRegularHoliday());
         restaurant.setPhoneNumber(restaurantEditForm.getPhoneNumber());
         restaurant.setCategory(restaurantEditForm.getCategory());
                     
         restaurantRepository.save(restaurant);
     } 
     
     // UUIDを使って生成したファイル名を返す
     public String generateNewFileName(String fileName) {
         String[] fileNames = fileName.split("\\.");                
         for (int i = 0; i < fileNames.length - 1; i++) {
             fileNames[i] = UUID.randomUUID().toString();            
         }
         String hashedFileName = String.join(".", fileNames);
         return hashedFileName;
     }     
     
     // 画像ファイルを指定したファイルにコピーする
     public void copyImageFile(MultipartFile imageFile, Path filePath) {           
         try {
             Files.copy(imageFile.getInputStream(), filePath);
         } catch (IOException e) {
             e.printStackTrace();
         }          
     } 
 }