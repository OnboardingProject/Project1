package com.catalog.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.catalog.entity.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
	Optional<Category> findByLevelId(int levelId);

}
