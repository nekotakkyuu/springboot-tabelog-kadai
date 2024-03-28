package com.example.taberogu.form;

 import org.springframework.web.multipart.MultipartFile;

import com.example.taberogu.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
 
 @Data
 @AllArgsConstructor
public class RestaurantEditForm {
     @NotNull
     private Integer id;    
     
     @NotBlank(message = "店舗名を入力してください。")
     private String name;
         
     private MultipartFile imageFile;
     
     @NotBlank(message = "説明を入力してください。")
     private String description;   
     
     @NotBlank(message = "営業時間を入力してください。")
     private String openingHours;
     
     @NotBlank(message = "価格帯を入力してください。")
     private String price;        
     
     @NotBlank(message = "郵便番号を入力してください。")
     private String postalCode;
     
     @NotBlank(message = "住所を入力してください。")
     private String address;
     
     @NotBlank(message = "定休日を入力してください。")
     private String regularHoliday;
     
     @NotBlank(message = "電話番号を入力してください。")
     private String phoneNumber;
     
     @NotNull(message = "カテゴリを選択してください。")
     private Category category;
}