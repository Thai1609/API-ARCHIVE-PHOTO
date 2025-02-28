package com.michaelnguyen.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateUserById(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }
}
