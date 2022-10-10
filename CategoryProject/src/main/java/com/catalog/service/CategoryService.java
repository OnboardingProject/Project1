package com.catalog.service;

import java.util.List;

import com.catalog.entity.*;
import com.catalog.request.CategoryRequest;
import com.catalog.request.CategoryUpdateRequest;

/**
 * This is the service class for catalog management
 * 
 */
public interface CategoryService {
	/**
	 * service method to save categories
	 * @param catgeoryRequest
	 * @return category
	 */
	public Category saveCatalog(CategoryRequest catgeoryRequest);
	/**
	 * service method to fetch category
	 * 
	 * @return categoryList
	 */
	 public List<Category> fetchCategory();
	
	/**
	 * @param categoryUpdateRequest
	 * service method to update categories
	 * @return category
	 */
Category updateCatalog(CategoryUpdateRequest categoryUpdateRequest);
	

}
