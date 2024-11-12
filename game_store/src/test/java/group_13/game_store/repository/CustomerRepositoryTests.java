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
import group_13.game_store.model.PaymentInformation;

@SpringBootTest
public class CustomerRepositoryTests {
    // loading an instance of the local tables containing rows of Customer, PaymentInformation, and Address instances from the local database
    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PaymentInformationRepository paymentRepo;

    @Autowired
    private AddressRepository addressRepo;

    // clearing the Customer, PaymentInformation, and Address tables that were loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        customerRepo.deleteAll();
        paymentRepo.deleteAll();
        addressRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Customer nicolas = new Customer("nicolas", "nicolasIsAmazing", "nick@gmail.com", "1234asd", "613-242-1325");
        
        //Create a fake Address and a fake Date that are needed for nicolasPayment so that we can test those
        Address nicolasAddress = new Address("pine", "R5H 7K9", 15, "Monteal", "Quebec", "Canada", 21);
        addressRepo.save(nicolasAddress);

        
        //Create the Payment Information for Nicolas so that we can test the associations are correctly saved in the database
        PaymentInformation nicolasPayment = new PaymentInformation("27122313", "nicolas", Date.valueOf("2026-10-11"), 252, nicolasAddress);
        paymentRepo.save(nicolasPayment);
        

        //Assign the Payment information to nicolas
        nicolas.setPaymentInformation(nicolasPayment);
        nicolas = customerRepo.save(nicolas);

        

        

        //Act
        Customer nicolasFromDb = customerRepo.findByUsername("nicolasIsAmazing");

        //Assert
        // ensuring the loaded Customer row instance actually exists in the table of the local database
        assertNotNull(nicolasFromDb);
        
        //Assert that basic customer attributes are present
        // verifying if all the fields of the Customer instance that was created before saving it into the local database matches the fields of the loaded row instance of Customer from the table
        assertEquals(nicolas.getName(), nicolasFromDb.getName());
        assertEquals(nicolas.getEmail(), nicolasFromDb.getEmail());
        assertEquals(nicolas.getPassword(), nicolasFromDb.getPassword());
        assertEquals(nicolas.getPhoneNumber(), nicolasFromDb.getPhoneNumber());
        assertEquals(nicolas.getPermissionLevel(), nicolasFromDb.getPermissionLevel());

        //Check that the payment information is present
        assertEquals(true, nicolasFromDb.hasPaymentInformation());

        //Check that the Payment Information is equal
        assertEquals(nicolas.getPaymentInformation().getPaymentInfoID(), nicolasFromDb.getPaymentInformation().getPaymentInfoID());
        
    }
}
