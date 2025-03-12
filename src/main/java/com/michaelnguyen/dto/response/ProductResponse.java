package com.michaelnguyen.dto.response;

import com.michaelnguyen.entity.Category;
import com.michaelnguyen.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

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
    private Category category;
    private String brand;
    private UserResponse user;
    private Set<ProductImage> images;
    private Set<ProductVariantResponse> variants;
    private Set<ProductReviewResponse> reviews;
    private Set<String> tags;
}
