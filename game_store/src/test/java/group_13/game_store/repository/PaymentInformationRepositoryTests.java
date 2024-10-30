package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import group_13.game_store.model.Address;
import group_13.game_store.model.PaymentInformation;

@SpringBootTest
public class PaymentInformationRepositoryTests {
    // loading an instance of the local tables containing rows of Address and PaymentInformation from the local database
    @Autowired
    private PaymentInformationRepository paymentInformationRepository;
    @Autowired
    private AddressRepository addressRepository;

    // clearing the Address and PaymentInformation, and Address tables that were loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        paymentInformationRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    public void testWriteAndReadPaymentInformation() {
        // Arrange
        Address savedAddress = new Address("Sherbrooke St W", "H3A 0G4", 845, "Montreal", "Quebec", "Canada", 0);
        savedAddress = addressRepository.save(savedAddress);
        PaymentInformation savedPaymentInformation = new PaymentInformation(123456789, "John Cena",
                Date.valueOf("2024-10-11"), 000, savedAddress);
        
        // saving the above PaymentInformation and Address instances in the cleared Address and PaymentInformation tables 
        savedAddress = addressRepository.save(savedAddress);
        savedPaymentInformation = paymentInformationRepository.save(savedPaymentInformation);

        // Act
        PaymentInformation readPaymentInformation = paymentInformationRepository
                .findByPaymentInfoID(savedPaymentInformation.getPaymentInfoID());

        // Assert
        // ensuring the loaded PaymentInformation and Address row instances actually exist in the tables of the local database
        assertNotNull(readPaymentInformation);
        assertNotNull(readPaymentInformation.getBillingAddress());
        // verifying if all the fields of PaymentInformation instance that was created before saving it into the local database matches the fields of the loaded row instance of PaymentInformation from the table
        assertEquals(savedPaymentInformation.getPaymentInfoID(), readPaymentInformation.getPaymentInfoID());
        assertEquals(savedPaymentInformation.getCardNumber(), readPaymentInformation.getCardNumber());
        assertEquals(savedPaymentInformation.getBillingName(), readPaymentInformation.getBillingName());
        assertEquals(savedPaymentInformation.getExpiryDate(), readPaymentInformation.getExpiryDate());
        assertEquals(savedPaymentInformation.getCvvCode(), readPaymentInformation.getCvvCode());
        assertEquals(savedPaymentInformation.getBillingAddress().getAddressID(), readPaymentInformation.getBillingAddress().getAddressID());

    }

}
