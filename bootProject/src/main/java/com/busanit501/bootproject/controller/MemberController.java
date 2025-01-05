package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.dto.UserDTO;
import com.busanit501.bootproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private UserService userService;


    // 시큐리티 설정에서 설정한 로그인 페이지로 이동함.
    @GetMapping("/login")
    public void loginGet(String error, String logout,
                         RedirectAttributes redirectAttributes) {
        log.info("loginGet===================");
        log.info("logoutTest ===================" + logout);

        if (logout != null) {
            log.info("logoutTest user ====================");
        }
        if (error != null) {
            log.info("logoutTest error ====================" + error);
            redirectAttributes.addFlashAttribute(
                    "error", "");
        }

    }


    // 회원가입 페이지
    @GetMapping("/signup")
    public void signup() {}
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@ModelAttribute UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/member/login"))
                .build();
        //리다이렉트로 데이터 탑재후 login페이지로 이동시키면 됌
    }

    // 이메일 중복 확인
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }


}
