package com.rescuebite.provider;

import com.rescuebite.provider.dto.CreateProviderRequest;
import com.rescuebite.provider.dto.VerifyProviderRequest;
import com.rescuebite.provider.model.BusinessType;
import com.rescuebite.provider.model.FoodProvider;
import com.rescuebite.provider.model.VerificationStatus;
import com.rescuebite.provider.repository.FoodProviderRepository;
import com.rescuebite.provider.service.FoodProviderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FoodProviderServiceTests {

    @Autowired
    private FoodProviderService providerService;

    @MockBean
    private FoodProviderRepository providerRepository;

    @Test
    @DisplayName("Should create provider profile with PENDING verification status")
    void testCreateProvider() {
        when(providerRepository.existsByUserId("usr-hasna-001")).thenReturn(false);
        when(providerRepository.existsByRegistrationNumber("REG-HASNA-01")).thenReturn(false);

        FoodProvider saved = new FoodProvider(
                "usr-hasna-001", "Hasna Bakery", BusinessType.BAKERY,
                "REG-HASNA-01", "hasna@bakehouse.test", "+94771234567",
                "25 Flower Rd", "Colombo", "08:00 - 20:00"
        );
        saved.setId("prv-hasna-001");

        when(providerRepository.save(any(FoodProvider.class))).thenReturn(saved);

        CreateProviderRequest req = new CreateProviderRequest();
        req.setUserId("usr-hasna-001");
        req.setBusinessName("Hasna Bakery");
        req.setBusinessType(BusinessType.BAKERY);
        req.setRegistrationNumber("REG-HASNA-01");
        req.setContactEmail("hasna@bakehouse.test");
        req.setContactPhone("+94771234567");
        req.setAddress("25 Flower Rd");
        req.setCity("Colombo");
        req.setOperatingHours("08:00 - 20:00");

        var response = providerService.createProvider(req);

        assertNotNull(response);
        assertEquals("Hasna Bakery", response.getBusinessName());
        assertEquals(VerificationStatus.PENDING, response.getVerificationStatus());
    }

    @Test
    @DisplayName("Should verify provider and update status to VERIFIED")
    void testVerifyProvider() {
        FoodProvider provider = new FoodProvider(
                "usr-hasna-001", "Hasna Bakery", BusinessType.BAKERY,
                "REG-HASNA-01", "hasna@bakehouse.test", "+94771234567",
                "25 Flower Rd", "Colombo", "08:00 - 20:00"
        );
        provider.setId("prv-hasna-001");

        when(providerRepository.findById("prv-hasna-001")).thenReturn(Optional.of(provider));
        when(providerRepository.save(any(FoodProvider.class))).thenAnswer(i -> i.getArgument(0));

        VerifyProviderRequest req = new VerifyProviderRequest();
        req.setStatus(VerificationStatus.VERIFIED);

        var response = providerService.verifyProvider("prv-hasna-001", req);

        assertEquals(VerificationStatus.VERIFIED, response.getVerificationStatus());
    }
}
