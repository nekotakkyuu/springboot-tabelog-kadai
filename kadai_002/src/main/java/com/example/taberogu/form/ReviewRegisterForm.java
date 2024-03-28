package com.example.taberogu.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {
	private Integer restaurant_id;
	
	private Integer user_id;
	
	@NotNull(message = "評価を選択してください。")
	private String evaluation;
	
	@NotBlank(message = "コメントを入力してください。")
	private String content;
}
