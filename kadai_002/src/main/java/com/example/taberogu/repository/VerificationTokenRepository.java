package com.example.taberogu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taberogu.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer> {
    public VerificationToken findByToken(String token);
}
