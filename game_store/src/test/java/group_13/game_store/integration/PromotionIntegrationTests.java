package group_13.game_store.integration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.PromotionRequestDto;
import group_13.game_store.dto.PromotionResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Promotion;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.ReviewRepository;

import org.springframework.http.HttpStatus;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PromotionIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    
    @Autowired
    private PromotionRepository promotionRepository;

    @AfterAll
    public void clearDatabase() {
        promotionRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreatePromotion_Success() {
        String loggedInUsername = "owner";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<PromotionResponseDto> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            PromotionResponseDto.class
        );

        // Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Check if the response matches the request
        PromotionResponseDto promotionResponse = response.getBody();
        assertEquals(percentage, promotionResponse.getPercentage());
        assertEquals(description, promotionResponse.getDescription());
        assertEquals(title, promotionResponse.getTitle());

        // Check if the promotion was properly saved in the database
        int promotionID = promotionResponse.getPromotionID();

        // Check if the promotion was properly saved in the database
        Promotion savedPromotion = promotionRepository.findById(promotionID).get();
        assertNotNull(savedPromotion);
        assertEquals(percentage, savedPromotion.getPercentage());
        assertEquals(description, savedPromotion.getDescription());
        assertEquals(title, savedPromotion.getTitle());    
    }

    @Test
    @Order(2)
    public void testCreatePromotion_Fail_Percentage() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();

        // Check if the promotion was not saved in the database
        String loggedInUsername = "owner";

        int percentage = 101;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<PromotionResponseDto> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            PromotionResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }

    @Test
    @Order(3)
    public void testCreatePromotion_UserLacksPermission() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();


        String loggedInUsername = "guest";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<String> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to create promotions."));

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }

    @Test
    @Order(4)
    public void testCreatePromotion_MissingTitle() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();

        // Check if the promotion was not saved in the database
        String loggedInUsername = "owner";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<String> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Title must be provided."));

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }

    @Test
    @Order(5)
    public void testCreatePromotion_MissingDescription() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();

        // Check if the promotion was not saved in the database
        String loggedInUsername = "owner";

        int percentage = 10;
        String description = "";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<String> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Description must be provided."));

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }

    @Test
    @Order(6)
    public void testCreatePromotion_MissingStartDate() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();

        // Check if the promotion was not saved in the database
        String loggedInUsername = "owner";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = null;
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<String> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Start date and end date must be provided."));

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }

    @Test
    @Order(7)
    public void testCreatePromotion_MissingEndDate() {
        //Check the current amount of promotions in the database
        long previousPromotionCount = promotionRepository.count();

        // Check if the promotion was not saved in the database
        String loggedInUsername = "owner";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = null; //Make it a valid promotion
        String title = "Fall Sale";

        // Create a promotion request
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);

        ResponseEntity<String> response = client.postForEntity(
            "/games/promotions?loggedInUsername=" + loggedInUsername, 
            promotionRequest, 
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Start date and end date must be provided."));

        // Check if the promotion was not saved in the database
        assertEquals(previousPromotionCount, promotionRepository.count());
    }
    
}