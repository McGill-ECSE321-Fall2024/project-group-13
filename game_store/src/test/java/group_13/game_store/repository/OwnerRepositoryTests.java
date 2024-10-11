package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import group_13.game_store.model.Owner;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        ownerRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Owner garrett = new Owner("Garret", "garretIsCool", "garret@gmail.com", "1234", "6134211234", 3);
        garrett = ownerRepo.save(garrett);

        //Act
        Owner garrettFromDb = ownerRepo.findByUsername("garretIsCool");

        //Assert
        assertNotNull(garrettFromDb);
        assertEquals(garrett.getName(), garrettFromDb.getName());
        assertEquals(garrett.getEmail(), garrettFromDb.getEmail());
        assertEquals(garrett.getPassword(), garrettFromDb.getPassword());
        assertEquals(garrett.getPhoneNumber(), garrettFromDb.getPhoneNumber());
        assertEquals(garrett.getPermissionLevel(), garrettFromDb.getPermissionLevel());
    }
}
