package com.library.rest;

import com.library.dto.ApiResponse;
import com.library.dto.CategoryDTO;
import com.library.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CategoryRestController - REST API for Category operations.
 * Reads: any authenticated user. Writes: librarian only.
 */
@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Category created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}
