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

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.dto.UserAccountResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UserAccountIntegrationTests {

    @Autowired
	private TestRestTemplate client;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	private Customer customer1;
	private Customer customer2;
	//private Customer customer3;
	private Employee employee1;

	private String validName = "Bob";
	private String validUsername = "Bob1234";
	private String validEmail = "bobaccount@outlook.com";
	private String validPassword = "Passw0rd";
	private String validPhoneNumber = "555-555-5555";

	@BeforeEach
    public void setup() {
		customer1 = new Customer("RealNameOne", "FakeUsername1", "name1@outlook.com", "555-553-111" ,"Passw0rd1");
		customer2 = new Customer("RealNameTwo", "FakeUsername2", "name2@outlook.com", "555-553-222" ,"Passw0rd2");
		//customer3 = new Customer("RealNameThree", "FakeUsername3", "name3@outlook.com", "555-553-333" ,"Passw0rd3");
		employee1 = new Employee("EmployeeName", "EmployeeUsername", "employeename@outlook.com", "444-553-444" ,"Passw0rd1234567890", true);
		
		// create some orders too
		customerRepository.save(customer1);
		customerRepository.save(customer2);
		//customerRepository.save(customer3);
		employeeRepository.save(employee1);
	}

    @AfterAll
	public void clearDatabase() {
		customerRepository.deleteAll();
	}

    @Test
	@Order(1)
	public void testCreateValidCustomerAccountAsAguest() {
		// arrange
		UserAccountRequestDto testedCreatedAcount = new UserAccountRequestDto(validUsername, validName, validEmail, validPhoneNumber, validPassword);

		// act
		// WHAT TO DO ABOUT RREQUEST PARAM IF NOT LOGGED IN @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
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
	@Order(2)
	public void testCreateValidCustomerAccountWhenNotAGuestException() {
		// arrange
		UserAccountRequestDto testedCreatedAcount = new UserAccountRequestDto(validUsername, validName, validEmail, validPhoneNumber, validPassword);


		// act
		// WHAT TO DO ABOUT RREQUEST PARAM IF NOT LOGGED IN
		ResponseEntity<String> response = client.postForEntity("/customers?loggedInUsername=FakeUsername", testedCreatedAcount, String.class);

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
	@Order(3)
	public void testFindAllCustomersAsEmployee() {
		// Arrange
		System.out.println("URL: /customers?loggedInUsername=EmployeeUsername");

		// act
		ResponseEntity<CustomerListDto> response = client.getForEntity("/customers?loggedInUsername=EmployeeUsername", CustomerListDto.class);

		// assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().customers().size());
		//assertEquals();
		
	}

	@Test
	@Order(4)
	public void testFindAllCustomersAsCustomer(){
		
	}

	@Test
	@Order(5)
	public void testFindCustomerAsEmployee(){

	}

	@Test
	@Order(6)
	public void testFindAllCustomerAsCustomer(){
		
	}

	@Test
	@Order(7)
	public void testUpdateGeneralUserInformationWhenNotAguest(){

	}

	@Test
	@Order(8)
	public void testUpdateGeneralUserInformationWhenAGuest(){

	}

	@Test 
	@Order(9)
	public void testFindAllOrdersOfCustomerAsAnEmployee() {

	}

	@Test 
	@Order(10)
	public void testFindAllOrdersOfCustomerAsNonEmployee() {

	}

	@Test 
	@Order(11)
	public void testCreateOrderAsCustomer() {

	}

	@Test 
	@Order(12)
	public void testCreateOrderAsNonCustomer() {

	}

	@Test
	@Order(13)
	public void testFindOrderOfCustomerAsCustomer(){

	}

	@Test
	@Order(14)
	public void testFindOrderOfCustomerAsNonCustomer(){
		
	}

	@Test
	@Order(15)
	public void testReturnOrderAsCustomer() {

	}

	@Test
	@Order(16) 
	public void testReturnOrderAsNonCustomer() {
		
	}


}
