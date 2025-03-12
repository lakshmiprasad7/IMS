package project.InventoryManagmentSystem.service;


import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.SupplierDTO;

public interface SupplierService {

        Response addSupplier(SupplierDTO supplierDTO);

        Response updateSupplier(Long id, SupplierDTO supplierDTO);

        Response getAllSupplier();

        Response getSupplierById(Long id);

        Response deleteSupplier(Long id);

    }

