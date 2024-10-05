package com.michaelnguyen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.VerificationToken;

public interface IVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	Optional<VerificationToken> findByToken(String token);

}