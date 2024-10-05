package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.michaelnguyen.entity.UserProfile;

public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {

}
