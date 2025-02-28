package com.michaelnguyen.repository;

import com.michaelnguyen.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {

}
