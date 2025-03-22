package project.InventoryManagmentSystem.service;



import org.springframework.web.multipart.MultipartFile;
import project.InventoryManagmentSystem.dto.ProductDTO;
import project.InventoryManagmentSystem.dto.Response;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);

    Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);

    Response getAllProducts();

    Response getProductById(Long id);

    Response deleteProduct(Long id);

    Response searchProduct(String input);
    
}