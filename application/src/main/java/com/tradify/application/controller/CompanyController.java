package com.tradify.application.controller;

import com.tradify.application.dto.CompanyProfileDto;
import com.tradify.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity createCompany(@RequestBody CompanyProfileDto dto, JwtAuthenticationToken token){
        String username = token.getName();
        companyService.save(dto, username);
    }
}
