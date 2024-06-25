package com.example.bankapi.Controller.AdminController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name="Admin Controller")//gán tên cho Swagger
@SecurityRequirement(name="bearerAuth")
public class AdminController {
    @GetMapping("/home")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String homePage(){
        return "hello admin";

    }
}
