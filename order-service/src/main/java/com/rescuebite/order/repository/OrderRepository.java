package com.rescuebite.order.repository;

import com.rescuebite.order.model.Order;
import com.rescuebite.order.model.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByCustomerId(String customerId);

    List<Order> findByProviderId(String providerId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByIdAndCollectionCode(String id, String collectionCode);
}

