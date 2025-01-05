package com.busanit501.bootproject.service;

import com.busanit501.bootproject.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    boolean checkEmailExists(String email);
    public UserDTO getUserByEmail(String email);


}
