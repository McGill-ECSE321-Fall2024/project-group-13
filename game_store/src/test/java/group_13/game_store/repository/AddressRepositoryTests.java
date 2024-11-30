package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;

@SpringBootTest
public class AddressRepositoryTests {
    // loading an instance of the local table consisting of rows of Address instances from the local database
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PaymentInformationRepository paymentRepo;

    // clearing the Adress table that was loaded in before testing 
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepo.deleteAll();
        paymentRepo.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testWriteAndReadAddress() {
        // Arrange
        Address savedAddress = new Address("Sherbrooke St W", "H3A 0G4", 845, "Montreal", "Quebec", "Canada", 0);
        savedAddress = addressRepository.save(savedAddress);
        Customer nicolas = new Customer("nicolas", "nicolasIsAmazing", "nick@gmail.com", "1234asd", "613-242-1325");
        nicolas.setAddress(savedAddress);
        nicolas = customerRepo.save(nicolas);

        int savedAddressID = savedAddress.getAddressID();

        // Act
        Address readAddress = addressRepository.findByAddressID(savedAddressID);

        // Assert
        // ensuring the loaded Address row instance actually exists in the table of the local database
        assertNotNull(readAddress);
        // verifying if all the fields of Address instance that was created before saving it into the local database matches the fields of the loaded row instance of Address from the table
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
