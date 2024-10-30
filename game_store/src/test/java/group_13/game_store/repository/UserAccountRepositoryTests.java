package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import group_13.game_store.model.UserAccount;

@SpringBootTest
public class UserAccountRepositoryTests { 
    // loading an instance of the local tables containing rows of UserAccount from the local database
    @Autowired
    private UserAccountRepository userAccountRepository;

    // clearing the UserAccount table that was loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        userAccountRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadUserAccount() {
        // Arrange
        UserAccount willAccount = new UserAccount("William", "MajesticMrMoose", "william@gmail.com", "complex1234", "613-4453-421", 2);

        // saving the above UserAccount instance in the cleared UserAccount table 
        willAccount = userAccountRepository.save(willAccount);

        //Act
        UserAccount willAccountFromDb = userAccountRepository.findByUsername("MajesticMrMoose");

        //Assert
        assertNotNull(willAccountFromDb);
        // verifying if all the fields of UserAccount instance that was created before saving it into the local database matches the fields of the loaded row instance of UserAccount from the table
        assertEquals(willAccount.getName(), willAccountFromDb.getName());
        assertEquals(willAccount.getEmail(), willAccountFromDb.getEmail());
        assertEquals(willAccount.getPassword(), willAccountFromDb.getPassword());
        assertEquals(willAccount.getPhoneNumber(), willAccountFromDb.getPhoneNumber());
        assertEquals(willAccount.getPermissionLevel(), willAccountFromDb.getPermissionLevel());
    }

}