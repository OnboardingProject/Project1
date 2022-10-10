//package com.catalog.service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import com.catalog.constants.Constants;
//import com.catalog.entity.Category;
//import com.catalog.entity.SubCategory;
//import com.catalog.exception.HierarchyNotFoundException;
//import com.catalog.repository.CategoryRepository;
//import com.catalog.requestVo.CategoryRequest;
//import com.catalog.requestVo.CategoryUpdateRequest;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Service Implementation class to save and update categories
// *
// */
//@Slf4j
//@Service
//public class CategoryServiceImpl2 implements CategoryService {
//	@Autowired
//	CategoryRepository categoryRepository;
//
//	/**
//	 * save method which is used to save parent and child category
//	 */
//	public Category saveCatalog(CategoryRequest categoryRequest) {
//		log.info("Enterd into save catalog");
//		Category category = null;
//
//		// parent request
//		if (Objects.isNull(categoryRequest.getHierarchyLevel())) {
//			log.info("Call for parent request");
//			category = saveParent(categoryRequest);
//		} else {
//			log.info("Call for child request");
//			category = saveChild(categoryRequest);
//		}
//		categoryRepository.save(category);
//		log.info("Exit from save catalog");
//		return category;
//
//	}
//	@Override
//	public Category updateCatalog(CategoryUpdateRequest categoryUpdateRequest) {
//		// parent request
//		log.info("trying to update parent category by levelId" + categoryUpdateRequest.getLevelId());
//		if (categoryUpdateRequest.getHierarchyLevel() == null) {
//			Category category = categoryRepository.findByLevelId(categoryUpdateRequest.getLevelId());
//			if (Objects.nonNull(category)) {
//				category.setLevelName(categoryUpdateRequest.getLevelName());
//				category.setLastUpdatedBy(categoryUpdateRequest.getLastUpdatedBy());
//				category.setLastUpdatedTime(LocalDateTime.now());
//				categoryRepository.save(category);
//				return category;
//			}
//
//			else {
//				log.info("parent class exception occurs");
//				throw new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.NOT_FOUND);
//			}
//		} else {
//
//			SubCategory subCategory = null;
//			List<SubCategory> subCategoryRequestList = new ArrayList<>();
//			String[] l = categoryUpdateRequest.getHierarchyLevel().split("-");
//			List<Integer> levels = new ArrayList<Integer>();
//			for (String a : l) {
//				// adding levels to an integer list
//				levels.add(Integer.parseInt(a));
//			}
//			Category category = categoryRepository.findByLevelId(categoryUpdateRequest.getLevelId());
//
//			if (levels.size() == 1) {
//				subCategoryRequestList = category.getLevels();
//
//				SubCategory subcategoryLevel1 = subCategoryRequestList.stream()
//						.filter(a -> a.getLevelId() == levels.get(0)).findFirst()
//						.orElseThrow(() -> new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
//								HttpStatus.NOT_FOUND));
//				subcategoryLevel1.setLevelName(categoryUpdateRequest.getLevelName());
//			} else {
//				subCategory = category.getLevels().stream().filter(x -> x.getLevelId() == levels.get(0)).findFirst()
//						.orElseThrow(() -> new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
//								HttpStatus.NOT_FOUND));
//
//				subCategory = recursiveUpdateLevel(1, subCategory, categoryUpdateRequest, levels);
//
//			}
//			category.setLastUpdatedBy(categoryUpdateRequest.getLastUpdatedBy());
//			category.setLastUpdatedTime(LocalDateTime.now());
//			categoryRepository.save(category);
//			return category;
//		}
//	}
//
//
//	/**
//	 * @param categoryRequest method used to save child category
//	 * @return category
//	 */
//	Category saveChild(CategoryRequest categoryRequest) {
//		log.info("Enterd into child request method");
//		Category category = null;
//		SubCategory subCategory = null;
//		List<SubCategory> subCategoryRequestList = new ArrayList<>();
//		String[] levelsHierarchy = categoryRequest.getHierarchyLevel().split("-");
//		List<Integer> levels = new ArrayList<Integer>();
//		int initialArraySize = 1;
//		for (String a : levelsHierarchy) {
//			// adding levels to an integer list
//			levels.add(Integer.parseInt(a));
//		}
//
//		category = categoryRepository.findByLevelId(levels.get(0));
//		if (Objects.isNull(category))
//			throw new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST);
//		else {
//			if (levels.size() == initialArraySize) {
//				subCategoryRequestList = category.getLevels();
//				subCategoryRequestList.add(new SubCategory(getNewId(subCategoryRequestList),
//						categoryRequest.getLevelName(), new ArrayList<>()));
//				category.setLevels(subCategoryRequestList);
//				return category;
//			} else {
//				if (Objects.isNull(category.getLevels()))
//					throw new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST);
//				else {
//
//					subCategory = category.getLevels().stream().filter(x -> x.getLevelId() == levels.get(1)).findFirst()
//							.orElseThrow(() -> new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
//									HttpStatus.BAD_REQUEST));
//					subCategory = recursiveLevels(1, subCategory, categoryRequest, levels);
//				}
//			}
//		}
//
//		List<SubCategory> parentCatList = category.getLevels().stream().filter(x -> x.getLevelId() != levels.get(1))
//				.collect(Collectors.toList());
//		parentCatList.add(subCategory);
//		category.setLevels(parentCatList);
//		log.info("Exit from child request method");
//		return category;
//
//	}
//
//	List<SubCategory> catList;
//	List<SubCategory> temp = new ArrayList<>();
//	List<SubCategory> tempList = new ArrayList<>();
//	int counter = 0;
//
//	/**
//	 * @param i
//	 * @param subCategory
//	 * @param catgeoryRequest
//	 * @param levels          recursive function to traverse through the inner
//	 *                        levels and save child categories
//	 * @return subCategory
//	 */
//	SubCategory recursiveLevels(int i, SubCategory subCategory, CategoryRequest catgeoryRequest, List<Integer> levels) {
//		log.info("Enterd into recursive method for child");
//		i = i + 1;
//		if (Objects.isNull(subCategory.getLevels()))
//			throw new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG, HttpStatus.BAD_REQUEST);
//		else {
//			List<SubCategory> temp = subCategory.getLevels();
//			if (i == levels.size()) {
//				temp.add(new SubCategory(getNewId(temp), catgeoryRequest.getLevelName(), new ArrayList<>()));
//				subCategory.setLevels(temp);
//			} else {
//				int j = i;
//				int levelId = levels.get(i);
//				SubCategory iteratedVal = temp.stream().filter(t -> t.getLevelId() == levelId).findFirst()
//						.orElseThrow(() -> new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
//								HttpStatus.BAD_REQUEST));
//				iteratedVal = recursiveLevels(i, iteratedVal, catgeoryRequest, levels);
//				temp = temp.stream().filter(x -> x.getLevelId() != levels.get(j)).collect(Collectors.toList());
//				temp.add(iteratedVal);
//				subCategory.setLevels(temp);
//			}
//			log.info("Exit from recursive method for child");
//			return subCategory;
//		}
//
//	}
//
//	/**
//	 * @param categoryRequest method used to save parent category
//	 * @return category
//	 */
//	Category saveParent(CategoryRequest categoryRequest) {
//		log.info("Enterd into parent request method");
//		Category category = null;
//		List<Category> listCategory = categoryRepository.findAll();
//		category = new Category();
//		// initially if hierarchy is null
//		if (listCategory.isEmpty()) {
//			category.setLevelId(1);
//		} else {
//			int nextLevelId = listCategory.get(listCategory.size() - 1).getLevelId() + 1;
//			category.setLevelId(nextLevelId);
//		}
//		category.setLevelName(categoryRequest.getLevelName());
//		category.setCreatedBy(categoryRequest.getCreatedBy());
//		category.setCreatedTime(LocalDateTime.now());
//		category.setLastUpdatedBy(categoryRequest.getLastUpdatedBy());
//		category.setLastUpdatedTime(LocalDateTime.now());
//		category.setLevels(new ArrayList<>());
//		log.info("Exit from parent request method");
//		return category;
//	}
//
//	/**
//	 * @param temp method used to update level id based on the highest level id
//	 *             present
//	 * @return
//	 */
//	private int getNewId(List<SubCategory> temp) {
//		log.info("Generating id ");
//		int newId = 1;
//		if (temp.size() > 0) {
//			List<Integer> idList = temp.stream().map(x -> x.getLevelId()).sorted().collect(Collectors.toList());
//			newId = idList.get(idList.size() - 1) + 1;
//		}
//		return newId;
//	}
//
//	
//	SubCategory recursiveUpdateLevel(Integer i, SubCategory subCategory, CategoryUpdateRequest catgeoryUpdateRequest,
//			List<Integer> levels) {
//		System.out.println("recursive levels");
//		if (i < levels.size()) {
//			Integer levelId = levels.get(i);
//
//			SubCategory iteratedVal = subCategory.getLevels().stream().filter(t -> t.getLevelId() == levelId).findFirst()
//					.orElseThrow(() -> new HierarchyNotFoundException(Constants.HIERARCHY_EXCEPTION_MSG,
//							HttpStatus.NOT_FOUND));
//
//			iteratedVal.setLevelName(catgeoryUpdateRequest.getLevelName());
//
//			i++;
//			recursiveUpdateLevel(i, iteratedVal, catgeoryUpdateRequest, levels);
//
//		}
//		return subCategory;
//	}
//
//}