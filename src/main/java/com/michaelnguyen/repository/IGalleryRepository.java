package com.michaelnguyen.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.michaelnguyen.entity.Gallery;

public interface IGalleryRepository extends JpaRepository<Gallery, Long> {
	@Query("SELECT COALESCE(MAX(id), 0) + 1 FROM Gallery")
	Long newIdGallery();

	@Query("SELECT t FROM Gallery t where t.status='public'")
	Page<Gallery> findAllGalleries(Pageable pageable);

	@Query("SELECT t FROM Gallery t where t.user.id=?1")
	Page<Gallery> findAllGalleriesByUser(Long id, Pageable pageable);

	@Query("SELECT t FROM Gallery t where t.tag.name=?1 and (t.user.id = ?2 or t.status = 'public')")
	List<Gallery> getGalleryByTag(String nameTag, Long idUser);
}
