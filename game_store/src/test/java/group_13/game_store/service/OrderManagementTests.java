package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;
import group_13.game_store.model.Game.VisibilityStatus;
import group_13.game_store.model.GameCategory;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.GameRepository;

import org.junit.jupiter.api.Test;

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


    // sample data
    // date of order
    private Date randomDate1 = Date.valueOf("2024-02-09");
    // date of successful return
    private Date randomDate2 = Date.valueOf("2024-02-14");
    // date of unsuccessful return
    private Date randomDate3 = Date.valueOf("2024-02-16");
    
    private Customer customer1 = new Customer("Generic Name", "Generic Username", "name@outlook.com", "Passw0rd", "555-555-5555");
    private GameCategory category1 = new GameCategory("this type of game involves X", GameCategory.VisibilityStatus.Visible, "generic category");
    private Game game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);
    private Order order1 = new Order(randomDate1, null, customer1);


    public void setup() {
        category1.setCategoryID(1);
        game1.setGameID(1);
        order1.setOrderID(1);
    }

    @Test
    public void testGetOrderByIdWhenValidId() {
        // arrange
        when(orderRepository.findByOrderID(1)).thenReturn(order1);

        // act
        Order orderToFind = service.getOrderById(1);

        // assert
        assertNotNull(orderToFind);
		assertEquals(randomDate1, orderToFind.getPurchaseDate());
		assertNull(orderToFind.getReturnDate());
		assertEquals(customer1.getUsername(), orderToFind.getCustomer().getUsername());
    }

    @Test
    public void testGetOrderByIdWhenInvalidId() {

    }

    @Test
    public void testReturnOrderWhenSuccessful() {
        
    }

    @Test
    public void testReturnOrderWhenUnsuccessful() {
        
    }

    @Test
    public void testGetOrderHistoryOfCustomerWhenCustomerExists() {

    }

    @Test 
    public void testGetOrderHistoryOfCustomerWhenCustomerDoesNotExists() {

    }
}