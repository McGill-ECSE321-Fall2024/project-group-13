package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;

@SpringBootTest
public class OrderRepositoryTests {
    // Initialize the repositories
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CustomerRepository customerRepo;

    // Clear the repository after each test
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @Test
    public void testCreateandReadOrder() {
        // Arrange
        Customer customer = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890", 1);
        Order order = new Order(Date.valueOf("2024-02-09"), 15, Date.valueOf("2024-02-15"), true, customer);

        customer = customerRepo.save(customer);
        order = orderRepo.save(order);
        int id = order.getOrderID();

        // Act
        Order orderFromDb = orderRepo.findByOrderID(id);

        // Assert
        assertNotNull(orderFromDb);
        assertEquals(id, orderFromDb.getOrderID());
        assertNotNull(orderFromDb.getCustomer());
        assertEquals(order.getCustomer().getUsername(), orderFromDb.getCustomer().getUsername());
        assertEquals(order.getPurchaseDate(), orderFromDb.getPurchaseDate());
        assertEquals(order.getTotalPrice(), orderFromDb.getTotalPrice());
        assertEquals(order.getReturnDate(), orderFromDb.getReturnDate());
        assertEquals(order.getIsReturned(), orderFromDb.getIsReturned());

    }

}
