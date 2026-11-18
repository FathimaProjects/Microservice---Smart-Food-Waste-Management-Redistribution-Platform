package com.rescuebite.provider.controller;

import com.rescuebite.provider.dto.CreateProviderRequest;
import com.rescuebite.provider.dto.ProviderResponse;
import com.rescuebite.provider.dto.UpdateProviderRequest;
import com.rescuebite.provider.dto.VerifyProviderRequest;
import com.rescuebite.provider.model.BusinessType;
import com.rescuebite.provider.model.VerificationStatus;
import com.rescuebite.provider.service.FoodProviderService;
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
@RequestMapping("/api/providers")
@Tag(name = "Food Provider Management", description = "Endpoints for managing food provider profiles, business details, and verification status")
public class FoodProviderController {

    private final FoodProviderService providerService;

    public FoodProviderController(FoodProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    @Operation(summary = "Register a food provider business profile", description = "Creates a new business profile linked to a user account.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Provider registered successfully"),
            @ApiResponse(responseCode = "400", description = "User ID already has a provider profile or invalid data")
    })
    public ResponseEntity<ProviderResponse> createProvider(@Valid @RequestBody CreateProviderRequest request) {
        ProviderResponse response = providerService.createProvider(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get provider by ID", description = "Retrieves provider details by provider ID.")
    public ResponseEntity<ProviderResponse> getProviderById(@PathVariable String id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get provider by User ID", description = "Retrieves provider profile associated with a specific user.")
    public ResponseEntity<ProviderResponse> getProviderByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(providerService.getProviderByUserId(userId));
    }

    @GetMapping
    @Operation(summary = "List all providers", description = "Returns all providers with optional filters for verification status, city, or business type.")
    public ResponseEntity<List<ProviderResponse>> getAllProviders(
            @RequestParam(required = false) VerificationStatus status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BusinessType businessType) {
        return ResponseEntity.ok(providerService.getAllProviders(status, city, businessType));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update provider profile", description = "Updates business details, contact info, and operating hours.")
    public ResponseEntity<ProviderResponse> updateProvider(
            @PathVariable String id,
            @Valid @RequestBody UpdateProviderRequest request) {
        return ResponseEntity.ok(providerService.updateProvider(id, request));
    }

    @PatchMapping("/{id}/verify")
    @Operation(summary = "Verify or reject food provider", description = "Updates provider verification status (VERIFIED or REJECTED) with optional remarks.")
    public ResponseEntity<ProviderResponse> verifyProvider(
            @PathVariable String id,
            @Valid @RequestBody VerifyProviderRequest request) {
        return ResponseEntity.ok(providerService.verifyProvider(id, request));
    }

    @GetMapping("/{id}/is-verified")
    @Operation(summary = "Check verification status", description = "Lightweight boolean endpoint used by Food Listing Service to check if a provider is verified.")
    public ResponseEntity<Boolean> isProviderVerified(@PathVariable String id) {
        return ResponseEntity.ok(providerService.isProviderVerified(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete provider profile", description = "Deletes a food provider record.")
    public ResponseEntity<Void> deleteProvider(@PathVariable String id) {
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }
}

