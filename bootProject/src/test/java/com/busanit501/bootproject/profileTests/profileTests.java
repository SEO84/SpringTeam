package com.busanit501.bootproject.profileTests;

import com.busanit501.bootproject.dto.UserDTO;
import com.busanit501.bootproject.repository.UserRepository;
import com.busanit501.bootproject.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class profileTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    public void readOneTest() {
        Long userId = 1L;
//        Optional<User> result = userRepository.findById(userId);
//        User user = result.orElseThrow();
//        UserDTO userDTO = userService.getUserById(userId);

        UserDTO userDTO = userService.getUserById(userId);
        log.info("testSelectOneBoard , 하나 조회 boardDTO: " + userDTO.toString());
    }
}
