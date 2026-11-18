package com.rescuebite.listing;

import com.rescuebite.listing.dto.CreateListingRequest;
import com.rescuebite.listing.model.FoodCategory;
import com.rescuebite.listing.model.FoodListing;
import com.rescuebite.listing.model.ListingStatus;
import com.rescuebite.listing.repository.FoodListingRepository;
import com.rescuebite.listing.service.FoodListingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FoodListingServiceTests {

    @Autowired
    private FoodListingService listingService;

    @MockBean
    private FoodListingRepository listingRepository;

    @Test
    @DisplayName("Should reduce stock and transition to OUT_OF_STOCK when remaining is zero")
    void testReduceStockToZero() {
        FoodListing listing = new FoodListing(
                "p-1", "BakeHouse", "Croissants", "Fresh",
                FoodCategory.BAKED_GOODS, 2, "boxes", 1000.0, 500.0, false,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(5), "Colombo"
        );
        listing.setId("lst-001");

        when(listingRepository.findById("lst-001")).thenReturn(Optional.of(listing));
        when(listingRepository.save(any(FoodListing.class))).thenAnswer(i -> i.getArgument(0));

        var response = listingService.reduceStock("lst-001", 2);

        assertEquals(0, response.getQuantity());
        assertEquals(ListingStatus.OUT_OF_STOCK, response.getStatus());
    }
}

