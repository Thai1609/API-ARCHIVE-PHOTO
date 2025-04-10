package com.michaelnguyen.repository;

import com.michaelnguyen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email AND " +
            "(u.provider = :provider OR (:provider IS NULL AND u.provider IS NULL)) AND " +
            "(u.providerId = :providerId OR (:providerId IS NULL AND u.providerId IS NULL))")
    Optional<User> findByOptions(@Param("email") String email, @Param("provider") String provider,
                                 @Param("providerId") String providerId);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled = true where u.id = :id")
    void updateEnabled(@Param(value = "id") long id);

}
