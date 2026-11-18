package com.rescuebite.provider.repository;

import com.rescuebite.provider.model.BusinessType;
import com.rescuebite.provider.model.FoodProvider;
import com.rescuebite.provider.model.VerificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodProviderRepository extends MongoRepository<FoodProvider, String> {

    Optional<FoodProvider> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<FoodProvider> findByVerificationStatus(VerificationStatus status);

    List<FoodProvider> findByCityIgnoreCase(String city);

    List<FoodProvider> findByBusinessType(BusinessType businessType);
}

