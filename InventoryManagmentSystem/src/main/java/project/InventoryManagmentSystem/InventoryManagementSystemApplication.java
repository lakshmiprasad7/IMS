package project.InventoryManagmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "project.InventoryManagmentSystem.entity")
@EnableJpaRepositories(basePackages = "project.InventoryManagmentSystem.repository")
@SpringBootApplication
public class InventoryManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementSystemApplication.class, args);
    }
}

