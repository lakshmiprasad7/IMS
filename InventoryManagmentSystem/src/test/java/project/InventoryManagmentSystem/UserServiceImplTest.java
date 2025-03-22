package project.InventoryManagmentSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.InventoryManagmentSystem.dto.LoginRequest;
import project.InventoryManagmentSystem.dto.RegisterRequest;
import project.InventoryManagmentSystem.dto.Response;
import project.InventoryManagmentSystem.dto.UserDTO;
import project.InventoryManagmentSystem.entity.User;
import project.InventoryManagmentSystem.enums.UserRole;
import project.InventoryManagmentSystem.exceptions.InvalidCredentialsException;
import project.InventoryManagmentSystem.exceptions.NotFoundException;
import project.InventoryManagmentSystem.repository.UserRepository;
import project.InventoryManagmentSystem.security.JwtUtils;
import project.InventoryManagmentSystem.service.impl.UserServiceImpl;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    private User user;
    private UserDTO userDTO;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("john");
        user.setEmail("john@gmail.com");
        user.setPassword("john");
        user.setPhoneNumber("7893080121");
        user.setRole(UserRole.MANAGER);

        userDTO = new UserDTO();
        userDTO.setName("john");
        userDTO.setEmail("john@gmail.com");

        registerRequest = new RegisterRequest();
        registerRequest.setName("john");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("john");
        registerRequest.setPhoneNumber("7893080121");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@gmail.com");
        loginRequest.setPassword("john");
    }

    @Test
    void registerUser_ShouldReturnSuccessResponse() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Response response = userService.registerUser(registerRequest);

        assertEquals(200, response.getStatus());
        assertEquals("User was successfully registered", response.getMessage());
    }

    @Test
    void loginUser_ShouldReturnSuccessResponse() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyString())).thenReturn("token");

        Response response = userService.loginUser(loginRequest);

        assertEquals(200, response.getStatus());
        assertEquals("User Logged in Successfully", response.getMessage());
        assertNotNull(response.getToken());
    }

    @Test
    void loginUser_InvalidCredentials_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Executable executable = () -> userService.loginUser(loginRequest);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, executable);
        assertEquals("Password Does Not Match", exception.getMessage());
    }

    

    @Test
    void getCurrentLoggedInUser_ShouldReturnUser() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("john@gmail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User result = userService.getCurrentLoggedInUser();

        assertNotNull(result);
        assertEquals("john", result.getName());
    }

    @Test
    void getUserById_ShouldReturnSuccessResponse() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(), eq(UserDTO.class))).thenReturn(userDTO);

        Response response = userService.getUserById(1L);

        assertEquals(200, response.getStatus());
        assertEquals("success", response.getMessage());
    }

    @Test
    void getUserById_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Executable executable = () -> userService.getUserById(1L);

        NotFoundException exception = assertThrows(NotFoundException.class, executable);
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void updateUser_ShouldReturnSuccessResponse() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Response response = userService.updateUser(1L, userDTO);

        assertEquals(200, response.getStatus());
        assertEquals("User successfully updated", response.getMessage());
    }

    @Test
    void updateUser_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Executable executable = () -> userService.updateUser(1L, userDTO);

        NotFoundException exception = assertThrows(NotFoundException.class, executable);
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldReturnSuccessResponse() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Response response = userService.deleteUser(1L);

        assertEquals(200, response.getStatus());
        assertEquals("User successfully Deleted", response.getMessage());
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Executable executable = () -> userService.deleteUser(1L);

        NotFoundException exception = assertThrows(NotFoundException.class, executable);
        assertEquals("User Not Found", exception.getMessage());
    }

   
}
