package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.ProductReview;

public interface IProductReviewRepository extends JpaRepository<ProductReview, Long> {
}