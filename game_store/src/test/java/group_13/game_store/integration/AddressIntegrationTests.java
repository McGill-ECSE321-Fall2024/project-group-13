package group_13.game_store.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import group_13.game_store.dto.AddressRequestDto;
import group_13.game_store.dto.AddressResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.repository.CustomerRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AddressIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerRepository customerRepo;

    private String createdCustomerUsername = "bob123";
    private int createdAddressId;
    private int createdBillingAddressId;

    @BeforeAll
    public void setup() {
        // Create a user to ensure that the CUSTOMER_ID exists for testing
        Customer user = new Customer("Bob", createdCustomerUsername, "hi@gmail.com", "password123", "514-222-2222");
        customerRepo.save(user);
    }

    @Test
    @Order(1)
    public void testAddValidAddress() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Pine", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005);
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<AddressResponseDto> response = client.postForEntity(url, request, AddressResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto createdAddress = response.getBody();
        assertNotNull(createdAddress);
        assertEquals("Pine", createdAddress.getStreet());
        assertEquals("H3G1B1", createdAddress.getPostalCode());
        assertEquals("Montreal", createdAddress.getCity());

        // Store the created address ID for further tests
        createdAddressId = createdAddress.getAddressID();
    }

    @Test
    @Order(2)
    public void testGetAddressValidCustomer() {
        // Arrange
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<AddressResponseDto> response = client.getForEntity(url, AddressResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto address = response.getBody();
        assertNotNull(address);
        assertEquals("Pine", address.getStreet());
        assertEquals("Montreal", address.getCity());
    }

    @Test
    @Order(3)
    public void testGetAddressInvalidUser() {
        // Arrange
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=invalidUser";

        // Act
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("Access denied.", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(4)
    public void testAddAddressInvalidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("456 Elm St", "H3Z1X1", 10, "Toronto", "ON", "Canada", 202);
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=invalidUser";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("Access denied.", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(5)
    public void testUpdateAddressValidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Maple", "H4G2C2", 1500, "Montreal", "Quebec", "Canada", 2005);
        String url = "/customers/" + createdCustomerUsername + "/address/" + createdAddressId + "?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Act & Assert - Get the updated address to verify changes
        String getUrl = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=" + createdCustomerUsername;
        ResponseEntity<AddressResponseDto> response = client.getForEntity(getUrl, AddressResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto updatedAddress = response.getBody();
        assertNotNull(updatedAddress);
        assertEquals("Maple", updatedAddress.getStreet());
        assertEquals("H4G2C2", updatedAddress.getPostalCode());
        assertEquals(1500, updatedAddress.getNumber());
    }

    @Test
    @Order(6)
    public void testAddBillingAddressValidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Oak", "H5J3K3", 123, "Toronto", "Ontario", "Canada", 300);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<AddressResponseDto> response = client.postForEntity(url, request, AddressResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto billingAddress = response.getBody();
        assertNotNull(billingAddress);
        assertEquals("Oak", billingAddress.getStreet());
        assertEquals("Toronto", billingAddress.getCity());

        createdBillingAddressId = billingAddress.getAddressID();
    }

    @Test
    @Order(7)
    public void testGetBillingAddressValidUser() {
        // Arrange
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<AddressResponseDto> response = client.getForEntity(url, AddressResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto billingAddress = response.getBody();
        assertNotNull(billingAddress);
        assertEquals("Oak", billingAddress.getStreet());
        assertEquals("Toronto", billingAddress.getCity());
    }

    @Test
    @Order(8)
    public void testUpdateBillingAddressValidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Birch", "H6L4P4", 456, "Vancouver", "British Columbia", "Canada", 400);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address/" + createdBillingAddressId + "?loggedInUsername=" + createdCustomerUsername;
    
        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    
        // Act & Assert - Get the updated billing address to verify changes
        String getUrl = "/customers/" + createdCustomerUsername + "/paymentinfo/address?loggedInUsername=" + createdCustomerUsername;
        ResponseEntity<AddressResponseDto> response = client.getForEntity(getUrl, AddressResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AddressResponseDto updatedBillingAddress = response.getBody();
        assertNotNull(updatedBillingAddress);
        assertEquals("Birch", updatedBillingAddress.getStreet());
        assertEquals("Vancouver", updatedBillingAddress.getCity());
    }

    // Additional Tests to Cover Missing Branches
    @Test
    @Order(9)
    public void testUpdateAddressInvalidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Maple", "H4G2C2", 1500, "Montreal", "Quebec", "Canada", 2005);
        String url = "/customers/" + createdCustomerUsername + "/address/" + createdAddressId + "?loggedInUsername=invalidUser";
    
        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    public void testUpdateBillingAddressInvalidUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Birch", "H6L4P4", 456, "Vancouver", "British Columbia", "Canada", 400);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address/" + createdBillingAddressId + "?loggedInUsername=invalidUser";
    
        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    public void testUpdateAddressNotFound() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Maple", "H4G2C2", 1500, "Montreal", "Quebec", "Canada", 2005);
        String url = "/customers/" + createdCustomerUsername + "/address/9999?loggedInUsername=" + createdCustomerUsername; // Non-existent address ID
    
        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    public void testUpdateBillingAddressNotFound() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Birch", "H6L4P4", 456, "Vancouver", "British Columbia", "Canada", 400);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address/9999?loggedInUsername=" + createdCustomerUsername; // Non-existent billing address ID
    
        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);
    
        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    public void testAddAddressWithMissingFields() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto(null, "H4G2C2", 1500, "Montreal", "Quebec", "Canada", 2005);
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<String> response = client.postForEntity(url, request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(15)
    public void testGetAddressWithDifferentLoggedInUsername() {
        // Arrange
        String url = "/customers/" + createdCustomerUsername + "/address?loggedInUsername=differentUser";

        // Act
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    @Order(16)
    public void testUpdateAddressWithMissingFields() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto(null, "H4G2C2", 1500, "Montreal", "Quebec", "Canada", 2005);
        String url = "/customers/" + createdCustomerUsername + "/address/" + createdAddressId + "?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(17)
    public void testUpdateBillingAddressWithMissingFields() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Birch", "H6L4P4", 0, "", "British Columbia", "Canada", 400);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address/" + createdBillingAddressId + "?loggedInUsername=" + createdCustomerUsername;

        // Act
        ResponseEntity<Void> responseEntity = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(request), Void.class);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(18)
    public void testAddBillingAddressUnauthorizedUser() {
        // Arrange
        AddressRequestDto request = new AddressRequestDto("Birch", "H6L4P4", 123, "Vancouver", "British Columbia", "Canada", 400);
        String url = "/customers/" + createdCustomerUsername + "/paymentinfo/address?loggedInUsername=unauthorizedUser";

        // Act
        ResponseEntity<String> response = client.postForEntity(url, request, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("Access denied.", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }


}
