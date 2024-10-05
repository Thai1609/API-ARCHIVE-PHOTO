package com.michaelnguyen.repository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.michaelnguyen.dto.response.PageResponse;
import com.michaelnguyen.entity.Gallery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class SearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public PageResponse<?> searchAllProductsWithSortBy(int pageNo, int pageSize, String search, String sortBy) {
		StringBuilder sqlQuery = new StringBuilder("SELECT p FROM Product p where 1=1");

		if (StringUtils.hasLength(search)) {
			sqlQuery.append(" and LOWER(p.name) LIKE LOWER(:name)");
		}
		if (StringUtils.hasLength(sortBy)) {
			Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
			Matcher matcher = pattern.matcher(sortBy);

			if (matcher.find()) {
				sqlQuery.append(String.format("order by p.%s %s", matcher.group(1), matcher.group(3)));
			}

		}

		Query selectQuery = entityManager.createQuery(sqlQuery.toString());
		selectQuery.setFirstResult(pageNo);
		selectQuery.setMaxResults(pageSize);

		if (StringUtils.hasLength(search)) {
			selectQuery.setParameter("name", String.format("%%%s%%", search));
		}

		List<Gallery> products = selectQuery.getResultList();

		/////////////

		StringBuilder sqlCountQuery = new StringBuilder("SELECT count(*) FROM Product p where 1=1");

		if (StringUtils.hasLength(search)) {
			sqlCountQuery.append(" and LOWER(p.name) LIKE LOWER(?1)");
		}

		Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());

		if (StringUtils.hasLength(search)) {
			selectCountQuery.setParameter(1, String.format("%%%s%%", search));
		}

		Long totalElements = (Long) selectCountQuery.getSingleResult();

		Page<?> page = new PageImpl<>(products, PageRequest.of(pageNo, pageSize), totalElements);

		return PageResponse.builder().pageNo(pageNo).pageSize(pageSize).totalPage(page.getTotalPages())
				.items(page.stream().toList()).build();

	}
}
