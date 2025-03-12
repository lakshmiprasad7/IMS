package project.InventoryManagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.InventoryManagmentSystem.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String name;
    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;
    
    // Declare transactions field
    private List<TransactionDTO> transactions;

    // Getter
    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    // Setter with immutable copy
    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = (transactions != null) ? List.copyOf(transactions) : null;
    }
}
