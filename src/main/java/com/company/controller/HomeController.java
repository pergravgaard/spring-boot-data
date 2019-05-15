package com.company.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String home(Model model, Principal principal) {
        OAuth2User user = ((OAuth2AuthenticationToken) principal).getPrincipal();
        model.addAttribute("username", user.getAttributes().getOrDefault("name", "John Doe"));
        System.out.println("Username: " + model.asMap().get("username"));
        return "index";
    }

}
