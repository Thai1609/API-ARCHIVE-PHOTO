package com.michaelnguyen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
	private Long userId;
	private Long categoryId;
	private String nameProduct;
	private String description;
	private double price;
	private String status;
	private int stock;
	private Long brandId;

}
