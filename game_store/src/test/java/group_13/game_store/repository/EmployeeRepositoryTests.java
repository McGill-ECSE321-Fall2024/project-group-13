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
    @Autowired
    private EmployeeRepository employeeRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        employeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount(){
        //Arrange
        Employee marrec = new Employee("Marrec", "marrecIsSoCool", "marrec@gmail.com", "12345", "613-241-5312", 2, true);
        marrec = employeeRepo.save(marrec);

        //Act
        Employee marrecFromDb = employeeRepo.findByUsername("marrecIsSoCool");

        //Assert
        assertNotNull(marrecFromDb);
        assertEquals(marrec.getName(), marrecFromDb.getName());
        assertEquals(marrec.getEmail(), marrecFromDb.getEmail());
        assertEquals(marrec.getPassword(), marrecFromDb.getPassword());
        assertEquals(marrec.getPhoneNumber(), marrecFromDb.getPhoneNumber());
        assertEquals(marrec.getPermissionLevel(), marrecFromDb.getPermissionLevel());
        assertEquals(marrec.getIsActive(), marrecFromDb.getIsActive());
    }
}