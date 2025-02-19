package com.michaelnguyen.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private int rating;

	@Column(columnDefinition = "TEXT")
	private String comment;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
