package project.InventoryManagmentSystem.service;

import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.TransactionRequest;
import project.InventoryManagmentSystem.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus status);
}
