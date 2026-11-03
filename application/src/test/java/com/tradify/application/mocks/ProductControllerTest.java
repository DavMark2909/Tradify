package com.tradify.application.mocks;

import com.tradify.application.controller.ProductController;
import com.tradify.application.dto.ProductUpdateDto;
import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.User;
import com.tradify.application.security.JwtConverter;
import com.tradify.application.security.ResourceServerConfig;
import com.tradify.application.service.ProductService;
import com.tradify.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;


@WebMvcTest(ProductController.class)
@Import(ResourceServerConfig.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;
    @MockitoBean
    UserService userService;
    @MockitoBean
    JwtConverter jwtConverter;   // it is needed to pull up the resource config class
    @MockitoBean
    JwtDecoder jwtDecoder;       // mocking the decoder as we dont need it to go for the actual decoder

    @Test
    void createWithoutTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/product/create")
//                need to add csrf as otherwise returns 403, as csrf filter is before authorization in spring sec
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithTokenReturns201AndCallsService() throws Exception {
        CompanyProfile company = new CompanyProfile();
        User user = new User();
        user.setCompanyProfile(company);
//        mock the desired behavior here
        when(userService.findByUsernameWithCompany("bob")).thenReturn(user);

        mockMvc.perform(post("/api/product/create")
                        .with(jwt().jwt(j -> j.subject("bob").claim("userId", 7L)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Coffee","description":"Arabica","price":10.50,
                                 "currency":"USD","unitOfMeasure":"kg",
                                 "availableQuantity":100,"status":"ACTIVE"}
                                """))
                .andExpect(status().isCreated());

//        createProduct belongs to productService. We can pass in as parameters any ProductUpdateDto
//        and particular company
        verify(productService).createProduct(any(ProductUpdateDto.class), eq(company));

    }
}
