// package group_13.game_store.integration;


// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;

// import group_13.game_store.repository.PromotionRepository;
// import group_13.game_store.repository.ReviewRepository;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(OrderAnnotation.class)
// @TestInstance(Lifecycle.PER_CLASS)
// public class PromotionIntegrationTests {
//     @Autowired
//     private TestRestTemplate client;
    
//     @Autowired
//     private PromotionRepository promotionRepository;

//     @Test
//     @Order(1)
//     public void CreateReview() {
//         // Create a review

//     }

//     @Test
//     @Order(2)
//     public void GetReview() {
//         // Get a review

//     }

//     @Test
//     @Order(3)
//     public void UpdateReview() {
//         // Update a review

//     }

//     @Test
//     @Order(4)
//     public void DeleteReview() {
//         // Delete a review

//     }

//     @Test
//     @Order(5)
//     public void GetReviews() {
//         // Get all reviews

//     }
    
// }