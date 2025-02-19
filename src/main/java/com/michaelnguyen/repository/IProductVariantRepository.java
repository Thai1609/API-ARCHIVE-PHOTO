package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.ProductVariant;

public interface IProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}