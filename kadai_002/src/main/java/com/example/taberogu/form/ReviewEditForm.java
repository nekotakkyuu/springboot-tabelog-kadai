package com.example.taberogu.form;

import com.example.taberogu.entity.Restaurant;
import com.example.taberogu.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	@NotNull
	private Integer review_id;
	
	private Restaurant restaurant;
	
	private User user;
	
	@NotNull(message = "評価を選択してください。")
	private String evaluation;
	
	@NotBlank(message = "コメントを入力してください。")
	private String content;

}
