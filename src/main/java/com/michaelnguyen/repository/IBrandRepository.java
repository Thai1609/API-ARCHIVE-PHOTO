package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.Brand;

public interface IBrandRepository extends JpaRepository<Brand, Long> {
}