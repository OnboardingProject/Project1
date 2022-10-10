package com.catalog.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.catalog.constants.Constants;
import com.catalog.entity.Category;
import com.catalog.entity.SubCategory;
import com.catalog.exception.HierarchyNotFoundException;
import com.catalog.exception.ListEmptyException;
import com.catalog.repository.CategoryRepository;
import com.catalog.request.CategoryRequest;
import com.catalog.service.CategoryServiceImpl;

/**
 * This test class is used to test service class methods of catalog management
 *
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;
	@Mock
	private CategoryRepository categoryRepository;

	/**
	 * {@link CategoryServiceImpl#saveCatalog(CategoryRequest)} This method tests
	 * when the parent category is saved to database according to the level
	 * hierarchy
	 *
	 */
	@Test
	public void saveCategoryTest_Parent_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal tools", "admin", "admin", null);
		when(categoryRepository.save(Mockito.any())).thenReturn(category);
		Category saveCategory = categoryServiceImpl.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);
	}

	/**
	 * This method to save catalog when list is not empty
	 */
	@Test
	public void saveCategoryTest_Parent2_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		Category secondCategory = new Category("6338766a9f9d0307958e2dbb", 2, "external tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);

		CategoryRequest categoryRequest = new CategoryRequest("external tools", "admin", "admin", null);
		when(categoryRepository.findAll()).thenReturn(List.of(category));
		Category saveCategory = categoryServiceImpl.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e2dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(secondCategory, saveCategory);
	}

	/**
	 * This method to save child catalog when list is not empty
	 */
	@Test
	public void saveCategoryTest_Child_Success() {
		List<SubCategory> subCategory = new ArrayList<>();
		subCategory.add(new SubCategory(1, "internal 1", null));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1", "admin", "admin", "1");
		when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryServiceImpl.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child2_Success() {
		List<SubCategory> childSubCategory = new ArrayList<>();
		childSubCategory.add(new SubCategory(1, "internal 1.1", null));
		List<SubCategory> subCategory = new ArrayList<>();

		subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
		when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryServiceImpl.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child3_Success() {
		List<SubCategory> secondChildSubCategory = new ArrayList<>();
		secondChildSubCategory.add(new SubCategory(1, "internal 1.1.1", null));
		List<SubCategory> childSubCategory = new ArrayList<>();
		childSubCategory.add(new SubCategory(1, "internal 1.1", secondChildSubCategory));
		List<SubCategory> subCategory = new ArrayList<>();

		subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
		Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				subCategory);
		CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
		when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
		Category saveCategory = categoryServiceImpl.saveCatalog(categoryRequest);
		saveCategory.setId("6338766a9f9d0307958e3dbb");
		saveCategory.setCreatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		saveCategory.setLastUpdatedTime(LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9));
		assertEquals(category, saveCategory);

	}

	@Test
	public void saveCategoryTest_Child1_Exception() {
		CategoryRequest categoryRequest = new CategoryRequest("internal 1", "admin", "admin", "1");
		HierarchyNotFoundException ex = assertThrows(HierarchyNotFoundException.class,
				() -> categoryServiceImpl.saveCatalog(categoryRequest));
		assertEquals(Constants.HIERARCHY_EXCEPTION_MSG, ex.getErrorMessage());
	}

	@Test
	public void saveCategoryTest_Child2_Exception() {
		try {
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					null);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
			when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryServiceImpl.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child3_Exception() {
		try {
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(1, "internal 1", null));
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
			when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryServiceImpl.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child4_Exception() {
		try {
			List<SubCategory> childSubCategory = new ArrayList<>();
			childSubCategory.add(new SubCategory(1, "internal 1.1", null));
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(2, "internal 1", childSubCategory));

			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1", "admin", "admin", "1-1");
			when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryServiceImpl.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierarchyNotFoundException);
		}
	}

	@Test
	public void saveCategoryTest_Child5_Exception() {
		try {
			List<SubCategory> secondChildSubCategory = new ArrayList<>();
			secondChildSubCategory.add(new SubCategory(1, "internal 1.1.1", null));
			List<SubCategory> childSubCategory = new ArrayList<>();
			childSubCategory.add(new SubCategory(2, "internal 1.1", secondChildSubCategory));
			List<SubCategory> subCategory = new ArrayList<>();

			subCategory.add(new SubCategory(1, "internal 1", childSubCategory));
			Category category = new Category("6338766a9f9d0307958e3dbb", 1, "internal tools", "admin",
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "admin", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					subCategory);
			CategoryRequest categoryRequest = new CategoryRequest("internal 1.1.1", "admin", "admin", "1-1-1");
			when(categoryRepository.findByLevelId(Mockito.anyInt())).thenReturn(Optional.of(category));
			categoryServiceImpl.saveCatalog(categoryRequest);

		} catch (Exception e) {
			assertTrue(e instanceof HierarchyNotFoundException);
		}
	}

	@Test
	public void fetchCategoryServiceTest() {

		Category category1 = new Category("id1", 1, "internal", "u1212", LocalDateTime.now(), "u1212",
				LocalDateTime.now(), null);

		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category1);

		when(categoryRepository.findAll()).thenReturn(categoryList);
		categoryServiceImpl.fetchCategory();
	}

	@Test
	public void fetchCategoryServiceTestException() {

		try {
			Category category1 = new Category("id1", 1, "internal", "u1212", LocalDateTime.now(), "u1212",
					LocalDateTime.now(), null);
			Category category2 = new Category("id2", 2, "external", "u1212", LocalDateTime.now(), "u1212",
					LocalDateTime.now(), null);
			List<Category> categoryList = new ArrayList<Category>();
			categoryList.add(category1);
			categoryList.add(category2);
			when(categoryRepository.findAll()).thenReturn(null);

			categoryServiceImpl.fetchCategory();
		} catch (Exception e) {
			assertTrue(e instanceof ListEmptyException);
		}
	}

}