package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Promotion;

@SpringBootTest
public class PromotionRepositoryTests {
	// loading an instance of the local tables containing rows of Promotion from the local database
	@Autowired
	private PromotionRepository repo;

	// clearing the Promotion table that was loaded in before testing
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		repo.deleteAll();
	}

	@Test
	public void testCreateAndReadPromotion() {
		// Arrange
        Date startDate = Date.valueOf("2024-02-09");
        Date endDate = Date.valueOf("2024-02-10");
        String title = "Summer Sale";
        String description = "Sale for all of this Summer's hottest games";
		Promotion summerSale = new Promotion(10, startDate, endDate, title, description);
		
		// saving the above Promotion instances in the cleared Promotion tables 
		summerSale = repo.save(summerSale);
		
		int id = summerSale.getPromotionID();

		// Act
		Promotion saleFromDb = repo.findByPromotionID(id);

		// Assert
		// ensuring the loaded Promotion row instances actually exist in the tables of the local database
		assertNotNull(saleFromDb);
		// verifying if all the fields of the Promotion instance that was created before saving it into the local database matches the fields of the loaded row instance of Promotion from the table
		assertEquals(id, saleFromDb.getPromotionID());
        assertEquals(summerSale.getPercentage(), saleFromDb.getPercentage());
		assertEquals(summerSale.getStartDate(), saleFromDb.getStartDate());
		assertEquals(summerSale.getEndDate(), saleFromDb.getEndDate());
		assertEquals(summerSale.getTitle(), saleFromDb.getTitle());
		assertEquals(summerSale.getDescription(), saleFromDb.getDescription());
	}
}