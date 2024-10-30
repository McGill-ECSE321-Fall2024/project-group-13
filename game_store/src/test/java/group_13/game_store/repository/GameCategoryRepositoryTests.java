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
    // loading an instance of the local table containing rows of GameCategory instances from the local database
    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    // clearing the GameCategory tables that were loaded in before testing
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
        // saving the above GameCategory instances in the cleared Employee tables 
        savedGameCategory = gameCategoryRepository.save(savedGameCategory);

        // Act
        GameCategory readGameCategory = gameCategoryRepository.findByCategoryID(savedGameCategory.getCategoryID());

        // Assert
        // ensuring the loaded GameCategory row instances actually exist in the tables of the local database
        assertNotNull(readGameCategory);
        // verifying if all the fields of GameCategory instance that was created before saving it into the local database matches the fields of the loaded row instance of GameCategory from the table
        assertEquals(savedGameCategory.getCategoryID(), readGameCategory.getCategoryID());
        assertEquals(savedGameCategory.getDescription(), readGameCategory.getDescription());
        assertEquals(savedGameCategory.getName(), readGameCategory.getName());
        assertEquals(savedGameCategory.getStatus(), readGameCategory.getStatus());

    }

}