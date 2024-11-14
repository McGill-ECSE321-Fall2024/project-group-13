package group_13.game_store.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.LoginRequestDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.service.AccountService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoginIntegrationTests {
    // Inject all dependencies for repositories and client
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AccountService accountService;

    // Set fields to be used for testing
    private String validUsername = "marrec8";
    private String invlaidUsername = "bob";
    private String validPassword = "GreatPassword8";
    private String invalidPassword = "greatPassword8";

    @BeforeAll
    public void setup() {
        // Create objects to use for testing
        Customer customer = new Customer ("Marrec", "marrec8", "marrec@mail", "GreatPassword8", "123-456-7890");
        String hashedPassword = accountService.hashPassword(customer.getPassword());
        customer.setPassword(hashedPassword);
        customerRepo.save(customer);

        Employee employee = new Employee ("Will", "william1", "william@mail", "GreatPassword7", "098-765-4321", true);
        employeeRepo.save(employee);
    }


    @AfterAll
    public void deleteAll() {
        customerRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    // ************************** LOGIN TEST ************************************************
    @Test
    @Order(1)
    public void testLoginInvalidUsername() {
        // Arrange
        LoginRequestDto loginInfo = new LoginRequestDto(invlaidUsername, validPassword);
        String url = "/login?loggedInUsername=guest";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginInfo, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Parse the response body as JSON
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(404, json.getInt("status"));
            assertEquals("Not Found", json.getString("error"));
            assertEquals("User with given username not found", json.getString("message"));
        } catch (JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(2)
    public void testLoginInvalidPassword() {
        // Arrange
        LoginRequestDto loginInfo = new LoginRequestDto(validUsername, invalidPassword);
        String url = "/login?loggedInUsername=guest";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginInfo, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Parse the response body as JSON
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(400, json.getInt("status"));
            assertEquals("Bad Request", json.getString("error"));
            assertEquals("Password does not match the account with the given username", json.getString("message"));
        } catch (JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(3)
    public void testLoginInvalidPermission() {
        // Arrange
        LoginRequestDto loginInfo = new LoginRequestDto(validUsername, validPassword);
        String url = "/login?loggedInUsername=marrec8";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginInfo, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Parse the response body as JSON
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(400, json.getInt("status"));
            assertEquals("Bad Request", json.getString("error"));
            assertEquals("You are already logged in", json.getString("message"));
        } catch (JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(4)
    public void testLoginSuccess() {
        // Arrange
        LoginRequestDto loginInfo = new LoginRequestDto(validUsername, validPassword);
        String url = "/login?loggedInUsername=guest";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginInfo, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), validUsername);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        

    }




}
