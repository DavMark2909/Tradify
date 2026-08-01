package com.tradify.authorization.controller;

import com.tradify.authorization.dto.UserRegistrationDto;
import com.tradify.authorization.security.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String saveUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) throws Exception {
        customUserDetailsService.registerUser(userRegistrationDto).orElseThrow(() -> new Exception("User registration failed"));
        return "redirect:/login?registered=true";
    }

}
