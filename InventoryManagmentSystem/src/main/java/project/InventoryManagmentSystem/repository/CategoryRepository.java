package project.InventoryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.InventoryManagmentSystem.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}