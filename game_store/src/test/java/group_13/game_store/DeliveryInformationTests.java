package group_13.game_store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import group_13.game_store.model.Address;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.model.DeliveryInformation;
import group_13.game_store.repository.DeliveryInformationRepository;

@SpringBootTest
public class DeliveryInformationTests {
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private DeliveryInformationRepository deliveryInformationRepo;
   

    @BeforeEach
	@AfterEach
	public void clearDatabase() {
		deliveryInformationRepo.deleteAll();
        addressRepo.deleteAll();
	}
    
    @Test
    public void testCreateAndReadDeliveryInformation() {
        // arrage
        Address randomAddress = new Address("randomStreet", "randomPostalCode", 99, "Montreal", "Quebec", "Canada", 99);
        DeliveryInformation randomDeliveryInformation = new DeliveryInformation("randomDeliveryName", randomAddress);
        
        randomAddress = addressRepo.save(randomAddress);
        randomDeliveryInformation = deliveryInformationRepo.save(randomDeliveryInformation);
        
        int randomDeliveryInformationId = randomDeliveryInformation.getDeliveryInfoID();

        
        
        // act
        DeliveryInformation deliveryInfoFromDB = deliveryInformationRepo.findByDeliveryInfoID(randomDeliveryInformationId);
        
        // assert
        assertNotNull(deliveryInfoFromDB);
        assertNotNull(deliveryInfoFromDB.getDeliveryAddress());

        assertEquals(randomDeliveryInformation.getDeliveryInfoID(), deliveryInfoFromDB.getDeliveryInfoID());
        assertEquals(randomDeliveryInformation.getDeliveryName(), deliveryInfoFromDB.getDeliveryName());
        
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getAddressID(), deliveryInfoFromDB.getDeliveryAddress().getAddressID());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getApartmentNo(), deliveryInfoFromDB.getDeliveryAddress().getApartmentNo());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getCity(), deliveryInfoFromDB.getDeliveryAddress().getCity());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getCountry(), deliveryInfoFromDB.getDeliveryAddress().getCountry());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getNumber(), deliveryInfoFromDB.getDeliveryAddress().getNumber());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getPostalCode(), deliveryInfoFromDB.getDeliveryAddress().getPostalCode());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getStateOrProvince(), deliveryInfoFromDB.getDeliveryAddress().getStateOrProvince());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getStreet(), deliveryInfoFromDB.getDeliveryAddress().getStreet());
    }
}
