package com.michaelnguyen.entity;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private BigDecimal price;

	private BigDecimal discount;

	@Column(nullable = false)
	private int stock;

	@Enumerated(EnumType.STRING)
	private ProductStatus status = ProductStatus.ACTIVE;

	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonBackReference
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	@JsonBackReference
	private Brand brand;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<ProductImage> images;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<ProductVariant> variants;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<ProductReview> reviews;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<ProductTag> tags;
}

enum ProductStatus {
	ACTIVE, INACTIVE
}
