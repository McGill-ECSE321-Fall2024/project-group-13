package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.DeliveryInformation;
import group_13.game_store.model.PaymentInformation;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository cutomerRepo;

    @Autowired
    private PaymentInformationRepository paymentRepo;

    @Autowired
    private DeliveryInformationRepository deliveryRepo;

    @Autowired
    private AddressRepository addressRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        cutomerRepo.deleteAll();
        paymentRepo.deleteAll();
        deliveryRepo.deleteAll();
        addressRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Customer nicolas = new Customer("nicolas", "nicolasIsAmazing", "nick@gmail.com", "1234asd", "613-242-1325", 1);
        
        //Create a fake Address and a fake Date that are needed for nicolasPayment and nicolasDelivery so that we can test those
        Address nicolasAddress = new Address("pine", "R5H 7K9", 15, "Monteal", "Quebec", "Canada", 21);
        addressRepo.save(nicolasAddress);
        
        Date expiryDate = new Date(12312412);
        
        //Create the Payment Information, Delivery Information for Nicolas so that we can test the associations are correctly saved in the database
        PaymentInformation nicolasPayment = new PaymentInformation(2712-2313-1242-2312, "nicolas", expiryDate, 252, nicolasAddress);
        paymentRepo.save(nicolasPayment);

        DeliveryInformation nicolasDelivery = new DeliveryInformation("NicolasDelivery", nicolasAddress);
        deliveryRepo.save(nicolasDelivery);

        //Assign both the Delivery information and Payment information to nicolas
        nicolas.setDeliveryInformation(nicolasDelivery);
        nicolas.setPaymentInformation(nicolasPayment);

        nicolas = cutomerRepo.save(nicolas);

        //Act
        Customer nicolasFromDb = cutomerRepo.findByUsername("nicolasIsAmazing");

        //Assert
        assertNotNull(nicolasFromDb);
        
        //Assert that basic customer attributes are present
        assertEquals(nicolas.getName(), nicolasFromDb.getName());
        assertEquals(nicolas.getEmail(), nicolasFromDb.getEmail());
        assertEquals(nicolas.getPassword(), nicolasFromDb.getPassword());
        assertEquals(nicolas.getPhoneNumber(), nicolasFromDb.getPhoneNumber());
        assertEquals(nicolas.getPermissionLevel(), nicolasFromDb.getPermissionLevel());

        //Check that both the payment information and delivery information are present
        assertEquals(true, nicolasFromDb.hasDeliveryInformation());
        assertEquals(true, nicolasFromDb.hasPaymentInformation());

        //Check that both Delivery Information and Payment Information are equal
        assertEquals(nicolas.getDeliveryInformation().getDeliveryInfoID(), nicolasFromDb.getDeliveryInformation().getDeliveryInfoID());
        assertEquals(nicolas.getPaymentInformation().getPaymentInfoID(), nicolasFromDb.getPaymentInformation().getPaymentInfoID());
        
    }
}
