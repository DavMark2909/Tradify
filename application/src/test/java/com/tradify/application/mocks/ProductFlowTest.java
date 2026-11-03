package com.tradify.application.mocks;

import com.tradify.application.entity.CompanyProfile;
import com.tradify.application.entity.Product;
import com.tradify.application.entity.Sector;
import com.tradify.application.entity.User;
import com.tradify.application.repository.CompanyProfileRepository;
import com.tradify.application.repository.ProductRepository;
import com.tradify.application.repository.SectorRepository;
import com.tradify.application.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductFlowTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SectorRepository sectorRepository;
    @Autowired
    CompanyProfileRepository companyProfileRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void createdProductIsReallyPersisted() throws Exception {
        Sector sector = new Sector();
        sector.setName("Coffee");
        sector = sectorRepository.save(sector);

        CompanyProfile company = new CompanyProfile();
        company.setName("Beans Ltd");
        company.setSector(sector);
        company = companyProfileRepository.save(company);

        User user = new User();
        user.setId(1L);
        user.setUsername("bob");
        user.setCompanyProfile(company);
        userRepository.save(user);

        mockMvc.perform(post("/api/product/create")
                        .with(jwt().jwt(j -> j.subject("bob").claim("userId", 1L)))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Arabica","description":"Green","price":10.50,
                                 "currency":"USD","unitOfMeasure":"kg",
                                 "availableQuantity":100,"status":"ACTIVE"}
                                """))
                .andExpect(status().isCreated());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getTitle()).isEqualTo("Arabica");
        assertThat(products.get(0).getSupplier().getId()).isEqualTo(company.getId());
    }
}