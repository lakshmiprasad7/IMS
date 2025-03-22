package project.InventoryManagmentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import project.InventoryManagmentSystem.dto.ProductDTO;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.entity.Category;
import project.InventoryManagmentSystem.entity.Product;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.CategoryRepository;
import project.InventoryManagmentSystem.repository.ProductRepository;
import project.InventoryManagmentSystem.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MultipartFile imageFile;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock a Category entity
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        // Mock a Product entity
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setSku("E1");
        product.setPrice(BigDecimal.valueOf(180000));
        product.setStockQuantity(10);
        product.setDescription("This is a 22 inch TV with LED screen");
        product.setCategory(category);

        // Mock a ProductDTO
        productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setName("Laptop");
        productDTO.setSku("E1");
        productDTO.setPrice(BigDecimal.valueOf(180000));
        productDTO.setStockQuantity(10);
        productDTO.setDescription("This is a 22 inch TV with LED screen");
        productDTO.setCategoryId(1L);
    }

    @Test
    void testSaveProduct() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Response response = productService.saveProduct(productDTO, null);

        assertEquals(200, response.getStatus());
        assertEquals("Product successfully saved", response.getMessage());
        verify(productRepository, times(1)).save(any(Product.class));
    }

  

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        List<ProductDTO> productDTOs = Arrays.asList(productDTO);

        when(productRepository.findAll(any(Sort.class))).thenReturn(products);
        when(modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType())).thenReturn(productDTOs);

        Response response = productService.getAllProducts();

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getProducts());
        assertEquals(1, response.getProducts().size());
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(modelMapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        Response response = productService.getProductById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getProduct());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Product Not Found", exception.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Response response = productService.deleteProduct(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Product Deleted successfully", response.getMessage());
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product Not Found", exception.getMessage());
    }
}
