package com.tradify.application.controller;

import com.tradify.application.dto.UserDto;
import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.User;
import com.tradify.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticateMe(JwtAuthenticationToken token){
        User byUsername = userService.findByUsernameWithCompany(token.getName());
        CompanyProfile companyProfile = byUsername.getCompanyProfile();
        String companyName = (companyProfile != null && companyProfile.getName() != null)
                ? companyProfile.getName()
                : null;
        boolean isBuyer = companyProfile != null && companyProfile.isBuyer();
        boolean isSupplier = companyProfile != null && companyProfile.isSupplier();
        boolean isLogistics = companyProfile != null && companyProfile.isLogistics();
        return ResponseEntity.ok(new UserDto(byUsername.getUsername(),
                byUsername.getName(),
                byUsername.getLastName(),
                companyName,
                isBuyer,
                isSupplier,
                isLogistics));
    }
}
