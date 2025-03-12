package project.InventoryManagmentSystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.TransactionRequest;
import project.InventoryManagmentSystem.enums.TransactionStatus;
import project.InventoryManagmentSystem.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> purchaseInventory(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));
    }

    @PostMapping("/sell")
    public ResponseEntity<Response> makeSale(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
public ResponseEntity<Response> getAllTransactions(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size,
        @RequestParam(required = false, defaultValue = "") String filter) {

    int maxSize = Math.min(size, 500); // Limit max size to 500
    logger.info("Fetching transactions - Page: {}, Size: {}, Filter: {}", page, maxSize, filter.isEmpty() ? "No filter applied" : filter);

    return ResponseEntity.ok(transactionService.getAllTransactions(page, maxSize, filter));
}



@GetMapping("/{id}")
public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
    return ResponseEntity.ok(transactionService.getAllTransactionById(id));
}



    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getTransactionByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Response> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody TransactionStatus status) {

        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, status));
    }


}

