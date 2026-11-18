package com.rescuebite.listing.controller;

import com.rescuebite.listing.dto.CreateListingRequest;
import com.rescuebite.listing.dto.ListingResponse;
import com.rescuebite.listing.dto.ReduceStockRequest;
import com.rescuebite.listing.dto.UpdateListingRequest;
import com.rescuebite.listing.model.FoodCategory;
import com.rescuebite.listing.model.ListingStatus;
import com.rescuebite.listing.service.FoodListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@Tag(name = "Food Listings", description = "Endpoints for managing surplus food listings, stock quantities, and donation flags")
public class FoodListingController {

    private final FoodListingService listingService;

    public FoodListingController(FoodListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    @Operation(summary = "Create surplus food listing", description = "Creates a new food listing with price, quantity, expiry time, and collection deadline.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Listing created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid pricing")
    })
    public ResponseEntity<ListingResponse> createListing(@Valid @RequestBody CreateListingRequest request) {
        ListingResponse response = listingService.createListing(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food listing by ID", description = "Retrieves full details of a single food listing.")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @GetMapping
    @Operation(summary = "List food listings with optional filters", description = "Retrieves listings filtered by category, donationOnly flag, or status.")
    public ResponseEntity<List<ListingResponse>> getAllListings(
            @RequestParam(required = false) FoodCategory category,
            @RequestParam(required = false) Boolean donationOnly,
            @RequestParam(required = false) ListingStatus status) {
        return ResponseEntity.ok(listingService.getAllListings(category, donationOnly, status));
    }

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get listings by food provider", description = "Returns all surplus items published by a specific provider.")
    public ResponseEntity<List<ListingResponse>> getListingsByProvider(@PathVariable String providerId) {
        return ResponseEntity.ok(listingService.getListingsByProvider(providerId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update food listing", description = "Updates details of an existing food listing.")
    public ResponseEntity<ListingResponse> updateListing(
            @PathVariable String id,
            @Valid @RequestBody UpdateListingRequest request) {
        return ResponseEntity.ok(listingService.updateListing(id, request));
    }

    @PatchMapping("/{id}/reduce-stock")
    @Operation(summary = "Reduce listing stock", description = "Internal endpoint called by Order Service when placing an order to decrement quantity atomically.")
    public ResponseEntity<ListingResponse> reduceStock(
            @PathVariable String id,
            @Valid @RequestBody ReduceStockRequest request) {
        return ResponseEntity.ok(listingService.reduceStock(id, request.getQuantity()));
    }

    @PatchMapping("/{id}/restore-stock")
    @Operation(summary = "Restore listing stock", description = "Internal endpoint called when an order is cancelled to return quantity to stock.")
    public ResponseEntity<ListingResponse> restoreStock(
            @PathVariable String id,
            @Valid @RequestBody ReduceStockRequest request) {
        return ResponseEntity.ok(listingService.restoreStock(id, request.getQuantity()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete food listing", description = "Deletes a food listing.")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}

