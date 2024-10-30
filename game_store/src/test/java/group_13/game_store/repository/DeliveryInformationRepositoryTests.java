package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import group_13.game_store.model.Address;
import group_13.game_store.model.DeliveryInformation;

@SpringBootTest
public class DeliveryInformationRepositoryTests {
    // loading an instance of the local tables containing rows of Address and DeliveryInformation from the local database
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private DeliveryInformationRepository deliveryInformationRepo;
   
    // clearing the Address and DeliveryInformation, and Address tables that were loaded in before testing
    @BeforeEach
	@AfterEach
	public void clearDatabase() {
		deliveryInformationRepo.deleteAll();
        addressRepo.deleteAll();
	}
    
    @Test
    public void testCreateAndReadDeliveryInformation() {
        // arrange
        Address randomAddress = new Address("randomStreet", "randomPostalCode", 99, "Montreal", "Quebec", "Canada", 99);
        DeliveryInformation randomDeliveryInformation = new DeliveryInformation("randomDeliveryName", randomAddress);
        
        // saving the above DeliveryInformation and Address instances in the cleared Address and DeliveryInformation tables 
        randomAddress = addressRepo.save(randomAddress);
        randomDeliveryInformation = deliveryInformationRepo.save(randomDeliveryInformation);
        
        int randomDeliveryInformationId = randomDeliveryInformation.getDeliveryInfoID();
        
        // act
        DeliveryInformation deliveryInfoFromDB = deliveryInformationRepo.findByDeliveryInfoID(randomDeliveryInformationId);
        
        // assert
        // ensuring the loaded DeliveryInformation and Address row instances actually exist in the tables of the local database
        assertNotNull(deliveryInfoFromDB);
        assertNotNull(deliveryInfoFromDB.getDeliveryAddress());
        // verifying if all the fields of DeliveryInformation instance that was created before saving it into the local database matches the fields of the loaded row instance of DeliveryInformation from the table
        assertEquals(randomDeliveryInformation.getDeliveryInfoID(), deliveryInfoFromDB.getDeliveryInfoID());
        assertEquals(randomDeliveryInformation.getDeliveryName(), deliveryInfoFromDB.getDeliveryName());
        assertEquals(randomDeliveryInformation.getDeliveryAddress().getAddressID(), deliveryInfoFromDB.getDeliveryAddress().getAddressID());
    }
}
