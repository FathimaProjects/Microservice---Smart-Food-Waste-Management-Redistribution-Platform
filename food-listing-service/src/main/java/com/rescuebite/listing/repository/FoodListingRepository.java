package com.rescuebite.listing.repository;

import com.rescuebite.listing.model.FoodCategory;
import com.rescuebite.listing.model.FoodListing;
import com.rescuebite.listing.model.ListingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodListingRepository extends MongoRepository<FoodListing, String> {

    List<FoodListing> findByProviderId(String providerId);

    List<FoodListing> findByStatus(ListingStatus status);

    List<FoodListing> findByCategory(FoodCategory category);

    List<FoodListing> findByDonation(boolean donation);

    List<FoodListing> findByCategoryAndStatus(FoodCategory category, ListingStatus status);
}

