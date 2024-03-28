package com.example.taberogu.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRegisterForm {
	@NotBlank(message = "カテゴリ名を入力してください。")
	private String name;
}
