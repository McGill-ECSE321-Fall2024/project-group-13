package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Address;

@SpringBootTest
public class AddressRepositoryTests {

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        addressRepository.deleteAll();
    }

    @Test
    public void testWriteAndReadAddress() {
        // Arrange
        Address savedAddress = new Address("Sherbrooke St W", "H3A 0G4", 845, "Montreal", "Quebec", "Canada", 0);
        savedAddress = addressRepository.save(savedAddress);
        int savedAddressID = savedAddress.getAddressID();

        // Act
        Address readAddress = addressRepository.findByAddressID(savedAddressID);

        // Assert
        assertNotNull(readAddress);
        assertEquals(savedAddressID, readAddress.getAddressID());
        assertEquals(savedAddress.getStreet(), readAddress.getStreet());
        assertEquals(savedAddress.getPostalCode(), readAddress.getPostalCode());
        assertEquals(savedAddress.getNumber(), readAddress.getNumber());
        assertEquals(savedAddress.getCity(), readAddress.getCity());
        assertEquals(savedAddress.getStateOrProvince(), readAddress.getStateOrProvince());
        assertEquals(savedAddress.getCountry(), readAddress.getCountry());
        assertEquals(savedAddress.getApartmentNo(), readAddress.getApartmentNo());

    }

}
