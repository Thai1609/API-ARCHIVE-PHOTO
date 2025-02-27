package com.michaelnguyen.dto.response;

import java.math.BigDecimal;
import java.util.Set;

import com.michaelnguyen.entity.ProductImage;
import com.michaelnguyen.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private BigDecimal discount;
	private int stock;
	private String status;
	private String category;
	private String brand;
	private String seller;
	private UserResponse user;
	private Set<ProductImage> images;
	private Set<ProductVariantResponse> variants;
	private Set<ProductReviewResponse> reviews;
	private Set<String> tags;
}
