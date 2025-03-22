package project.InventoryManagmentSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;


import project.InventoryManagmentSystem.dto.Response;

import project.InventoryManagmentSystem.dto.TransactionRequest;
import project.InventoryManagmentSystem.entity.Product;
import project.InventoryManagmentSystem.entity.Supplier;
import project.InventoryManagmentSystem.entity.Transaction;
import project.InventoryManagmentSystem.entity.User;
import project.InventoryManagmentSystem.repository.ProductRepository;
import project.InventoryManagmentSystem.repository.SupplierRepository;
import project.InventoryManagmentSystem.repository.TransactionRepository;
import project.InventoryManagmentSystem.service.UserService;
import project.InventoryManagmentSystem.service.impl.TransactionServiceImpl;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    private Product product;
    private Supplier supplier;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock a Product
        product = new Product();
        product.setId(1L);
        product.setName("Television");
        product.setPrice(BigDecimal.valueOf(180000));
        product.setStockQuantity(10);

        // Mock a Supplier
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Gaforr");

        // Mock a User
        user = new User();
        user.setId(3L);
        user.setName("John Doe");
    }

    @Test
    void testPurchase() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setProductId(1L);
        transactionRequest.setSupplierId(1L);
        transactionRequest.setQuantity(2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.of(supplier));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);

        Response response = transactionService.purchase(transactionRequest);

        assertEquals(200, response.getStatus());
        assertEquals("Purchase Made successfully", response.getMessage());
        assertEquals(12, product.getStockQuantity()); // Updated stock after purchase
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testSell() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setProductId(1L);
        transactionRequest.setQuantity(2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);

        Response response = transactionService.sell(transactionRequest);

        assertEquals(200, response.getStatus());
        assertEquals("Product Sale successfully made", response.getMessage());
        assertEquals(8, product.getStockQuantity()); // Updated stock after sale
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testReturnToSupplier() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setProductId(1L);
        transactionRequest.setSupplierId(1L);
        transactionRequest.setQuantity(2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.of(supplier));
        when(userService.getCurrentLoggedInUser()).thenReturn(user);

        Response response = transactionService.returnToSupplier(transactionRequest);

        assertEquals(200, response.getStatus());
        assertEquals("Product Returned in progress", response.getMessage());
        assertEquals(8, product.getStockQuantity()); // Updated stock after return
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // @Test
    // void testGetAllTransactions() {
    //     Transaction transaction = new Transaction();
    //     transaction.setId(1L);
    //     transaction.setProduct(product);
    //     transaction.setTotalProducts(2);
    //     transaction.setTotalPrice(BigDecimal.valueOf(360000));
    //     List<Transaction> transactionList = Arrays.asList(transaction);

    //     Page<Transaction> transactionPage = new PageImpl<>(transactionList);

    //     when(transactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(transactionPage);
    //     when(modelMapper.map(transactionList, new TypeToken<List<TransactionDTO>>() {}.getType()))
    //         .thenReturn(Arrays.asList(new TransactionDTO()));

    //     Response response = transactionService.getAllTransactions(0, 10, null);

    //     assertEquals(200, response.getStatus());
    //     assertEquals("success", response.getMessage());
    //     assertNotNull(response.getTransactions());
    //     assertEquals(1, response.getTransactions().size());
    // }

    
}
