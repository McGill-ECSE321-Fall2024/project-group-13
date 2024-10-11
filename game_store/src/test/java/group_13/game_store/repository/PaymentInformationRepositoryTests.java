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

    @Autowired
    private PaymentInformationRepository paymentInformationRepository;
    @Autowired
    private AddressRepository addressRepository;

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
        savedAddress = addressRepository.save(savedAddress);
        savedPaymentInformation = paymentInformationRepository.save(savedPaymentInformation);

        // Act
        PaymentInformation readPaymentInformation = paymentInformationRepository
                .findByPaymentInfoID(savedPaymentInformation.getPaymentInfoID());

        // Assert
        assertNotNull(readPaymentInformation);
        assertEquals(savedPaymentInformation.getPaymentInfoID(), readPaymentInformation.getPaymentInfoID());
        assertEquals(savedPaymentInformation.getCardNumber(), readPaymentInformation.getCardNumber());
        assertEquals(savedPaymentInformation.getBillingName(), readPaymentInformation.getBillingName());
        assertEquals(savedPaymentInformation.getExpiryDate(), readPaymentInformation.getExpiryDate());
        assertEquals(savedPaymentInformation.getCvvCode(), readPaymentInformation.getCvvCode());
        assertNotNull(readPaymentInformation.getBillingAddress());
        assertEquals(savedPaymentInformation.getBillingAddress().getAddressID(), readPaymentInformation.getBillingAddress().getAddressID());

    }

}
