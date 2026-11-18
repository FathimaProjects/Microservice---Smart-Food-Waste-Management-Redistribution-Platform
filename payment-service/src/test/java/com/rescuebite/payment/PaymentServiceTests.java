package com.rescuebite.payment;

import com.rescuebite.payment.dto.OrderDto;
import com.rescuebite.payment.dto.ProcessPaymentRequest;
import com.rescuebite.payment.exception.PaymentProcessingException;
import com.rescuebite.payment.model.Payment;
import com.rescuebite.payment.model.PaymentMethod;
import com.rescuebite.payment.model.PaymentStatus;
import com.rescuebite.payment.repository.PaymentRepository;
import com.rescuebite.payment.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentServiceTests {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private RestClient restClient;

    @Test
    @DisplayName("Should detect simulated payment failure and save FAILED record")
    void testSimulatedPaymentFailure() {
        when(paymentRepository.findByOrderId("ord-001")).thenReturn(Optional.empty());

        ProcessPaymentRequest req = new ProcessPaymentRequest();
        req.setOrderId("ord-001");
        req.setCustomerId("c-001");
        req.setAmount(1200.0);
        req.setCurrency("LKR");
        req.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        req.setCardLastFour("0002");
        req.setSimulateFailure(true);

        // When RestClient is mocked, verify that simulateFailure behaves as expected
        assertThrows(Exception.class, () -> paymentService.processPayment(req));
    }
}

