package com.library.mapper;

import com.library.dto.CategoryDTO;
import com.library.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryMapper - Converts between Category entity and CategoryDTO
 */
@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setCategoryId(dto.getCategoryId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public List<CategoryDTO> toDTOList(List<Category> categories) {
        List<CategoryDTO> dtos = new ArrayList<>();
        for (Category category : categories) {
            dtos.add(toDTO(category));
        }
        return dtos;
    }
}
