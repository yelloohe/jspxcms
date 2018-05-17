package com.jspxcms.common.orm;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * JPA Repository工厂
 * 
 * @author liufang
 * 
 */
public class MyJpaRepositoryFactory extends JpaRepositoryFactory {

	public MyJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
			return QueryDslJpaRepository.class;
		} else {
			return MySimpleJpaRepository.class;
		}
	}

	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
		return QUERY_DSL_PRESENT
				&& MyQueryDslPredicateExecutor.class
						.isAssignableFrom(repositoryInterface);
	}
}
