package com.example.taberogu.form;

import com.example.taberogu.entity.Role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionForm {
	@NotNull
	private Integer user_id;
	
	private Role role_id;

}
