package group_13.game_store.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import group_13.game_store.dto.PaymentInformationRequestDto;
import group_13.game_store.dto.PaymentInformationResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.PaymentInformationRepository;

import org.json.JSONException;
import org.json.JSONObject;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerIntegrationTests {
    // Inject all dependencies for repositories and client
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PaymentInformationRepository paymentInfoRepo;

    @Autowired
    private AddressRepository addressRepo;

    private int addressId;
    private String validUsername;
    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);

    @BeforeAll
    public void setup() {
        // Create objects to use for testing
        Address address = new Address ("Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005);
        addressRepo.save(address);
        addressId = address.getAddressID();

        Customer customer = new Customer("Marrec", "marrec8", "marrec@mail", "GreatPassword#8", "123-456-7890");
        customerRepo.save(customer);

        // Set the values of some variables to be used in testing
        validUsername = "marrec8";
    }

    // Clear database after finished with these tests
    @AfterAll
    public void clearDatabase() {
        customerRepo.deleteAll();
        paymentInfoRepo.deleteAll();
        addressRepo.deleteAll();
    }

    // ****************************************************** GET and POST and PUT CustomerPaymentInfo Test ******************************************************************************* 
    @Test
    @Order(1)
    public void testGetInvalidPaymentInfo() {
        // Arrange
        String url = String.format("/customers/%s/paymentInfo", validUsername);

        // Act
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Parse the response body as JSON
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(404, json.getInt("status"));
            assertEquals("Not Found", json.getString("error"));
            assertEquals("This customer has not set any payment information", json.getString("message"));
        } catch (JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(2)
    public void testCreateValidPaymentInfo() {
        // Arrange
        PaymentInformationRequestDto paymentInfo1 = new PaymentInformationRequestDto("1234567890123456", "Marrec", tomorrow, 123, addressId);
        String url = String.format("/customers/%s/paymentInfo", validUsername);

        // Act
        ResponseEntity<PaymentInformationResponseDto> response = client.postForEntity(url, paymentInfo1, PaymentInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Customer resultCustomer = customerRepo.findByUsername(validUsername);
        
        // First, compare request with response (FOR FRONT END PURPOSES)
        assertEquals(paymentInfo1.getAddressId(), response.getBody().getBillingAddress().getAddressID());
        assertEquals(paymentInfo1.getBillingName(), response.getBody().getBillingName());
        assertEquals(paymentInfo1.getCardNumber(), response.getBody().getCardNumber());
        assertEquals(paymentInfo1.getCvvCode(), response.getBody().getCvvCode());
        assertEquals(paymentInfo1.getExpiryDate(), response.getBody().getExpiryDate());

        // Next, compare the response with the DB (FOR BACK END PURPOSES)
        assertNotNull(resultCustomer.getPaymentInformation());
        assertEquals(resultCustomer.getPaymentInformation().getBillingAddress().getAddressID(), response.getBody().getBillingAddress().getAddressID());
        assertEquals(resultCustomer.getPaymentInformation().getCardNumber(), response.getBody().getCardNumber());
        assertEquals(resultCustomer.getPaymentInformation().getBillingName(), response.getBody().getBillingName());
        assertEquals(resultCustomer.getPaymentInformation().getCvvCode(), response.getBody().getCvvCode());
        assertEquals(resultCustomer.getPaymentInformation().getExpiryDate().toLocalDate(), response.getBody().getExpiryDate());
        assertEquals(resultCustomer.getPaymentInformation().getPaymentInfoID(), response.getBody().getPaymentInfoID());
    }

    @Test
    @Order(3)
    public void testGetValidPaymentInfo() {
        // Arrange
        String url = String.format("/customers/%s/paymentInfo", validUsername);

        // Act
        ResponseEntity<PaymentInformationResponseDto> response = client.getForEntity(url, PaymentInformationResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        
        Customer resultCustomer = customerRepo.findByUsername(validUsername);
        assertNotNull(resultCustomer.getPaymentInformation());
        assertEquals(resultCustomer.getPaymentInformation().getBillingAddress().getAddressID(), response.getBody().getBillingAddress().getAddressID());
        assertEquals(resultCustomer.getPaymentInformation().getCardNumber(), response.getBody().getCardNumber());
        assertEquals(resultCustomer.getPaymentInformation().getBillingName(), response.getBody().getBillingName());
        assertEquals(resultCustomer.getPaymentInformation().getCvvCode(), response.getBody().getCvvCode());
        assertEquals(resultCustomer.getPaymentInformation().getExpiryDate().toLocalDate(), response.getBody().getExpiryDate());
        assertEquals(resultCustomer.getPaymentInformation().getPaymentInfoID(), response.getBody().getPaymentInfoID());
    }

    @Test
    @Order(4)
    public void testUpdateValidPaymentInfo() {
        // Arrange 
        PaymentInformationRequestDto paymentInfo1 = new PaymentInformationRequestDto("0987654321098765", "Mark", tomorrow, 123, addressId);
        Customer customerWithInfo = customerRepo.findByUsername(validUsername);
        int paymentInfoId = customerWithInfo.getPaymentInformation().getPaymentInfoID();

        String url = String.format("/customers/%s/paymentInfo/%d", validUsername, paymentInfoId);

        // Set up the request entity with headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentInformationRequestDto> requestEntity = new HttpEntity<>(paymentInfo1, headers);
        
        // Act
        ResponseEntity<PaymentInformationResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, PaymentInformationResponseDto.class);


        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // First, compare request with response (FOR FRONT END PURPOSES)
        assertEquals(paymentInfo1.getAddressId(), response.getBody().getBillingAddress().getAddressID());
        assertEquals(paymentInfo1.getBillingName(), response.getBody().getBillingName());
        assertEquals(paymentInfo1.getCardNumber(), response.getBody().getCardNumber());
        assertEquals(paymentInfo1.getCvvCode(), response.getBody().getCvvCode());
        assertEquals(paymentInfo1.getExpiryDate(), response.getBody().getExpiryDate());

        // Next, compare the response with the DB (FOR BACK END PURPOSES)
        Customer resultCustomer = customerRepo.findByUsername(validUsername);
        assertNotNull(resultCustomer.getPaymentInformation());
        assertEquals(resultCustomer.getPaymentInformation().getBillingAddress().getAddressID(), response.getBody().getBillingAddress().getAddressID());
        assertEquals(resultCustomer.getPaymentInformation().getBillingName(), response.getBody().getBillingName());
        assertEquals(resultCustomer.getPaymentInformation().getCardNumber(), response.getBody().getCardNumber());
        assertEquals(resultCustomer.getPaymentInformation().getCvvCode(), response.getBody().getCvvCode());
        assertEquals(resultCustomer.getPaymentInformation().getExpiryDate().toLocalDate(), response.getBody().getExpiryDate());
        assertEquals(resultCustomer.getPaymentInformation().getPaymentInfoID(), response.getBody().getPaymentInfoID());
    }

    @Test
    @Order(5)
    public void testCreateInvalidPaymentInfo() {
        // Arrange
        PaymentInformationRequestDto paymentInfo1 = new PaymentInformationRequestDto("1234", "Marrec", tomorrow, 123, addressId);
        String url = String.format("/customers/%s/paymentInfo", validUsername);

        // Act
        ResponseEntity<String> response = client.postForEntity(url, paymentInfo1, String.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());

        // Parse the response body as JSON
        try {
            JSONObject json = new JSONObject(response.getBody());
            assertEquals(400, json.getInt("status"));
            assertEquals("Bad Request", json.getString("error"));
            assertEquals("The customer does not have a valid credit card number", json.getString("message"));
        } catch (JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    @Test
    @Order(6)
    public void testUpdateInvalidPaymentInfo() {
        // Arrange
        PaymentInformationRequestDto paymentInfo1 = new PaymentInformationRequestDto("0987654321098765", "Mark", tomorrow, 1234, addressId);
        Customer customerWithInfo = customerRepo.findByUsername(validUsername);
        int paymentInfoId = customerWithInfo.getPaymentInformation().getPaymentInfoID();

        String url = String.format("/customers/%s/paymentInfo/%d", validUsername, paymentInfoId);

        // Set up the request entity with headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentInformationRequestDto> requestEntity = new HttpEntity<>(paymentInfo1, headers);
        
        // Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
