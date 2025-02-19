package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.ProductTag;

public interface IProductTagRepository extends JpaRepository<ProductTag, Long> {
}
