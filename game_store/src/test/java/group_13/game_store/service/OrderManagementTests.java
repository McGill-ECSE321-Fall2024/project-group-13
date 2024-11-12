package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.invocation.InvocationOnMock;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;
import group_13.game_store.model.Game.VisibilityStatus;
import group_13.game_store.model.GameCategory;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.GameRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class OrderManagementTests {

    @Mock
	private OrderRepository orderRepository;
    @Mock
	private CustomerRepository customerRepository;
    @Mock
    private GameRepository gameRepository;
	@InjectMocks
	private OrderManagementService service;

    // declaring instance variables that will be useful for tests
    private Date randomDate1;
    private Date randomDate2;
    private Date randomDate3;
    private Date randomDate4;
    private Date randomDate5;
    private Customer customer1;
    private GameCategory category1;
    private Game game1;
    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    public void setup() {
        // sample data
        // date of order
        randomDate1 = Date.valueOf("2024-02-09");
        // date of successful return
        randomDate2 = Date.valueOf("2024-02-14");
        // date of unsuccessful return
        randomDate3 = Date.valueOf("2024-02-17");
        // date of purchase of other orders
        randomDate4 = Date.valueOf("2023-12-10");
        randomDate5 = Date.valueOf("2023-12-09"); 
        
        customer1 = new Customer("Generic Name", "Generic Username", "name@outlook.com", "Passw0rd", "555-555-5555");
        category1 = new GameCategory("this type of game involves X", GameCategory.VisibilityStatus.Visible, "generic category");
        game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);
        order1 = new Order(randomDate1, null, customer1);
        order2 = new Order(randomDate4, null, customer1);
        order3 = new Order(randomDate5, null, customer1);

        // want to guarantee the IDs of these instances for tests
        category1.setCategoryID(1);
        game1.setGameID(1);
        order1.setOrderID(1);
        order2.setOrderID(2);
        order3.setOrderID(3);
    }

    @Test
    public void testGetOrderByIdWhenValidId() {
        // arrange
        int validOrderId = 1;
        when(orderRepository.findByOrderID(validOrderId)).thenReturn(order1);

        // act
        Order orderToFind = service.getOrderById(validOrderId);

        // assert
        assertNotNull(orderToFind);
		assertEquals(randomDate1, orderToFind.getPurchaseDate());
		assertNull(orderToFind.getReturnDate());
		assertEquals(customer1.getUsername(), orderToFind.getCustomer().getUsername());

        verify(orderRepository, times(1)).findByOrderID(validOrderId);
    }

    @Test
    public void testGetOrderByIdWhenInvalidId() {
        // arrange
        int invalidOrderId = 33;
        when(orderRepository.findByOrderID(invalidOrderId)).thenReturn(null);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.getOrderById(invalidOrderId));
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No order with order ID 33.", exception.getReason());

        verify(orderRepository, times(1)).findByOrderID(invalidOrderId);
    }

    @Test
    public void testReturnOrderWhenSuccessful() {
        // arrange
        int validOrderId = 1;
        int validGameId = 1;
        when(orderRepository.findByOrderID(validOrderId)).thenReturn(order1);
        when(gameRepository.findByGameID(validGameId)).thenReturn(game1);
        when(gameRepository.save(any(Game.class))).thenReturn(game1);
        when(orderRepository.save(any(Order.class))).thenReturn(order1);
        
        // act
        Order orderToReturn = service.returnOrder(order1.getOrderID(), game1.getGameID(), randomDate2);

        
        // assert
        assertNotNull(orderToReturn);
		assertEquals(randomDate1, orderToReturn.getPurchaseDate());
        assertEquals(randomDate2, orderToReturn.getReturnDate());
		assertEquals(customer1.getUsername(), orderToReturn.getCustomer().getUsername());
        assertTrue(orderToReturn.isIsReturned());

        verify(orderRepository, times(1)).findByOrderID(validOrderId);
        verify(gameRepository, times(1)).findByGameID(validGameId);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testReturnOrderWhenUnsuccessful() {     
        // arrange
        int validOrderId = 1;
        int validGameId = 1;
        when(orderRepository.findByOrderID(validOrderId)).thenReturn(order1);
        when(gameRepository.findByGameID(validGameId)).thenReturn(game1);
        
        // act
        // would not return an exception but a null instead...
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.returnOrder(order1.getOrderID(), game1.getGameID(), randomDate3));
        
        // assert
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Order 1 cannot be returned, because 7 days have already passed since its purchase.", exception.getReason());

        verify(orderRepository, times(1)).findByOrderID(validOrderId);
    }

    @Test
    public void testGetOrderHistoryOfCustomerWhenCustomerExists() {
        // arrange
        when(customerRepository.findByUsername(customer1.getUsername())).thenReturn(customer1);
        when(orderRepository.findByCustomer_Username(customer1.getUsername())).thenReturn(List.of(order1, order2, order3));
    
        // act
        List<Order> orderHistory = service.getOrderHistoryOfCustomer(customer1.getUsername());

        // assert
        // order history should exist
        assertNotNull(orderHistory);
        assertEquals(3, orderHistory.size(), "There should be 3 orders in the order history");
        assertTrue(orderHistory.contains(order1), "order1 should be in the order history");
        assertTrue(orderHistory.contains(order2), "order1 should be in the order history");
        assertTrue(orderHistory.contains(order3), "order1 should be in the order history");

        verify(customerRepository, times(1)).findByUsername(customer1.getUsername());
        verify(orderRepository, times(1)).findByCustomer_Username(customer1.getUsername());
    }

    @Test 
    public void testGetOrderHistoryOfCustomerWhenCustomerDoesNotExists() {
        // arrange
        String invalidUsername = "Bob";
        when(customerRepository.findByUsername(invalidUsername)).thenReturn(null);

        // act
        // order would not show up at all since customer should not exist
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.getOrderHistoryOfCustomer(invalidUsername));
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No customer with username ID Bob.", exception.getReason());

        verify(customerRepository, times(1)).findByUsername(invalidUsername);
    }
}
