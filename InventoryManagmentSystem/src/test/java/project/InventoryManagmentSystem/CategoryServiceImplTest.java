package project.InventoryManagmentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import project.InventoryManagmentSystem.dto.CategoryDTO;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.entity.Category;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.CategoryRepository;
import project.InventoryManagmentSystem.service.impl.CategoryServiceImpl;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock category entity
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        // Mock category DTO
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Electronics");
    }

   


    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(modelMapper.map(any(), any())).thenReturn(categoryDTO);

        Response response = categoryService.getCategoryById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getCategory());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Category Not Found", exception.getMessage());
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Response response = categoryService.updateCategory(1L, categoryDTO);

        assertEquals(200, response.getStatus());
        assertEquals("Category Was Successfully Updated", response.getMessage());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Response response = categoryService.deleteCategory(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Category Was Successfully Deleted", response.getMessage());
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Category Not Found", exception.getMessage());
    }
}
