package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeeRepositoryTests {
    // loading an instance of the local tablescontaining rows of Employee instances from the local database
    @Autowired
    private EmployeeRepository employeeRepo;
    
    // clearing the Employee tables that were loaded in before testing
    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        employeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Employee marrec = new Employee("Marrec", "marrecIsSoCool", "marrec@gmail.com", "12345", "613-241-5312", true);
        // saving the above Employee instances in the cleared Employee tables 
        marrec = employeeRepo.save(marrec);

        //Act
        Employee marrecFromDb = employeeRepo.findByUsername("marrecIsSoCool");

        //Assert
        // ensuring the loaded Employee and Address row instances actually exist in the tables of the local database
        assertNotNull(marrecFromDb);
        // verifying if all the fields of Employee instance that was created before saving it into the local database matches the fields of the loaded row instance of Employee from the table
        assertEquals(marrec.getName(), marrecFromDb.getName());
        assertEquals(marrec.getEmail(), marrecFromDb.getEmail());
        assertEquals(marrec.getPassword(), marrecFromDb.getPassword());
        assertEquals(marrec.getPhoneNumber(), marrecFromDb.getPhoneNumber());
        assertEquals(marrec.getPermissionLevel(), marrecFromDb.getPermissionLevel());
        assertEquals(marrec.getIsActive(), marrecFromDb.getIsActive());
    }
}