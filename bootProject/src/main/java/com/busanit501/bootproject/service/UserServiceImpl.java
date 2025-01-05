package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserDTO;
import com.busanit501.bootproject.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getProfilePicture() == null) {
            userDTO.setProfilePicture("default.jpg");
        }
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        // User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // return modelMapper.map(user, UserDTO.class);

        Optional<User> result = userRepository.findById(userId);
        User user = result.orElseThrow();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;

//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        // 첨부 이미지를 추가한 버전으로 변경
////        BoardDTO dto = modelMapper.map(board, BoardDTO.class);
//        BoardDTO dto = entityToDto(board);
//        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        modelMapper.map(userDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? modelMapper.map(user, UserDTO.class) : null;
    }
}
