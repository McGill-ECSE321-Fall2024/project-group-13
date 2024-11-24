package group_13.game_store.integration;

import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.dto.OrderListDto;
import group_13.game_store.dto.ReturnOrderRequestDto;
import group_13.game_store.dto.ReturnOrderResponseDto;
import group_13.game_store.dto.OrderCreationRequestDto;
import group_13.game_store.dto.OrderCreationResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.dto.UserAccountResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Order;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.OrderManagementService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UserAccountIntegrationTests {

    @Autowired
	private TestRestTemplate client;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserAccountRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PaymentInformationRepository paymentinfoRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private GameCategoryRepository categoryRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private GameCopyRepository copyRepository;

	@Autowired
    private AccountService accountService;

	// @Autowired
    // private PaymentService paymentService;

	@Autowired
    private OrderManagementService orderManagementService;

	private Customer customer1;
	private Customer customer2;
	private Employee employee1;
	private Game game1;
	private GameCategory category1;
	private group_13.game_store.model.Order order1;
	private group_13.game_store.model.Order order2;


	private Date randomDate1 = Date.valueOf("2024-02-09");
	private Date randomDate2 = Date.valueOf("2023-12-10");
	private Date randomDate3 = Date.valueOf("2023-12-05");
	private Date randomDate4 = Date.valueOf("2024-02-12");

	private String validName = "Bob";
	private String validUsername = "Bob1234";
	private String validEmail = "bobaccount@outlook.com";
	private String validPassword = "Passw0rd";
	private String validPhoneNumber = "555-555-5555";

	@BeforeAll
    public void setup() {
		customer1 = new Customer("RealNameOne", "FakeUsername1", "name1@outlook.com", "Passw0rd1", "555-553-1111");
		customer2 = new Customer("RealNameTwo", "FakeUsername2", "name2@outlook.com", "Passw0rd2", "555-553-2222");
		employee1 = new Employee("EmployeeName", "EmployeeUsername", "employeename@outlook.com", "Passw0rd1234567890", "444-553-4444", true);
		category1 = new GameCategory("this type of game involves X", GameCategory.VisibilityStatus.Visible, "generic category");
        game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);

		Address savedAddress = new Address("Sherbrooke St W", "H3A0G4", 845, "Montreal", "Quebec", "Canada", 0);
		PaymentInformation savedPaymentInformation = new PaymentInformation("1234123412341234", "RealNameOne",
                Date.valueOf("2027-10-11"), 123, savedAddress);
		customer1.setPaymentInformation(savedPaymentInformation);
		customer1.setAddress(savedAddress);

        CartItem savedCartItem = new CartItem(new CartItem.Key(customer1, game1), 1);

		order1 = new group_13.game_store.model.Order(randomDate1, null, customer1);
        order2 = new group_13.game_store.model.Order(randomDate2, null, customer1);

		GameCopy copy1 = new GameCopy(order1, game1);
		GameCopy copy2 = new GameCopy(order2, game1);

		addressRepository.save(savedAddress);
		paymentinfoRepository.save(savedPaymentInformation);
		categoryRepository.save(category1);
		gameRepository.save(game1);
		customerRepository.save(customer1);
		customerRepository.save(customer2);
		cartItemRepository.save(savedCartItem);
		employeeRepository.save(employee1);
		orderRepository.save(order1);
		orderRepository.save(order2);
		copyRepository.save(copy1);
		copyRepository.save(copy2);
	}

    @AfterAll
	public void clearDatabase() {
		employeeRepository.deleteAll();
		cartItemRepository.deleteAll();
		copyRepository.deleteAll();
		orderRepository.deleteAll();
		customerRepository.deleteAll();	
		gameRepository.deleteAll();
		categoryRepository.deleteAll();
		paymentinfoRepository.deleteAll();
		addressRepository.deleteAll();
	}

    @Test
	@org.junit.jupiter.api.Order(1)
	public void testCreateValidCustomerAccountAsAGuest() {
		// arrange
		UserAccountRequestDto testedCreatedAcount = new UserAccountRequestDto(validUsername, validName, validEmail, validPhoneNumber, validPassword);

		ResponseEntity<CustomerResponseDto> response = client.postForEntity("/customers?loggedInUsername=guest", testedCreatedAcount, CustomerResponseDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(validName, response.getBody().getName());
		assertEquals(validUsername, response.getBody().getUsername());
		assertEquals(validEmail, response.getBody().getEmail());
		assertEquals(validPhoneNumber, response.getBody().getPhoneNumber());
		assertEquals(1, response.getBody().getPermissionLevel());
		//assertEquals("guest", response);
		assertEquals(LocalDate.now(), response.getBody().getCreationDate());
    }

	// expect an exception
    @Test
	@org.junit.jupiter.api.Order(2)
	public void testCreateValidCustomerAccountWhenNotAGuestException() {
		// arrange
		UserAccountRequestDto testedCreatedAcount = new UserAccountRequestDto(validUsername, validName, validEmail, validPhoneNumber, validPassword);

		// act
		// WHAT TO DO ABOUT RREQUEST PARAM IF NOT LOGGED IN
		ResponseEntity<String> response = client.postForEntity("/customers?loggedInUsername=FakeUsername1", testedCreatedAcount, String.class);

		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be logged out to create a customer account", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
    }

	@Test
	@org.junit.jupiter.api.Order(3)
	public void testFindAllCustomersAsEmployee() {
		// Arrange
		System.out.println("URL: /customers?loggedInUsername=EmployeeUsername");

		// act
		ResponseEntity<CustomerListDto> response = client.getForEntity("/customers?loggedInUsername=EmployeeUsername", CustomerListDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(3, response.getBody().getCustomers().size());
		// checking to see if these customer dtos are within the CustomerListDto
		Customer customer3 = customerRepository.findByUsername(validUsername);
		assertEquals(customer1.getName(), response.getBody().getCustomers().get(0).getName());
		assertEquals(customer1.getUsername(), response.getBody().getCustomers().get(0).getUsername());
		assertEquals(customer1.getEmail(), response.getBody().getCustomers().get(0).getEmail());
		assertEquals(customer1.getPhoneNumber(), response.getBody().getCustomers().get(0).getPhoneNumber());
		assertEquals(customer2.getName(), response.getBody().getCustomers().get(1).getName());
		assertEquals(customer2.getUsername(), response.getBody().getCustomers().get(1).getUsername());
		assertEquals(customer2.getEmail(), response.getBody().getCustomers().get(1).getEmail());
		assertEquals(customer2.getPhoneNumber(), response.getBody().getCustomers().get(1).getPhoneNumber());
		assertEquals(customer3.getName(), response.getBody().getCustomers().get(2).getName());
		assertEquals(customer3.getUsername(), response.getBody().getCustomers().get(2).getUsername());
		assertEquals(customer3.getEmail(), response.getBody().getCustomers().get(2).getEmail());
		assertEquals(customer3.getPhoneNumber(), response.getBody().getCustomers().get(2).getPhoneNumber());

	}

	@Test
	@org.junit.jupiter.api.Order(4)
	public void testFindAllCustomersAsCustomerException(){
		// Arrange
		System.out.println("URL: /customers?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<String> response = client.getForEntity("/customers?loggedInUsername=FakeUsername1", String.class);

		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be an owner or employee to view all customers", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	public void testFindCustomerAsEmployee(){
		// Arrange
		System.out.println("URL: /customers/FakeUsername1?loggedInUsername=EmployeeUsername");

		// act
		ResponseEntity<CustomerResponseDto> response = client.getForEntity("/customers/FakeUsername1?loggedInUsername=EmployeeUsername", CustomerResponseDto.class);
	
		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(customer1.getName(), response.getBody().getName());
		assertEquals(customer1.getUsername(), response.getBody().getUsername());
		assertEquals(customer1.getEmail(), response.getBody().getEmail());
		assertEquals(customer1.getPhoneNumber(), response.getBody().getPhoneNumber());
		assertEquals(LocalDate.now(), response.getBody().getCreationDate());
	}

	@Test
	@org.junit.jupiter.api.Order(6)
	public void testFindCustomerAsCustomerException(){
		// Arrange
		System.out.println("URL: /customers/FakeUsername2?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<String> response = client.getForEntity("/customers/FakeUsername2?loggedInUsername=FakeUsername1", String.class);
	
		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be an owner or employee", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test
	@org.junit.jupiter.api.Order(7)
	public void testUpdateGeneralUserInformationWhenNotAGuest(){
		// arrange
		String newValidPassword = "Br4ndN3wPassw0rd";
		String newValidPhoneNumber = "111-111-1111";
		UserAccountRequestDto testedUpdatedAccount = new UserAccountRequestDto(validUsername, validName, validEmail, newValidPhoneNumber, newValidPassword);
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserAccountRequestDto> requestEntity = new HttpEntity<>(testedUpdatedAccount, header);
		
		// act
		ResponseEntity<UserAccountResponseDto> response = client.exchange("/users/Bob1234?loggedInUsername=Bob1234", HttpMethod.PUT, requestEntity, UserAccountResponseDto.class);
		// the information should be updated after the above line was executed
		UserAccount updatedUser = userRepository.findByUsername(validUsername);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(accountService.hashPassword(newValidPassword), updatedUser.getPassword());
		assertEquals(newValidPhoneNumber, updatedUser.getPhoneNumber());
	}

	@Test
	@org.junit.jupiter.api.Order(8)
	public void testUpdateGeneralUserInformationWhenAGuestException(){
		// arrange
		String newValidPassword = "Br4ndN3wPassw0rd";
		String newValidPhoneNumber = "111-111-1111";
		UserAccountRequestDto testedUpdatedAccount = new UserAccountRequestDto(validName, validUsername, validEmail, newValidPhoneNumber, newValidPassword);
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserAccountRequestDto> requestEntity = new HttpEntity<>(testedUpdatedAccount, header);

		// act
		ResponseEntity<String> response = client.exchange("/users/guest?loggedInUsername=guest", HttpMethod.PUT, requestEntity, String.class);
	
		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("Must be registered user to change phone number or password", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test 
	@org.junit.jupiter.api.Order(9)
	public void testFindAllOrdersOfCustomerAsASameCustomer() {
		// Arrange
		System.out.println("URL: /customers/FakeUsername1/orders?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<OrderListDto> response = client.getForEntity("/customers/FakeUsername1/orders?loggedInUsername=FakeUsername1", OrderListDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getOrders().size());
		// checking to see if these order dtos are within the OrderListDto
		assertEquals(order1.getPurchaseDate().toString(), response.getBody().getOrders().get(0).getPurchaseDate().toString());
		//assertEquals(order1.getReturnDate(), response.getBody().getOrders().get(0).getReturnDate());
		assertEquals(order1.getCustomer().getUsername(), response.getBody().getOrders().get(0).getCustomer().getUsername());
		assertEquals(order2.getPurchaseDate().toString(), response.getBody().getOrders().get(1).getPurchaseDate().toString());
		//assertEquals(order2.getReturnDate(), response.getBody().getOrders().get(1).getReturnDate());
		assertEquals(order2.getCustomer().getUsername(), response.getBody().getOrders().get(1).getCustomer().getUsername());
	}

	@Test 
	@org.junit.jupiter.api.Order(10)
	public void testFindAllOrdersOfCustomerAsNotSameCustomerException() {
		// Arrange
		System.out.println("URL: /customers/FakeUsername2/orders?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<String> response = client.getForEntity("/customers/FakeUsername2/orders?loggedInUsername=FakeUsername1", String.class);

		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be a customer to view their own orders", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test 
	@org.junit.jupiter.api.Order(11)
	public void testCreateOrderAsCustomer() {
		// arrange
		OrderCreationRequestDto testedCreatedOrder = new OrderCreationRequestDto(randomDate1, customer1.getUsername());

		// act
		ResponseEntity<OrderCreationResponseDto> response = client.postForEntity("/customers/FakeUsername1/orders?loggedInUsername=FakeUsername1", testedCreatedOrder, OrderCreationResponseDto.class);
		//ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> paymentService.purchaseCart(customer1.getUsername()));
        
		// assert
		assertNotNull(response);
		// fails most likely because purchase cart is empty
		assertEquals(HttpStatus.OK, response.getStatusCode());
		//
		//assertEquals("", exception.getReason());
		assertEquals(randomDate1.toString(), response.getBody().getPurchaseDate().toString());
		assertEquals(customer1.getUsername(), response.getBody().getCustomer().getUsername());
		}

	@Test 
	@org.junit.jupiter.api.Order(12)
	public void testCreateOrderAsNonCustomer() {
		// arrange
		OrderCreationRequestDto testedCreatedOrder = new OrderCreationRequestDto(randomDate1, customer1.getUsername());

		// act
		ResponseEntity<String> response = client.postForEntity("/customers/EmployeeUsername/orders?loggedInUsername=EmployeeUsername", testedCreatedOrder, String.class);

		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be a customer to make own orders", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test 
	@org.junit.jupiter.api.Order(13)
	public void testFindOrderOfCustomerAsCustomer(){
		// Arrange
		int orderId = order1.getOrderID();
		System.out.println("URL: /customers/FakeUsername1/orders/" + String.valueOf(orderId) + "?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<OrderCreationResponseDto> response = client.getForEntity("/customers/FakeUsername1/orders/" + String.valueOf(orderId) + "?loggedInUsername=FakeUsername1", OrderCreationResponseDto.class);
	
		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(order1.getOrderID(), response.getBody().getOrderId());
		assertEquals(order1.getPurchaseDate().toString(), response.getBody().getPurchaseDate().toString());
		assertEquals(order1.getCustomer().getUsername(), response.getBody().getCustomer().getUsername());
	}

	@Test 
	@org.junit.jupiter.api.Order(14)
	public void testFindOrderOfCustomerAsNonCustomerException(){
		// Arrange
		System.out.println("URL: /customers/FakeUsername1/orders/1?loggedInUsername=EmployeeUsername");

		// act
		ResponseEntity<String> response = client.getForEntity("/customers/FakeUsername1/orders/1?loggedInUsername=EmployeeUsername", String.class);
	
		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be a customer to check their own order", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}

	@Test 
	@org.junit.jupiter.api.Order(15)
	public void testReturnOrderAsCustomer() {
		ReturnOrderRequestDto testedUpdatedOrder = new ReturnOrderRequestDto(randomDate4, customer1.getUsername());
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ReturnOrderRequestDto> requestEntity = new HttpEntity<>(testedUpdatedOrder, header);

		// act
		ResponseEntity<ReturnOrderResponseDto> response = client.exchange("/customers/FakeUsername1/orders/" + String.valueOf(order1.getOrderID()) + "?loggedInUsername=FakeUsername1", HttpMethod.PUT, requestEntity, ReturnOrderResponseDto.class);

		//ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderManagementService.returnOrder(order1.getOrderID(), randomDate4));
        Order updatedOrder = orderRepository.findByOrderID(order1.getOrderID());

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedOrder.getReturnDate().toString(), response.getBody().getReturnDate().toString());
		assertEquals(updatedOrder.getCustomer().getUsername(), response.getBody().getCustomerUsername());
	}

	@Test 
	@org.junit.jupiter.api.Order(16)
	public void testReturnOrderAsNonCustomer() {
		ReturnOrderRequestDto testedUpdatedOrder = new ReturnOrderRequestDto(randomDate4, customer1.getUsername());
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ReturnOrderRequestDto> requestEntity = new HttpEntity<>(testedUpdatedOrder, header);

		// act
		ResponseEntity<String> response = client.exchange("/customers/FakeUsername1/orders/" + String.valueOf(order1.getOrderID()) +"?loggedInUsername=EmployeeUsername", HttpMethod.PUT, requestEntity, String.class);

		//ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderManagementService.returnOrder(order1.getOrderID(), randomDate4));
        
		// assert
		
		//assertEquals("", exception.getReason());


		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be a customer to return their order", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("Response body is not a valid JSON");
		}
	}
}
