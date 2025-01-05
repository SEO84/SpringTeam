package com.busanit501.bootproject.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 1)화면 제공 2) 데이터 제공
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    @GetMapping("/home")
    public void home(@AuthenticationPrincipal UserDetails user, Model model) {



        model.addAttribute("user", user);

    }
}


