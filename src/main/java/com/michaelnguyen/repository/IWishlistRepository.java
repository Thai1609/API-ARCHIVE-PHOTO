package com.michaelnguyen.repository;

import com.michaelnguyen.entity.User;
import com.michaelnguyen.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);

    void deleteByUserAndGalleryId(User user, String galleryId);
}
