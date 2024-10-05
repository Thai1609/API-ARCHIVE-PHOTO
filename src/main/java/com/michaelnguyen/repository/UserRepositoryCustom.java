package com.michaelnguyen.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class UserRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public int updateUserById(String sql) {
		Query query = entityManager.createNativeQuery(sql);
		return query.executeUpdate();
	}
}
