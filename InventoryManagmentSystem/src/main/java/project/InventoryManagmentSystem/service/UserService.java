package project.InventoryManagmentSystem.service;

import project.InventoryManagmentSystem.dto.LoginRequest;
import project.InventoryManagmentSystem.dto.RegisterRequest;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.UserDTO;
import project.InventoryManagmentSystem.entity.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}

