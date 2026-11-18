package com.rescuebite.listing.service;

import com.rescuebite.listing.dto.CreateListingRequest;
import com.rescuebite.listing.dto.ListingResponse;
import com.rescuebite.listing.dto.UpdateListingRequest;
import com.rescuebite.listing.exception.BadRequestException;
import com.rescuebite.listing.exception.InsufficientStockException;
import com.rescuebite.listing.exception.ResourceNotFoundException;
import com.rescuebite.listing.model.FoodCategory;
import com.rescuebite.listing.model.FoodListing;
import com.rescuebite.listing.model.ListingStatus;
import com.rescuebite.listing.repository.FoodListingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodListingService {

    private final FoodListingRepository listingRepository;
    private final RestClient restClient;

    @Value("${services.provider-service.url:http://localhost:8082}")
    private String providerServiceUrl;

    public FoodListingService(FoodListingRepository listingRepository, RestClient restClient) {
        this.listingRepository = listingRepository;
        this.restClient = restClient;
    }

    public ListingResponse createListing(CreateListingRequest request) {
        // Validation for donation vs sale pricing
        double discountedPrice = 0.0;
        if (!request.isDonation()) {
            if (request.getDiscountedPrice() == null) {
                throw new BadRequestException("Discounted price is required for paid surplus listings");
            }
            if (request.getDiscountedPrice() > request.getOriginalPrice()) {
                throw new BadRequestException("Discounted price cannot be higher than original price");
            }
            discountedPrice = request.getDiscountedPrice();
        }

        // Optional inter-service provider verification check
        verifyProviderEligibility(request.getProviderId());

        FoodListing listing = new FoodListing(
                request.getProviderId(),
                request.getProviderName(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getQuantity(),
                request.getUnit(),
                request.getOriginalPrice(),
                discountedPrice,
                request.isDonation(),
                request.getExpiryDate(),
                request.getCollectionDeadline(),
                request.getPickupAddress()
        );

        FoodListing saved = listingRepository.save(listing);
        return new ListingResponse(saved);
    }

    public ListingResponse getListingById(String id) {
        FoodListing listing = findListingById(id);
        return new ListingResponse(listing);
    }

    public List<ListingResponse> getAllListings(FoodCategory category, Boolean donationOnly, ListingStatus status) {
        List<FoodListing> listings;

        if (donationOnly != null && donationOnly) {
            listings = listingRepository.findByDonation(true);
        } else if (category != null && status != null) {
            listings = listingRepository.findByCategoryAndStatus(category, status);
        } else if (category != null) {
            listings = listingRepository.findByCategory(category);
        } else if (status != null) {
            listings = listingRepository.findByStatus(status);
        } else {
            listings = listingRepository.findAll();
        }

        return listings.stream()
                .map(ListingResponse::new)
                .collect(Collectors.toList());
    }

    public List<ListingResponse> getListingsByProvider(String providerId) {
        return listingRepository.findByProviderId(providerId).stream()
                .map(ListingResponse::new)
                .collect(Collectors.toList());
    }

    public ListingResponse updateListing(String id, UpdateListingRequest request) {
        FoodListing listing = findListingById(id);

        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setCategory(request.getCategory());
        listing.setQuantity(request.getQuantity());
        if (request.getUnit() != null) listing.setUnit(request.getUnit());
        if (request.getOriginalPrice() != null) listing.setOriginalPrice(request.getOriginalPrice());
        if (request.getDiscountedPrice() != null) listing.setDiscountedPrice(request.getDiscountedPrice());
        listing.setDonation(request.isDonation());
        if (request.getStatus() != null) listing.setStatus(request.getStatus());
        if (request.getExpiryDate() != null) listing.setExpiryDate(request.getExpiryDate());
        if (request.getCollectionDeadline() != null) listing.setCollectionDeadline(request.getCollectionDeadline());
        if (request.getPickupAddress() != null) listing.setPickupAddress(request.getPickupAddress());
        listing.setUpdatedAt(LocalDateTime.now());

        if (listing.getQuantity() == 0 && listing.getStatus() == ListingStatus.AVAILABLE) {
            listing.setStatus(ListingStatus.OUT_OF_STOCK);
        } else if (listing.getQuantity() > 0 && listing.getStatus() == ListingStatus.OUT_OF_STOCK) {
            listing.setStatus(ListingStatus.AVAILABLE);
        }

        FoodListing updated = listingRepository.save(listing);
        return new ListingResponse(updated);
    }

    public synchronized ListingResponse reduceStock(String id, int quantityToReduce) {
        FoodListing listing = findListingById(id);

        if (listing.getStatus() != ListingStatus.AVAILABLE) {
            throw new InsufficientStockException("Listing is not available for purchase (Status: " + listing.getStatus() + ")");
        }

        if (listing.getQuantity() < quantityToReduce) {
            throw new InsufficientStockException("Requested quantity (" + quantityToReduce + ") exceeds available stock (" + listing.getQuantity() + ")");
        }

        int remainingStock = listing.getQuantity() - quantityToReduce;
        listing.setQuantity(remainingStock);
        if (remainingStock == 0) {
            listing.setStatus(ListingStatus.OUT_OF_STOCK);
        }
        listing.setUpdatedAt(LocalDateTime.now());

        FoodListing updated = listingRepository.save(listing);
        return new ListingResponse(updated);
    }

    public synchronized ListingResponse restoreStock(String id, int quantityToRestore) {
        FoodListing listing = findListingById(id);
        listing.setQuantity(listing.getQuantity() + quantityToRestore);
        if (listing.getStatus() == ListingStatus.OUT_OF_STOCK) {
            listing.setStatus(ListingStatus.AVAILABLE);
        }
        listing.setUpdatedAt(LocalDateTime.now());
        FoodListing updated = listingRepository.save(listing);
        return new ListingResponse(updated);
    }

    public void deleteListing(String id) {
        FoodListing listing = findListingById(id);
        listingRepository.delete(listing);
    }

    private FoodListing findListingById(String id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food listing not found with ID: " + id));
    }

    private void verifyProviderEligibility(String providerId) {
        try {
            Boolean isVerified = restClient.get()
                    .uri(providerServiceUrl + "/api/providers/" + providerId + "/is-verified")
                    .retrieve()
                    .body(Boolean.class);

            if (isVerified != null && !isVerified) {
                // Warning note in log - allow or require verification per business rule
                System.out.println("Notice: Provider " + providerId + " is currently unverified.");
            }
        } catch (Exception e) {
            // Soft-failure fallback: Provider service offline or not reached, allow listing to keep services autonomous
            System.out.println("Provider-service unreachable; proceeding autonomously: " + e.getMessage());
        }
    }
}

