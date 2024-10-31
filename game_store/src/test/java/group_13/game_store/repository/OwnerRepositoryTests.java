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
    // loading an instance of the local table containing row of Owner from the local database
    @Autowired
    private OwnerRepository ownerRepo;

    // clearing the table containing the owner that was loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        ownerRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Owner garrett = new Owner("Garret", "garretIsCool", "garret@gmail.com", "1234", "6134211234");
        // saving the above Owner instance in the cleared table that contained Owner  
        garrett = ownerRepo.save(garrett);

        //Act
        Owner garrettFromDb = ownerRepo.findByUsername("garretIsCool");

        //Assert
        // ensuring the loaded Owner row instance actually exist in the tables of the local database
        assertNotNull(garrettFromDb);
        // verifying if all the fields of the Owner instance that was created before saving it into the local database matches the fields of the loaded row instance of Owner from the table
        assertEquals(garrett.getName(), garrettFromDb.getName());
        assertEquals(garrett.getEmail(), garrettFromDb.getEmail());
        assertEquals(garrett.getPassword(), garrettFromDb.getPassword());
        assertEquals(garrett.getPhoneNumber(), garrettFromDb.getPhoneNumber());
        assertEquals(garrett.getPermissionLevel(), garrettFromDb.getPermissionLevel());
    }
}
