package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.michaelnguyen.entity.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, String> {
	Role findByName(String name);

}
