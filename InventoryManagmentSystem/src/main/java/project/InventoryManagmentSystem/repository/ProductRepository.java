package project.InventoryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.InventoryManagmentSystem.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
     
}