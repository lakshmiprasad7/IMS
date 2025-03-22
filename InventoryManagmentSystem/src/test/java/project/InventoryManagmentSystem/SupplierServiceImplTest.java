package project.InventoryManagmentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.SupplierDTO;
import project.InventoryManagmentSystem.entity.Supplier;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.SupplierRepository;
import project.InventoryManagmentSystem.service.impl.SupplierServiceImpl;

import org.modelmapper.TypeToken;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ModelMapper modelMapper;

    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Supplier entity
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Gaforr");
        supplier.setContactInfo("9346567412");
        supplier.setAddress("Marathahalli bangolre");

        // Mock SupplierDTO
        supplierDTO = new SupplierDTO();
        supplierDTO.setName("Gaforr");
        supplierDTO.setContactInfo("9346567412");
        supplierDTO.setAddress("Marathahalli bangolre");
    }

    @Test
    void testAddSupplier() {
        when(modelMapper.map(any(SupplierDTO.class), eq(Supplier.class))).thenReturn(supplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Response response = supplierService.addSupplier(supplierDTO);

        assertEquals(200, response.getStatus());
        assertEquals("Supplier Saved Successfully", response.getMessage());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplier() {
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierDTO updatedSupplierDTO = new SupplierDTO();
        updatedSupplierDTO.setName("Updated Gaforr");
        updatedSupplierDTO.setContactInfo("1234567890");
        updatedSupplierDTO.setAddress("New Address");

        Response response = supplierService.updateSupplier(1L, updatedSupplierDTO);

        assertEquals(200, response.getStatus());
        assertEquals("Supplier Was Successfully Updated", response.getMessage());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testGetAllSuppliers() {
        List<Supplier> mockSuppliers = Arrays.asList(supplier);

        when(supplierRepository.findAll(any(Sort.class))).thenReturn(mockSuppliers);
        when(modelMapper.map(mockSuppliers, new TypeToken<List<SupplierDTO>>() {}.getType()))
            .thenReturn(Arrays.asList(supplierDTO));

        Response response = supplierService.getAllSupplier();

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getSuppliers());
        assertEquals(1, response.getSuppliers().size());
    }

    @Test
    void testGetSupplierById_Success() {
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.of(supplier));
        when(modelMapper.map(any(Supplier.class), eq(SupplierDTO.class))).thenReturn(supplierDTO);

        Response response = supplierService.getSupplierById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
        assertNotNull(response.getSupplier());
    }

    @Test
    void testDeleteSupplier_Success() {
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.of(supplier));

        Response response = supplierService.deleteSupplier(1L);

        assertEquals(200, response.getStatus());
        assertEquals("Supplier Was Successfully Deleted", response.getMessage());
        verify(supplierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSupplier_NotFound() {
        when(supplierRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            supplierService.deleteSupplier(1L);
        });

        assertEquals("Supplier Not Found", exception.getMessage());
    }
}
