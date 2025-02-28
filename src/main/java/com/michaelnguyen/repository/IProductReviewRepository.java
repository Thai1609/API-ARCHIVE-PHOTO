package com.michaelnguyen.repository;

import com.michaelnguyen.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductReviewRepository extends JpaRepository<ProductReview, Long> {
}