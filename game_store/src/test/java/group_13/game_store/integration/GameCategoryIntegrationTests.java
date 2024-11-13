package group_13.game_store.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.GameCategoryRequestDto;
import group_13.game_store.dto.GameCategoryResponseDto;
import group_13.game_store.dto.GameCategoryListResponseDto;
import group_13.game_store.model.GameCategory.VisibilityStatus;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Customer;
import group_13.game_store.model.GameCategory;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameCategoryIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    private static final String OWNER_USERNAME = "owner";
    private static final String EMPLOYEE_USERNAME = "employee";
    private static final String CUSTOMER_USERNAME = "customer";
    private static final String GUEST_USERNAME = "guest";

    private static final String CATEGORY_NAME = "Action";
    private static final String CATEGORY_DESCRIPTION = "Action games";

    @AfterAll
    public void clearDatabase() {
        gameCategoryRepo.deleteAll();
        employeeRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @BeforeAll
    private void setupAccounts() {
        employeeRepo.save(new Employee("Will", EMPLOYEE_USERNAME, "tim@yahoo.ca", "123", "1234567890", true));
        customerRepo.save(new Customer("Tim", CUSTOMER_USERNAME, "tim@yahoo.ca", "123", "1234567890"));
    }
    
    @Test
    @Order(1)
    public void testCreateGameCategoryWithOwnerPermission() {
        setupAccounts();

        // Arrange
        GameCategoryRequestDto request = new GameCategoryRequestDto(CATEGORY_DESCRIPTION, CATEGORY_NAME);

        // Act
        ResponseEntity <GameCategoryResponseDto> response = client.postForEntity("/categories?loggedInUsername=" + OWNER_USERNAME, request, GameCategoryResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CATEGORY_NAME, response.getBody().getName());
        assertEquals(CATEGORY_DESCRIPTION, response.getBody().getDescription());
    }

    @Test
    @Order(2)
    public void testCreateGameCategoryWithCustomerPermission() {
        // Arrange
        GameCategoryRequestDto request = new GameCategoryRequestDto(CATEGORY_DESCRIPTION, CATEGORY_NAME);

        // Act
        ResponseEntity<String> response = client.postForEntity("/categories?loggedInUsername=" + CUSTOMER_USERNAME, request, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void testGetAllGameCategoriesWithOwnerPermission() {
        // Act
        ResponseEntity<GameCategoryListResponseDto> response = client.getForEntity("/categories?loggedInUsername=" + OWNER_USERNAME, GameCategoryListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getGameCategories());
        assertEquals(1, response.getBody().getGameCategories().size());
        assertEquals(CATEGORY_NAME, response.getBody().getGameCategories().get(0).getName());
    }

    @Test
    @Order(4)
    public void testGetAllGameCategoriesWithGuestPermission() {
        // Act
        ResponseEntity<String> response = client.getForEntity("/categories?loggedInUsername=" + GUEST_USERNAME, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testGetGameCategoryById() {
        // Arrange
        GameCategory category = gameCategoryRepo.findAll().iterator().next();
        int categoryId = category.getCategoryID();

        // Act
        ResponseEntity<GameCategoryResponseDto> response = client.getForEntity("/categories/" + categoryId, GameCategoryResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CATEGORY_NAME, response.getBody().getName());
    }

    @Test
    @Order(6)
    public void testDeleteGameCategoryByIdWithEmployeePermission() {
        // Arrange
        GameCategory category = gameCategoryRepo.findAll().iterator().next();
        int categoryId = category.getCategoryID();

        // Act
        ResponseEntity<Void> response = client.exchange("/categories/" + categoryId + "?loggedInUsername=" + EMPLOYEE_USERNAME, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, gameCategoryRepo.count());
    }

    @Test
    @Order(7)
    public void testDeleteGameCategoryByIdWithCustomerPermission() {
        // Arrange
        GameCategory category = gameCategoryRepo.save(new GameCategory(CATEGORY_NAME, VisibilityStatus.Visible, CATEGORY_DESCRIPTION));
        int categoryId = category.getCategoryID();

        // Act
        ResponseEntity<String> response = client.exchange("/categories/" + categoryId + "?loggedInUsername=" + CUSTOMER_USERNAME, HttpMethod.DELETE, null, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testGetGameCategoryArchiveRequestsWithOwnerPermission() {
        // Act
        ResponseEntity<GameCategoryListResponseDto> response = client.getForEntity("/categories/archive-requests?loggedInUsername=" + OWNER_USERNAME, GameCategoryListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getGameCategories());
        assertEquals(new ArrayList<>(), response.getBody().getGameCategories()); // Should be empty at this point
    }

    @Test
    @Order(9)
    public void testGetGameCategoryArchiveRequestsWithEmployeePermission() {
        // Act
        ResponseEntity<String> response = client.getForEntity("/categories/archive-requests?loggedInUsername=" + EMPLOYEE_USERNAME, String.class);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}