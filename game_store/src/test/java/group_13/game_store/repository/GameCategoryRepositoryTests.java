package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import group_13.game_store.model.GameCategory;

@SpringBootTest
public class GameCategoryRepositoryTests {

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        gameCategoryRepository.deleteAll();
    }

    @Test
    public void testReadAndWriteGameCategory() {
        // Arrange
        GameCategory savedGameCategory = new GameCategory("This game category is a test",
                GameCategory.VisibilityStatus.Visible, "Test Category");
        savedGameCategory = gameCategoryRepository.save(savedGameCategory);

        // Act
        GameCategory readGameCategory = gameCategoryRepository.findByCategoryID(savedGameCategory.getCategoryID());

        // Assert
        assertNotNull(readGameCategory);
        assertEquals(savedGameCategory.getCategoryID(), readGameCategory.getCategoryID());
        assertEquals(savedGameCategory.getDescription(), readGameCategory.getDescription());
        assertEquals(savedGameCategory.getName(), readGameCategory.getName());
        assertEquals(savedGameCategory.getStatus(), readGameCategory.getStatus());

    }

}