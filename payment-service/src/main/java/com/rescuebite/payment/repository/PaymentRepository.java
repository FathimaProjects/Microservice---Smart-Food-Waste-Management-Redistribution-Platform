package com.rescuebite.payment.repository;

import com.rescuebite.payment.model.Payment;
import com.rescuebite.payment.model.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByTransactionReference(String transactionReference);

    List<Payment> findByCustomerId(String customerId);

    List<Payment> findByStatus(PaymentStatus status);
}

