package project.InventoryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.InventoryManagmentSystem.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
