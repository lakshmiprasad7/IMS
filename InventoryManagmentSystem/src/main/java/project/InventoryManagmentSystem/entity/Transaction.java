package project.InventoryManagmentSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.InventoryManagmentSystem.enums.TransactionStatus;
import project.InventoryManagmentSystem.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Data
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalProducts;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // purchase, sale, return

    @Enumerated(EnumType.STRING) // Store Enum as String in DB
    
    private TransactionStatus status; //pending, completed, processing

    private String description;
    private String note;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore 
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore 
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonIgnore 
    private Supplier supplier;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", totalProducts=" + totalProducts +
                ", totalPrice=" + totalPrice +
                ", transactionType=" + transactionType +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                '}';
    }
}

