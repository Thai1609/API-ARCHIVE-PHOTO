package com.michaelnguyen.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class VerificationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	private LocalDateTime expiryDate;

	public VerificationToken(String token, User user, LocalDateTime expiryDate) {
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(this.expiryDate);
	}

 
}
