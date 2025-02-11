package com.michaelnguyen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.Wishlist;

public interface IWishlistRepository extends JpaRepository<Wishlist, Long> {
	List<Wishlist> findByUser(User user);

	void deleteByUserAndGalleryId(User user, String galleryId);
}
