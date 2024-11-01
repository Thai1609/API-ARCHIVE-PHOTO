package com.michaelnguyen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.michaelnguyen.entity.Gallery;

public interface IGalleryRepository extends JpaRepository<Gallery, Long> {
	@Query("SELECT COALESCE(MAX(id), 0) + 1 FROM Gallery")
	Long newIdGallery();

	@Query("SELECT t FROM Gallery t where t.status='public'")
	Page<Gallery> findAll(Pageable pageable);

	@Query("SELECT t FROM Gallery t where t.user.id=?1")
	Page<Gallery> findAllGalleryById(Long id, Pageable pageable);

 

	 
}
