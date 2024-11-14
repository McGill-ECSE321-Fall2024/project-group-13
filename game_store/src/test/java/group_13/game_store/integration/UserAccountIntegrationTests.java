package group_13.game_store.integration;

import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.dto.OrderListDto;
import group_13.game_store.dto.OrderRequestDto;
import group_13.game_store.dto.OrderResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.dto.UserAccountResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Order;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;

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
	private GameCategoryRepository categoryRepository;

	@Autowired
	private OrderRepository orderRepository;

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
		customer1 = new Customer("RealNameOne", "FakeUsername1", "name1@outlook.com", "555-553-111" ,"Passw0rd1");
		customer2 = new Customer("RealNameTwo", "FakeUsername2", "name2@outlook.com", "555-553-222" ,"Passw0rd2");
		employee1 = new Employee("EmployeeName", "EmployeeUsername", "employeename@outlook.com", "444-553-444" ,"Passw0rd1234567890", true);
		category1 = new GameCategory("this type of game involves X", GameCategory.VisibilityStatus.Visible, "generic category");
        game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);

		Address savedAddress = new Address("Sherbrooke St W", "H3A 0G4", 845, "Montreal", "Quebec", "Canada", 0);
		PaymentInformation savedPaymentInformation = new PaymentInformation("123456789", "John Cena",
                Date.valueOf("2022-10-11"), 000, savedAddress);
		customer1.setPaymentInformation(savedPaymentInformation);

		//category1.setCategoryID(1);
       // game1.setGameID(1);
		order1 = new group_13.game_store.model.Order(randomDate1, null, customer1);
		//order1.setOrderID(1);
        order2 = new group_13.game_store.model.Order(randomDate2, null, customer1);
		//order2.setOrderID(2);

		addressRepository.save(savedAddress);
		paymentinfoRepository.save(savedPaymentInformation);
		categoryRepository.save(category1);
		gameRepository.save(game1);
		customerRepository.save(customer1);
		customerRepository.save(customer2);
		employeeRepository.save(employee1);
		orderRepository.save(order1);
		orderRepository.save(order2);
	}

    @AfterAll
	public void clearDatabase() {
		employeeRepository.deleteAll();
		orderRepository.deleteAll();
		customerRepository.deleteAll();
		categoryRepository.deleteAll();
		gameRepository.deleteAll();
		paymentinfoRepository.deleteAll();
		addressRepository.deleteAll();
	}

    @Test
	@org.junit.jupiter.api.Order(1)
	public void testCreateValidCustomerAccountAsAGuest() {
		// arrange
		UserAccountRequestDto testedCreatedAcount = new UserAccountRequestDto(validUsername, validName, validEmail, validPhoneNumber, validPassword);

		// act
		// WHAT TO DO ABOUT REQUESTPARAM IF NOT LOGGED IN @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

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
		//assertEquals(newValidPassword, updatedUser.getPassword());
		assertEquals(newValidPhoneNumber, updatedUser.getPhoneNumber());
	}

	// dealing with guest again @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
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
		//assertEquals(order1.getPurchaseDate(), response.getBody().getOrders().get(0).getPurchaseDate());
		//assertEquals(order1.getReturnDate(), response.getBody().getOrders().get(0).getReturnDate());
		assertEquals(order1.getCustomer().getUsername(), response.getBody().getOrders().get(0).getCustomer().getUsername());
		//assertEquals(order2.getPurchaseDate(), response.getBody().getOrders().get(1).getPurchaseDate());
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
		OrderRequestDto testedCreatedOrder = new OrderRequestDto(randomDate1, null, customer1);

		// act
		ResponseEntity<OrderResponseDto> response = client.postForEntity("/customers/FakeUsername1/orders?loggedInUsername=FakeUsername1", testedCreatedOrder, OrderResponseDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(randomDate1, response.getBody().getPurchaseDate());
		assertNull(response.getBody().getReturnDate());
		assertEquals(customer1.getUsername(), response.getBody().getCustomer().getUsername());
		}

	@Test 
	@org.junit.jupiter.api.Order(12)
	public void testCreateOrderAsNonCustomer() {
		// arrange
		OrderRequestDto testedCreatedOrder = new OrderRequestDto(randomDate1, null, customer1);

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
		System.out.println("URL: /customers/FakeUsername1/orders/" + orderId + "?loggedInUsername=FakeUsername1");

		// act
		ResponseEntity<OrderResponseDto> response = client.getForEntity("URL: /customers/FakeUsername1/orders/" + orderId + "?loggedInUsername=FakeUsername1", OrderResponseDto.class);
	
		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(order1.getOrderID(), response.getBody().getOrderId());
		assertNull(response.getBody().getReturnDate());
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
		OrderRequestDto testedUpdatedOrder = new OrderRequestDto(randomDate1, randomDate4, customer1);
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(testedUpdatedOrder, header);

		// act
		ResponseEntity<OrderResponseDto> response = client.exchange("/users/FakeUsername1/orders/1/games/1&loggedInUsername=FakeUsername1", HttpMethod.PUT, requestEntity, OrderResponseDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(randomDate4, response.getBody().getReturnDate());
		assertEquals(customer1.getUsername(), response.getBody().getCustomer().getUsername());
		assertEquals(randomDate1, response.getBody().getPurchaseDate());
	}

	@Test 
	@org.junit.jupiter.api.Order(16)
	public void testReturnOrderAsNonCustomer() {
		OrderRequestDto testedUpdatedOrder = new OrderRequestDto(randomDate1, randomDate4, customer1);
		// creating the request entity
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(testedUpdatedOrder, header);

		// act
		ResponseEntity<String> response = client.exchange("/users/FakeUsername1/orders/1/games/1&loggedInUsername=EmployeeUsername", HttpMethod.PUT, requestEntity, String.class);

		// assert
		try {
			org.json.JSONObject json = new org.json.JSONObject(response.getBody());
			assertEquals(403, json.getInt("status"));
			assertEquals("Forbidden", json.getString("error"));
			assertEquals("User must be a customer to check their own order", json.getString("message"));
		} catch (org.json.JSONException e){
			fail("User must be a customer to return their order");
		}
	}


}
