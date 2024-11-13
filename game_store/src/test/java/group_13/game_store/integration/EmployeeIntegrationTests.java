package group_13.game_store.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.EmployeeListResponseDto;
import group_13.game_store.dto.EmployeeResponseDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.UserAccountRepository;
import group_13.game_store.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class EmployeeIntegrationTests {

    @Autowired
    private TestRestTemplate client;
    @Autowired
    private UserAccountRepository userAccountRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    private static final String OWNER_USERNAME = "owner";
    private static final String EMPLOYEE_USERNAME = "employee";
    private static final String NEW_EMPLOYEE_USERNAME = "new_employee";
    private static final String EMPLOYEE_EMAIL = "employee@example.com";
    private static final String EMPLOYEE_PASSWORD = "password";
    private static final String EMPLOYEE_NAME = "John Doe";
    private static final String EMPLOYEE_PHONE = "1234567890";

    @AfterAll
    public void clearDatabase() {
        employeeRepo.deleteAll();
        userAccountRepo.deleteAll();
    }

    private void setupAccounts() {
         employeeRepo.save(new Employee("Will", EMPLOYEE_USERNAME, "tim@yahoo.ca", "123", "1234567890", true));
    }

    @Test
    @Order(1)
    public void testGetAllEmployeesWithOwnerPermission() {
        setupAccounts();

        // Arrange
        employeeRepo.save(new Employee(EMPLOYEE_NAME, EMPLOYEE_USERNAME, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE, true));

        // Act
        ResponseEntity<EmployeeListResponseDto> response = client.getForEntity("/employees?loggedInUsername=" + OWNER_USERNAME, EmployeeListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getEmployees());
        assertEquals(1, response.getBody().getEmployees().size());
        assertEquals(EMPLOYEE_USERNAME, response.getBody().getEmployees().get(0).getUsername());
    }

    @Test
    @Order(2)
    public void testGetAllEmployeesWithoutOwnerPermission() {
        // Act
        ResponseEntity<String> response = client.getForEntity("/employees?loggedInUsername=" + EMPLOYEE_USERNAME, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testCreateEmployee() {
        // Arrange
        UserAccountRequestDto request = new UserAccountRequestDto(EMPLOYEE_USERNAME, EMPLOYEE_NAME, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE);

        // Act
        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                "/employees/" + NEW_EMPLOYEE_USERNAME + "?loggedInUsername=" + OWNER_USERNAME + "&isUpdate=false",
                HttpMethod.PUT, 
                new HttpEntity<>(request),
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(NEW_EMPLOYEE_USERNAME, response.getBody().getUsername());
        assertEquals(EMPLOYEE_NAME, response.getBody().getName());
    }

    @Test
    @Order(4)
    public void testUpdateEmployee() {
        // Arrange
        employeeRepo.save(new Employee(EMPLOYEE_NAME, NEW_EMPLOYEE_USERNAME, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE, true));
        UserAccountRequestDto updatedRequest = new UserAccountRequestDto(NEW_EMPLOYEE_USERNAME, "Updated Name", EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE);

        // Act
        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                "/employees/" + NEW_EMPLOYEE_USERNAME + "?loggedInUsername=" + OWNER_USERNAME + "&isUpdate=true",
                HttpMethod.PUT,
                new HttpEntity<>(updatedRequest),
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    @Order(5)
    public void testGetEmployeeByUsernameWithOwnerPermission() {
        // Act
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/employees/" + EMPLOYEE_USERNAME + "?loggedInUsername=" + OWNER_USERNAME, EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EMPLOYEE_USERNAME, response.getBody().getUsername());
    }

    @Test
    @Order(6)
    public void testDeleteEmployeeByUsername() {
        // Arrange
        employeeRepo.save(new Employee(EMPLOYEE_NAME, EMPLOYEE_USERNAME, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE, true));

        // Act
        ResponseEntity<EmployeeResponseDto> response = client.exchange(
                "/employees/" + EMPLOYEE_USERNAME + "?loggedInUsername=" + OWNER_USERNAME,
                HttpMethod.DELETE,
                null,
                EmployeeResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EMPLOYEE_USERNAME, response.getBody().getUsername());
        // assertTrue(employeeRepo.findByUsername(EMPLOYEE_USERNAME).isEmpty());
    }

    @Test
    @Order(7)
    public void testDeleteEmployeeWithoutPermission() {
        // Arrange
        employeeRepo.save(new Employee(EMPLOYEE_NAME, EMPLOYEE_USERNAME, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE, true));

        // Act
        ResponseEntity<String> response = client.exchange(
                "/employees/" + EMPLOYEE_USERNAME + "?loggedInUsername=" + EMPLOYEE_USERNAME,
                HttpMethod.DELETE,
                null,
                String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}