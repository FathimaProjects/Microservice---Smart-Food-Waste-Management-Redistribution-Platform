package com.rescuebite.provider.service;

import com.rescuebite.provider.dto.CreateProviderRequest;
import com.rescuebite.provider.dto.ProviderResponse;
import com.rescuebite.provider.dto.UpdateProviderRequest;
import com.rescuebite.provider.dto.VerifyProviderRequest;
import com.rescuebite.provider.exception.BadRequestException;
import com.rescuebite.provider.exception.ResourceNotFoundException;
import com.rescuebite.provider.model.BusinessType;
import com.rescuebite.provider.model.FoodProvider;
import com.rescuebite.provider.model.VerificationStatus;
import com.rescuebite.provider.repository.FoodProviderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodProviderService {

    private final FoodProviderRepository providerRepository;

    public FoodProviderService(FoodProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public ProviderResponse createProvider(CreateProviderRequest request) {
        if (providerRepository.existsByUserId(request.getUserId())) {
            throw new BadRequestException("A business profile already exists for user ID: " + request.getUserId());
        }

        if (providerRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BadRequestException("Registration number " + request.getRegistrationNumber() + " is already registered");
        }

        FoodProvider provider = new FoodProvider(
                request.getUserId(),
                request.getBusinessName(),
                request.getBusinessType(),
                request.getRegistrationNumber(),
                request.getContactEmail(),
                request.getContactPhone(),
                request.getAddress(),
                request.getCity(),
                request.getOperatingHours()
        );

        FoodProvider saved = providerRepository.save(provider);
        return new ProviderResponse(saved);
    }

    public ProviderResponse getProviderById(String id) {
        FoodProvider provider = findProviderById(id);
        return new ProviderResponse(provider);
    }

    public ProviderResponse getProviderByUserId(String userId) {
        FoodProvider provider = providerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found for user ID: " + userId));
        return new ProviderResponse(provider);
    }

    public List<ProviderResponse> getAllProviders(VerificationStatus status, String city, BusinessType businessType) {
        List<FoodProvider> list;
        if (status != null) {
            list = providerRepository.findByVerificationStatus(status);
        } else if (city != null && !city.isBlank()) {
            list = providerRepository.findByCityIgnoreCase(city);
        } else if (businessType != null) {
            list = providerRepository.findByBusinessType(businessType);
        } else {
            list = providerRepository.findAll();
        }

        return list.stream()
                .map(ProviderResponse::new)
                .collect(Collectors.toList());
    }

    public ProviderResponse updateProvider(String id, UpdateProviderRequest request) {
        FoodProvider provider = findProviderById(id);
        provider.setBusinessName(request.getBusinessName());
        provider.setBusinessType(request.getBusinessType());
        provider.setContactEmail(request.getContactEmail());
        provider.setContactPhone(request.getContactPhone());
        provider.setAddress(request.getAddress());
        provider.setCity(request.getCity());
        provider.setOperatingHours(request.getOperatingHours());
        provider.setUpdatedAt(LocalDateTime.now());

        FoodProvider updated = providerRepository.save(provider);
        return new ProviderResponse(updated);
    }

    public ProviderResponse verifyProvider(String id, VerifyProviderRequest request) {
        FoodProvider provider = findProviderById(id);
        provider.setVerificationStatus(request.getStatus());
        provider.setRejectionReason(request.getRejectionReason());
        provider.setUpdatedAt(LocalDateTime.now());

        FoodProvider updated = providerRepository.save(provider);
        return new ProviderResponse(updated);
    }

    public boolean isProviderVerified(String id) {
        return providerRepository.findById(id)
                .map(p -> p.getVerificationStatus() == VerificationStatus.VERIFIED)
                .orElse(false);
    }

    public void deleteProvider(String id) {
        FoodProvider provider = findProviderById(id);
        providerRepository.delete(provider);
    }

    private FoodProvider findProviderById(String id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food Provider not found with ID: " + id));
    }
}
