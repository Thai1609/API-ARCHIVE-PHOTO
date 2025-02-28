package com.michaelnguyen.repository;

import com.michaelnguyen.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}