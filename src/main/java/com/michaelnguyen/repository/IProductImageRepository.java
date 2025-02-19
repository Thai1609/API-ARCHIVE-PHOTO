package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.ProductImage;

public interface IProductImageRepository extends JpaRepository<ProductImage, Long> {}

