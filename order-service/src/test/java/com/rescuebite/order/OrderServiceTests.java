package com.rescuebite.order;

import com.rescuebite.order.exception.BadRequestException;
import com.rescuebite.order.model.Order;
import com.rescuebite.order.model.OrderStatus;
import com.rescuebite.order.repository.OrderRepository;
import com.rescuebite.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RestClient restClient;

    @Test
    @DisplayName("Should successfully collect order when given valid 6-digit code")
    void testCollectOrderWithValidCode() {
        Order confirmedOrder = new Order(
                "cust-hasna-001", "Hasna", "hasna@rescuebite.test",
                "lst-croissant-01", "Croissants Box", "prv-milfara-01",
                2, 650.0, 1300.0, false, OrderStatus.CONFIRMED,
                "849201", "Colombo 07", LocalDateTime.now().plusDays(1)
        );
        confirmedOrder.setId("ord-milfara-001");

        when(orderRepository.findById("ord-milfara-001")).thenReturn(Optional.of(confirmedOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        var response = orderService.collectOrder("ord-milfara-001", "849201");

        assertNotNull(response);
        assertEquals(OrderStatus.COLLECTED, response.getStatus());
    }

    @Test
    @DisplayName("Should reject order collection when given invalid collection code")
    void testCollectOrderWithInvalidCode() {
        Order confirmedOrder = new Order(
                "cust-hasna-001", "Hasna", "hasna@rescuebite.test",
                "lst-croissant-01", "Croissants Box", "prv-milfara-01",
                2, 650.0, 1300.0, false, OrderStatus.CONFIRMED,
                "849201", "Colombo 07", LocalDateTime.now().plusDays(1)
        );
        confirmedOrder.setId("ord-milfara-001");

        when(orderRepository.findById("ord-milfara-001")).thenReturn(Optional.of(confirmedOrder));

        assertThrows(BadRequestException.class, () -> orderService.collectOrder("ord-milfara-001", "000000"));
    }
}
