package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.michaelnguyen.entity.Gallery;

public interface IGalleryRepository extends JpaRepository<Gallery, Integer> {
	@Query("SELECT COALESCE(MAX(id), 0) + 1 FROM Gallery")
	int newIdGallery();

}
