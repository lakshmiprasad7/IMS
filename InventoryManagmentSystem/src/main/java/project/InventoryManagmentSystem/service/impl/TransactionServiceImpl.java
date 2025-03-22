package project.InventoryManagmentSystem.service.impl;
 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
 
import jakarta.transaction.Transactional;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.TransactionDTO;
import project.InventoryManagmentSystem.dto.TransactionRequest;
import project.InventoryManagmentSystem.entity.Product;
import project.InventoryManagmentSystem.entity.Supplier;
import project.InventoryManagmentSystem.entity.Transaction;
import project.InventoryManagmentSystem.entity.User;
import project.InventoryManagmentSystem.enums.TransactionStatus;
import project.InventoryManagmentSystem.enums.TransactionType;
import project.InventoryManagmentSystem.exceptions.NameValueRequiredException;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.ProductRepository;
import project.InventoryManagmentSystem.repository.SupplierRepository;
import project.InventoryManagmentSystem.repository.TransactionRepository;
import project.InventoryManagmentSystem.service.TransactionService;
import project.InventoryManagmentSystem.service.UserService;
import project.InventoryManagmentSystem.specification.TransactionFilter;
 
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

 
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Response purchase(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase Made successfully")
                .build();

    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);


        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product Sale successfully made")
                .build();


    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        @SuppressWarnings("unused")
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);


        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Product Returned in progress")
                .build();

    }

    @Override
    @Transactional
    public Response getAllTransactions(int page, int size, String filter) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        //user the Transaction specification
        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();

    }

    @Override
    @Transactional
    public Response getAllTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.getUser().setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDTO)
                .build();
    }

// @Override
//     @Transactional
//     public Response getAllTransactionById(Long id) {
//         Transaction transaction = transactionRepository.findById(id)
//                 .orElseThrow(() -> new NotFoundException("Transaction Not Found"));
 
//         // Force Hibernate to load lazy fields
//         Hibernate.initialize(transaction.getUser());
//         Hibernate.initialize(transaction.getProduct());
//         Hibernate.initialize(transaction.getSupplier());
 
//         TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
 
//         return Response.builder()
//                 .status(200)
//                 .message("success")
//                 .transaction(transactionDTO)
//                 .build();
//     }
 
//     @Override
//     @Transactional
//     public Response getAllTransactions(int page, int size, String filter) {
//         Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
//         Specification<Transaction> spec = TransactionFilter.byFilter(filter);
//         Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);
 
//         List<TransactionDTO> transactionDTOS = transactionPage.getContent().stream()
//      //  TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
//                 .map(transaction -> {
//                     Hibernate.initialize(transaction.getUser());
//                     Hibernate.initialize(transaction.getProduct());
//                     Hibernate.initialize(transaction.getSupplier());
//                     return modelMapper.map(transaction, TransactionDTO.class);
//                 })
//                 .collect(Collectors.toList());
 
//         return Response.builder()
//                 .status(200)
//                 .message("success")
//                 .transactions(transactionDTOS)
//                 .totalElements(transactionPage.getTotalElements())
//                 .totalPages(transactionPage.getTotalPages())
//                 .build();
//     }
 

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDTO>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus status) {

        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        existingTransaction.setStatus(status);
        existingTransaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();


    }


}
