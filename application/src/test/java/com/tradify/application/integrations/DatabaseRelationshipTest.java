package com.tradify.application.integrations;

import com.tradify.application.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
public class DatabaseRelationshipTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldStoreCompanyInSectorAndLinkUser() {
        // 1. Create the Sector
        Sector sector = new Sector();
        sector.setName("Manufacturing");
        Sector savedSector = entityManager.persist(sector);

        // 2. Create the Company and assign the Sector
        CompanyProfile company = new CompanyProfile();
        company.setName("Heavy Metals LLC");
        company.setSector(savedSector);
        CompanyProfile savedCompany = entityManager.persist(company);

        // 3. Create the User and assign the Company (The Owning Side)[cite: 11]
        User user = new User();
        user.setId(88L);
        user.setUsername("worker_bob");
        user.setCompanyProfile(savedCompany);
        User savedUser = entityManager.persist(user);

        // 4. Force SQL execution to write to the H2 database
        entityManager.flush();
        entityManager.clear();

        // 5. Query the database to verify the relationships held up
        User dbUser = entityManager.find(User.class, savedUser.getId());
        CompanyProfile dbCompany = entityManager.find(CompanyProfile.class, savedCompany.getId());

        assert dbUser != null;
        assertNotNull(dbUser.getCompanyProfile());
        assertEquals("Manufacturing", dbCompany.getSector().getName());
    }

    @Test
    void shouldStoreMultipleCompaniesInSector() {
        // 1. Save the Sector
        Sector sector = new Sector();
        sector.setName("Manufacturing");
        Sector savedSector = entityManager.persist(sector);

        // 2. Save the First Company
        CompanyProfile company1 = new CompanyProfile();
        company1.setName("Heavy Metals LLC");
        company1.setSector(savedSector);
        entityManager.persist(company1);

        // 3. Save the Second Company (Fixed Variable Names)
        CompanyProfile company2 = new CompanyProfile();
        company2.setName("Super Heavy Metals LLC");
        company2.setSector(savedSector);
        entityManager.persist(company2);

        // 4. Flush and Clear to force a real DB read
        entityManager.flush();
        entityManager.clear();

        // 5. Fetch and Assert
        Sector dbSector = entityManager.find(Sector.class, savedSector.getId());
        assertEquals(2, dbSector.getCompanies().size());
    }

    @Test
    void checkSavedItems() {
        Sector sector = new Sector();
        sector.setName("Manufacturing");
        Sector savedSector = entityManager.persist(sector);

        CompanyProfile company = new CompanyProfile();
        company.setName("Heavy Metals LLC");
        company.setSector(savedSector);
        CompanyProfile savedCompany = entityManager.persist(company);

        User user = new User();
        user.setId(88L);
        user.setUsername("worker_bob");
        user.setCompanyProfile(savedCompany);
        User savedUser = entityManager.persist(user);

        Product product = new Product();
        product.setTitle("Metal");
        product.setSupplier(savedCompany);
        product.setAvailableQuantity(BigDecimal.valueOf(10));
        product.setPrice(BigDecimal.valueOf(10));
        product.setCurrency("USD");
        product.setUnitOfMeasure("kg");
        product.setStatus("Active");
        product.setSector(savedSector);
        Product savedProduct = entityManager.persist(product);

        SavedItem savedItem = new SavedItem();
        savedItem.setProduct(product);
        savedItem.setBuyer(savedUser);
        SavedItem savedSavedItem = entityManager.persist(savedItem);

        entityManager.flush();
        entityManager.clear();

        Sector dbSector = entityManager.find(Sector.class, savedSector.getId());
        SavedItem dbSavedItem = entityManager.find(SavedItem.class, savedItem.getId());

        assertEquals(1, dbSector.getCompanies().size());
        assertEquals(1, dbSector.getProducts().size());

        assertEquals(88L, dbSavedItem.getBuyer().getId());

    }

}
