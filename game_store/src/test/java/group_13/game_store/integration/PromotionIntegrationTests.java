package group_13.game_store.integration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

    private int promotion1ID;
    private int promotion2ID;
    private int promotion3ID;
    private int promotion4ID;

    @BeforeEach
    public void setup() {
        Promotion promotion1 = new Promotion(20, Date.valueOf(LocalDate.of(2024, 6, 14)), Date.valueOf(LocalDate.of(2024, 9, 14)), "Summer Sale", "This is a sale for the whole summer woohoo.");
        Promotion promotion2 = new Promotion(30, Date.valueOf(LocalDate.of(2024, 9, 14)), Date.valueOf(LocalDate.of(2024, 12, 14)), "Fall Sale", "This is a sale for the whole fall woohoo.");
        Promotion promotion3 = new Promotion(40, Date.valueOf(LocalDate.of(2024, 12, 14)), Date.valueOf(LocalDate.of(2025, 3, 14)), "Winter Sale", "This is a sale for the whole winter woohoo.");
        Promotion promotion4 = new Promotion(50, Date.valueOf(LocalDate.of(2025, 3, 14)), Date.valueOf(LocalDate.of(2025, 6, 14)), "Spring Sale", "This is a sale for the whole spring woohoo.");

        promotion1 = promotionRepository.save(promotion1);
        promotion2 = promotionRepository.save(promotion2);
        promotion3 = promotionRepository.save(promotion3);
        promotion4 = promotionRepository.save(promotion4);


        promotion1ID = promotion1.getPromotionID();
        promotion2ID = promotion2.getPromotionID();
        promotion3ID = promotion3.getPromotionID();
        promotion4ID = promotion4.getPromotionID();
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
    @Order(3)
    public void  testUpdatePromotion_Success() {
        String loggedInUsername = "owner";
        
        int percentage = 50;
        String description = "Fall sale extra!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 10, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 2, 14)); //Make it a valid promotion
        String title = "Fall Sale plus 40 percent";

        // Update a review
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);
        HttpEntity<PromotionRequestDto> requestEntity = new HttpEntity<>(promotionRequest);


        ResponseEntity<PromotionResponseDto> response = client.exchange(
            "/games/promotions/" + promotion1ID + "?loggedInUsername=" + loggedInUsername,
            HttpMethod.PUT,
            requestEntity,
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
        Promotion savedPromotion = promotionRepository.findById(promotion1ID).get();
        assertNotNull(savedPromotion);
        assertEquals(percentage, savedPromotion.getPercentage());
        assertEquals(description, savedPromotion.getDescription());
        assertEquals(title, savedPromotion.getTitle());
    }

    @Test
    @Order(4)
    public void testUpdatePromotion_UserLacksPermission() {
        //Test with a guest who cant change promotions
        String loggedInUsername = "guest";

        int percentage = 10;
        String description = "Fall sale!";
        Date startDate = Date.valueOf(LocalDate.of(2024, 9, 14));
        Date endDate = Date.valueOf(LocalDate.of(2025, 1, 14)); //Make it a valid promotion
        String title = "Fall Sale";

        // Update a review
        PromotionRequestDto promotionRequest = new PromotionRequestDto(percentage, description, startDate, endDate, title);
        HttpEntity<PromotionRequestDto> requestEntity = new HttpEntity<>(promotionRequest);

        ResponseEntity<String> response = client.exchange(
            "/games/promotions/" + promotion1ID + "?loggedInUsername=" + loggedInUsername,
            HttpMethod.PUT,
            requestEntity,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to update promotions."));
    }

    @Test
    @Order(5)
    public void testDeletePromotion_Success() {
        String loggedInUsername = "owner";

        // Delete a promotion
        ResponseEntity<String> response = client.exchange(
            "/games/promotions/" + promotion1ID + "?loggedInUsername=" + loggedInUsername,
            HttpMethod.DELETE,
            null,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check if the promotion was properly deleted from the database
        boolean exists = promotionRepository.existsById(promotion1ID);
        assertTrue(!exists);

        setup(); //Restore the deleted promotion
    }

    @Test
    @Order(6)
    public void testDeletePromotion_UserLacksPermission() {
        //Test with a guest who cant delete promotions
        String loggedInUsername = "guest";

        // Delete a promotion
        ResponseEntity<String> response = client.exchange(
            "/games/promotions/" + promotion1ID + "?loggedInUsername=" + loggedInUsername,
            HttpMethod.DELETE,
            null,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to delete promotions."));

        // Check if the promotion was not deleted from the database
        boolean exists = promotionRepository.existsById(promotion1ID);
        assertTrue(exists);
    }

    @Test
    @Order(7)
    public void testGetPromotionById_Success() {
        // Get a promotion by its ID
        ResponseEntity<PromotionResponseDto> response = client.getForEntity(
            "/games/promotions/" + promotion1ID,
            PromotionResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Check if the response matches the request
        PromotionResponseDto promotionResponse = response.getBody();
        assertEquals(promotion1ID, promotionResponse.getPromotionID());
        assertEquals(20, promotionResponse.getPercentage());
        assertEquals("Summer Sale", promotionResponse.getTitle());
        assertEquals("This is a sale for the whole summer woohoo.", promotionResponse.getDescription());
    }

    @Test
    @Order(8)
    public void testGetPromotionById_PromotionDoesNotExist() {
        // Get a promotion by its ID
        ResponseEntity<String> response = client.getForEntity(
            "/games/promotions/" + 9999,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Promotion not found."));
    }

    
}