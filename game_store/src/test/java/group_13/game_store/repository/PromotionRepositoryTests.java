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
	@Autowired
	private PromotionRepository repo;

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
		summerSale = repo.save(summerSale);
		int id = summerSale.getPromotionID();

		// Act
		Promotion saleFromDb = repo.findByPromotionID(id);

		// Assert
		assertNotNull(saleFromDb);
		assertEquals(id, saleFromDb.getPromotionID());
        assertEquals(summerSale.getPercentage(), saleFromDb.getPercentage());
		assertEquals(summerSale.getStartDate(), saleFromDb.getStartDate());
		assertEquals(summerSale.getEndDate(), saleFromDb.getEndDate());
		assertEquals(summerSale.getTitle(), saleFromDb.getTitle());
		assertEquals(summerSale.getDescription(), saleFromDb.getDescription());
	}
}