package com.example.bankapi.Controller.UserController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name="User Controller")//gán tên cho swagger
@RequestMapping("/public")
public class HomeController {
    @GetMapping("/home")
    public String home(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}",authentication.getName());
        authentication.getAuthorities().forEach(s-> log.info(s.getAuthority()));
        return "HEllO";
    }
    @GetMapping("/permit-url")
    public String permitPage(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}",authentication.getName());
        authentication.getAuthorities().forEach(s-> log.info(s.getAuthority()));
        return "HEllO THERE";
    }
}
