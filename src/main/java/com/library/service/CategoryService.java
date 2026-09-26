package com.library.service;

import com.library.dto.CategoryDTO;
import java.util.List;

/**
 * CategoryService Interface - Business operations for book categories
 */
public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
