package com.michaelnguyen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.michaelnguyen.entity.Permission;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, String> {

}
