package project.InventoryManagmentSystem.service;


import project.InventoryManagmentSystem.dto.CategoryDTO;
import project.InventoryManagmentSystem.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}
