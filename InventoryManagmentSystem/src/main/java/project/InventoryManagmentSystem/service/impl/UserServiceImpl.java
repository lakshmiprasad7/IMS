package project.InventoryManagmentSystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project.InventoryManagmentSystem.dto.*;

import project.InventoryManagmentSystem.entity.User;
import project.InventoryManagmentSystem.enums.UserRole;
import project.InventoryManagmentSystem.exceptions.InvalidCredentialsException;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.UserRepository;
import project.InventoryManagmentSystem.security.JwtUtils;
import project.InventoryManagmentSystem.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        UserRole role = (registerRequest.getRole() != null) ? registerRequest.getRole() : UserRole.MANAGER;

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User was successfully registered")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password Does Not Match");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User Logged in Successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 months")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // Convert transactions to modifiable list before mapping
        users.forEach(user -> user.setTransactions(user.getTransactions() != null ? 
            user.getTransactions().stream().collect(Collectors.toList()) : null));

        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found"));

        // Convert transactions to a modifiable list before setting
        user.setTransactions(user.getTransactions() != null ? 
            user.getTransactions().stream().collect(Collectors.toList()) : null);

        return user;
    }

    @Override
    public Response getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

        // Convert transactions to a modifiable list before mapping
        user.setTransactions(user.getTransactions() != null ? 
            user.getTransactions().stream().collect(Collectors.toList()) : null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User successfully Deleted")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

        // Convert transactions to modifiable list before mapping
        user.setTransactions(user.getTransactions() != null ? 
            user.getTransactions().stream().collect(Collectors.toList()) : null);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        // Set user and supplier to null inside transactions to avoid circular mapping issues
        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }
}
