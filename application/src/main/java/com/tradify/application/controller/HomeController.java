package com.tradify.application.controller;

import com.tradify.application.dto.HomeViewDto;
import com.tradify.application.dto.ProductDto;
import com.tradify.application.dto.SavedItemDto;
import com.tradify.application.entity.User;
import com.tradify.application.service.HomeService;
import com.tradify.application.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/home")
@AllArgsConstructor
public class HomeController {

    private final UserService userService;
    private final HomeService homeService;

    @GetMapping("/")
    public ResponseEntity<HomeViewDto> home(JwtAuthenticationToken authentication,
                                       @RequestParam(defaultValue = "20") int page,
                                       @RequestParam(defaultValue = "0") int size) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<ProductDto> trendingProducts = homeService.getTrendingProducts();
        Page<SavedItemDto> savedItemDtoPage = homeService.getSavedItems(user.getId(), page,size);
        HomeViewDto homeDto = new HomeViewDto(trendingProducts, savedItemDtoPage);
        return ResponseEntity.ok(homeDto);
    }
}
