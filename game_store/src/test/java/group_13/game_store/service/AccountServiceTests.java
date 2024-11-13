package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Owner;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;

@SpringBootTest
public class AccountServiceTests {
    // Repo mocks
    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private PaymentInformationRepository paymentRepo;

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private UserAccountRepository userAccountRepo;

    // Service
    @InjectMocks
    private AccountService accountService;

    // Sample data
    private String pass1 = "Short1";
    private String pass2 = "missingcapital1";
    private String pass3 = "MISSINGLOWERCASE2";
    private String pass4 = "MissingNumber";
    private String pass5 = "ValidPassword5";

    private String invalidPhoneNumber = "1234567890";
    private String validPhoneNumber = "987-654-3210";

    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);
    private LocalDate yesterday = today.minusDays(1);

    private Customer customer1 = new Customer("customer1", "cust1", "cust1@mail", pass1, "123-456-7890");

    private Employee employee1 = new Employee("employee1", "emp1", "emp1@mail", "pass1", "123-456-7890", true);

    private Owner owner = new Owner("Owner", "owner", "owner@gmail.com", "pass1", "123-456-7890");

    private Address address1 = new Address("Pine", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005);

    private PaymentInformation paymentInfo1 = new PaymentInformation("1234567890123456", "Marrec", Date.valueOf(tomorrow), 123, address1);







    // Setup mock data
    @BeforeEach
    public void setup() {
        address1.setAddressID(1);
    }

    // **************************************   findUserByUsername and findCustomerByUsername testing *********************************************************************
    @Test
    public void testFindUserByUsernameInvalid() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.findUserByUsername(invalidUsername));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "User with given username not found");

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testFindUserByUsernameSuccess() {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);

        // Act
        UserAccount user = accountService.findUserByUsername(validUsername1);

        // Assert
        assertNotNull(user);
        assertEquals(validUsername1, user.getUsername());

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername1);
    }

    @Test
    public void testFindCustomerByUsernameInvalid() {
        // Arrange
        String invalidUsername = "oops";
        when(customerRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.findCustomerByUsername(invalidUsername));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "User with given username not found");

        // Verify that the repo method was called
        verify(customerRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testFindCustomerByUsernameSuccess() {
        // Arrange
        String validUsername1 = "cust1";
        when(customerRepo.findByUsername(validUsername1)).thenReturn(customer1);

        // Act
        Customer customer = accountService.findCustomerByUsername(validUsername1);

        // Assert
        assertNotNull(customer);
        assertEquals(validUsername1, customer.getUsername());

        // Verify that the repo method was called
        verify(customerRepo, times(1)).findByUsername(validUsername1);
    }
    // *************************** Permission Level Testing (findPermissionLevelByUsername, hasPermission, hasPermissionAtLeast) ******************************************
    @Test
    public void testFindPermissionLevelByUsernameSuccess() {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);
        String validUsername2 = "emp1";
        when(userAccountRepo.findByUsername(validUsername2)).thenReturn(employee1);
        String validUsername3 = "owner";
        when(userAccountRepo.findByUsername(validUsername3)).thenReturn(owner);

        // Act
        int permissionLevel1 = accountService.findPermissionLevelByUsername(validUsername1);
        int permissionLevel2 = accountService.findPermissionLevelByUsername(validUsername2);
        int permissionLevel3 = accountService.findPermissionLevelByUsername(validUsername3);

        // Assert that the correct permission level was returned
        assertEquals(permissionLevel1, 1);
        assertEquals(permissionLevel2,2);
        assertEquals(permissionLevel3, 3);

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername1);
        verify(userAccountRepo, times(1)).findByUsername(validUsername2);
        verify(userAccountRepo, times(1)).findByUsername(validUsername3);
    }

    @Test
    public void testFindPermissionLevelByUsernameFailure() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.findPermissionLevelByUsername(invalidUsername));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "User with given username not found");

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testHasPermissionSuccess() {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);
        String validUsername2 = "emp1";
        when(userAccountRepo.findByUsername(validUsername2)).thenReturn(employee1);

        // Act
        boolean permission1 = accountService.hasPermission(validUsername1, 1);
        boolean permission2 = accountService.hasPermission(validUsername2, 1);

        // Assert (first one should be true, second one should be false)
        assertTrue(permission1);
        assertFalse(permission2);

        // verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername1);
        verify(userAccountRepo, times(1)).findByUsername(validUsername2);
    }

    @Test
    public void testHasPermissionFailure() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.hasPermission(invalidUsername, 1));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "User with given username not found");

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testHasPermissionAtLeastSuccess () {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);
        String validUsername2 = "emp1";
        when(userAccountRepo.findByUsername(validUsername2)).thenReturn(employee1);

        // Act
        boolean permission1 = accountService.hasPermissionAtLeast(validUsername1, 1);
        boolean permission2 = accountService.hasPermissionAtLeast(validUsername1, 2);
        boolean permission3 = accountService.hasPermissionAtLeast(validUsername2, 1);

        // Assert (first one should be true, second one should be false, third one should be true)
        assertTrue(permission1);
        assertFalse(permission2);
        assertTrue(permission3);

        // verify that the repo method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername1);
        verify(userAccountRepo, times(1)).findByUsername(validUsername2);
    }

    @Test
    public void testHasPermissionAtLeastFailure () {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.hasPermissionAtLeast(invalidUsername, 1));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "User with given username not found");

        // Verify that the repo method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    // *************************** changePassword Testing ******************************************
    @Test
    public void testChangePasswordTooShort() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePassword(pass1, "cust1"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must be at least 8 characters long", exception.getReason());

        // Verify that the repository method was NOT called
        verify(userAccountRepo, times(0)).findByUsername("cust1");
    }

    @Test
    public void testChangePasswordNoCap() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePassword(pass2, "username"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 uppercase letter", exception.getReason());

        // Verify that the repository method was NOT called
        verify(userAccountRepo, times(0)).findByUsername("username");
    }

    @Test
    public void testChangePasswordNoLower() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePassword(pass3, "username"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 lowercase letter", exception.getReason());

        // Verify that the repository method was NOT called
        verify(userAccountRepo, times(0)).findByUsername("username");
    }

    @Test
    public void testChangePasswordNoNumber() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePassword(pass4, "username"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 number", exception.getReason());

        // Verify that the repository method was NOT called
        verify(userAccountRepo, times(0)).findByUsername("username");
    }

    @Test
    public void testChangePasswordInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePassword(pass5, invalidUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testChangepasswordSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act
        accountService.changePassword(pass5, validUsername);

        // Assert (the hashed passwords should equal)
        String hashed = accountService.hashPassword(pass5);
        assertEquals(customer1.getPassword(), hashed);

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
            
    }


    // ***************************** changePhoneNumber tests ***********************************
    @Test
    public void testChangePhoneNumberInvalidNumber() {
        // Arrange (none needed)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePhoneNumber(invalidPhoneNumber, "cust1"));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx", exception.getReason());
    }

    @Test
    public void testChangePhoneNumberInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePhoneNumber(validPhoneNumber, invalidUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }
    
    @Test
    public void testChangePhoneNumberSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act
        accountService.changePhoneNumber(validPhoneNumber, validUsername);

        // Assert (the hashed passwords should equal)
        assertEquals(customer1.getPhoneNumber(), validPhoneNumber);

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);

    }

    // ********************* createCustomerAccount tests *********************************
    @Test
    public void testCreateAccountMatchingUsername() {
        // Arrange 
        String matchingUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(matchingUsername)).thenReturn(customer1);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", matchingUsername, validEmail, pass5, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("An account already exists with this username, please select a different username", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(matchingUsername);
        verify(userAccountRepo, times(0)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountMatchingEmail() {
        // Arrange 
        String validUsername = "marrec8";
        String matchingEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(matchingEmail)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, matchingEmail, pass5, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("An account already exists with this email, please enter a different email", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(matchingEmail);
    }

    @Test
    public void testCreateAccountShortPassword() {
        // Arrange 
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass1, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must be at least 8 characters long", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountNoCapsPassword() {
        // Arrange
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass2, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 uppercase letter", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountNoLowerPassword() {
        // Arrange
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass3, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 lowercase letter", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountNoNumberPassword() {
        // Arrange
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass4, "123-456-7890"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The password must contain at least 1 number", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountInvalidPhone() {
        // Arrange
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        String invalidPhoneNumber = "1234567890";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass5, invalidPhoneNumber));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testCreateAccountInvalidEmail() {
        // Arrange
        String validUsername = "marrec8";
        String invalidEmail = "marrecmail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(invalidEmail)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.createCustomerAccount("Marrec", validUsername, invalidEmail, pass5, "123-456-7890"));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email is invalid, please enter a valid email with an @ symbol", exception.getReason());

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(invalidEmail);
    }

    @Test
    public void testCreateAccountSuccess() {
        // Arrange
        String validUsername = "marrec8";
        String validEmail = "marrec@mail";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(null);
        when(userAccountRepo.findByEmail(validEmail)).thenReturn(null);

        // Act
        Customer customer = accountService.createCustomerAccount("Marrec", validUsername, validEmail, pass5, "123-456-7890");
        
        // Assert
        assertNotNull(customer);
        assertEquals(customer.getEmail(), validEmail);
        assertEquals(customer.getName(), "Marrec");
        assertEquals(customer.getUsername(), validUsername);
        assertEquals(customer.getPassword(), accountService.hashPassword(pass5));
        assertEquals(customer.getPhoneNumber(), "123-456-7890");

        // Verify that the repository methods were called
        verify(userAccountRepo, times(1)).findByUsername(validUsername);
        verify(userAccountRepo, times(1)).findByEmail(validEmail);
    }

    // ***************************************** loginToAccount tests ****************************************************
    @Test
    public void testLoginToAccountInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.loginToAccount(invalidUsername, pass5));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testLoginToAccountInvalidPassword() {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.loginToAccount(validUsername1, pass5));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Password does not match the account with the given username", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(validUsername1);
    } 

    @Test
    public void testLoginToAccountSuccess() {
        // Arrange
        String validUsername1 = "cust1";
        when(userAccountRepo.findByUsername(validUsername1)).thenReturn(customer1);

        // Act
        customer1.setPassword(accountService.hashPassword(customer1.getPassword()));
        System.out.println(customer1.getPassword());
        String loggedInUsername = accountService.loginToAccount(validUsername1, pass1);

        // Assert
        assertEquals(validUsername1, loggedInUsername);
    }

    // *************************************    changePaymentInfoTests ********************************************
    @Test
    public void testChangePaymentInfoInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePaymentInfo(invalidUsername, "1234567890", "Marrec", Date.valueOf(tomorrow), 123, address1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testChangePaymentInfoInvalidUserType() {
        // Arrange
        String validUsername = "emp1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(employee1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePaymentInfo(validUsername, "1234567890", "Marrec", Date.valueOf(tomorrow), 123, address1));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The given user is not a Customer, and therefore does not have payment information", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testChangePaymentInfoInvalidInfo() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.changePaymentInfo(validUsername, "1234567890123456", null, Date.valueOf(tomorrow), 123, address1));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        // We don't know which specific error it is so don't check it

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testChangePaymentInfoNewSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act
        boolean changed = accountService.changePaymentInfo(validUsername, "1234567890123456", "Marrec", Date.valueOf(tomorrow), 123, address1);

        // Assert
        assertTrue(changed);
        assertNotNull(customer1.getPaymentInformation());
        assertNotNull(customer1.getPaymentInformation().getBillingAddress());
        assertEquals(customer1.getPaymentInformation().getBillingName(), "Marrec");
        assertEquals(customer1.getPaymentInformation().getCardNumber(), "1234567890123456");
        assertEquals(customer1.getPaymentInformation().getCvvCode(), 123);
        assertEquals(customer1.getPaymentInformation().getExpiryDate(), Date.valueOf(tomorrow));

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);        
    }

    @Test
    public void testChangePaymentInfoUpdateSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        customer1.setPaymentInformation(paymentInfo1);

        // Act
        boolean changed = accountService.changePaymentInfo(validUsername, "1234567890123456", "Marrec", Date.valueOf(tomorrow), 123, address1);
    
        // Assert
        assertTrue(changed);
        assertNotNull(customer1.getPaymentInformation());
        assertNotNull(customer1.getPaymentInformation().getBillingAddress());
        assertEquals(customer1.getPaymentInformation().getBillingName(), "Marrec");
        assertEquals(customer1.getPaymentInformation().getCardNumber(), "1234567890123456");
        assertEquals(customer1.getPaymentInformation().getCvvCode(), 123);
        assertEquals(customer1.getPaymentInformation().getExpiryDate(), Date.valueOf(tomorrow));

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);     
    }


    // ******************************* getPaymentInformationByCustomerUsername tests ******************************************
    @Test
    public void testGetPaymentInfoByUsernameInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(customerRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getPaymentInformationByCustomerUsername(invalidUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("A customer with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testGetPaymentInfoByUsernameNoPaymentInfo() {
        // Arrange
        String validUsername = "cust1";
        when(customerRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getPaymentInformationByCustomerUsername(validUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("This customer has not set any payment information", exception.getReason());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(validUsername);
    }

    @Test
    public void testGetPaymentInfoByUsernameSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(customerRepo.findByUsername(validUsername)).thenReturn(customer1);
        customer1.setPaymentInformation(paymentInfo1);

        // Act
        PaymentInformation paymentInfo = accountService.getPaymentInformationByCustomerUsername(validUsername);

        // Assert
        assertNotNull(paymentInfo);
        assertEquals(paymentInfo.getBillingAddress().getAddressID(), paymentInfo1.getBillingAddress().getAddressID());
        assertEquals(paymentInfo.getBillingName(), paymentInfo1.getBillingName());
        assertEquals(paymentInfo.getCardNumber(), paymentInfo1.getCardNumber());
        assertEquals(paymentInfo.getCvvCode(), paymentInfo1.getCvvCode());
        assertEquals(paymentInfo.getExpiryDate(), paymentInfo1.getExpiryDate());
        assertEquals(paymentInfo.getPaymentInfoID(), paymentInfo1.getPaymentInfoID());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(validUsername);
    }

    // ************************************ getPaymentInfoIdByUsername tests *************************************************
    @Test
    public void testGetPaymentInfoIdByUsernameInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(customerRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getPaymentInfoIdByUsername(invalidUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("A customer with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testGetPaymentInfoIdByUsernameNoPaymentInfo() {
        // Arrange
        String validUsername = "cust1";
        when(customerRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getPaymentInfoIdByUsername(validUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("This customer has not set any payment information", exception.getReason());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(validUsername);
    }

    @Test
    public void testGetPaymentInfoIdByUsernameSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(customerRepo.findByUsername(validUsername)).thenReturn(customer1);
        customer1.setPaymentInformation(paymentInfo1);

        // Act
        int paymentInfoId = accountService.getPaymentInfoIdByUsername(validUsername);

        // Assert
        assertEquals(paymentInfoId, paymentInfo1.getPaymentInfoID());

        // Verify that the repository method was called
        verify(customerRepo, times(1)).findByUsername(validUsername);
    }
    

    // **************************************** getAddressById tests ***************************************************
    @Test
    public void testGetAddressByIdInvalidId() {
        // Arrange
        int invalidId = 0;
        when(addressRepo.findByAddressID(invalidId)).thenReturn(null);

        // Act and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.getAddressById(invalidId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("There is no address with the given id", exception.getReason());

        // Verify that the repository method was called
        verify(addressRepo, times(1)).findByAddressID(invalidId);
    }

    @Test
    public void testGetAddressByIdSuccess() {
        // Arrange
        int validId = 1;
        when(addressRepo.findByAddressID(validId)).thenReturn(address1);

        // Act
        Address address = accountService.getAddressById(validId);

        // Assert
        assertNotNull(address);
        assertEquals(address.getAddressID(), address1.getAddressID());
        assertEquals(address.getApartmentNo(), address1.getApartmentNo());
        assertEquals(address.getCity(), address1.getCity());
        assertEquals(address.getCountry(), address1.getCountry());
        assertEquals(address.getNumber(), address1.getNumber());
        assertEquals(address.getPostalCode(), address1.getPostalCode());
        assertEquals(address.getStateOrProvince(), address1.getStateOrProvince());
        assertEquals(address.getStreet(), address1.getStreet());

        // Verify that the repository method was called
        verify(addressRepo, times(1)).findByAddressID(validId);
    }

    // ***************************************** validatePaymentInfo ***************************************************
    @Test
    public void testValidatePaymentInfoInvalidCardNumber() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.validatePaymentInfo("123", Date.valueOf(tomorrow), 123, address1, "Marrec"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The customer does not have a valid credit card number", exception.getReason());

    }

    @Test
    public void testValidatePaymentInfoInvalidCvv() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.validatePaymentInfo("1234567890123456", Date.valueOf(tomorrow), 12, address1, "Marrec"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The customer does not have a valid cvv code", exception.getReason());
    }

    @Test
    public void testValidatePaymentInfoInvalidExpiry() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.validatePaymentInfo("1234567890123456", Date.valueOf(yesterday), 123, address1, "Marrec"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The credit card with the following information is expired", exception.getReason());
    }

    @Test
    public void testValidatePaymentInfoInvalidName() {
        // Arrange (NOTHING IN THIS CASE)

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> accountService.validatePaymentInfo("1234567890123456", Date.valueOf(tomorrow), 123, address1, null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a cardholder name", exception.getReason());
    }

    @Test
    public void testValidatePaymentInfoSuccess() {
        // Arrange (NOTHING IN THIS CASE)

        // Act
        boolean valid = accountService.validatePaymentInfo("1234567890123456", Date.valueOf(tomorrow), 123, address1, "Marrec");
   
        // Assert
        assertTrue(valid);
    }

    // ************************ addAddressToUser tests **************************************************
    @Test
    public void testAddAddressToUserInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(invalidUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testAddAddressToUserInvalidUserType() {
        // Arrange
        String validUsername = "emp1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(employee1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The given user is not a Customer, so you cannot add an address", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidStreet() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, null, "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a street name", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidPostalCode() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "HGG1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid postal code: A#A#A#", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidNumber() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", -1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid house number", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidCity() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, null, "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a city", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidStateOrProvince() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", null, "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a state/province", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidCountry() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", null, 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a country", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserInvalidApartmentNumber() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", -1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid apartment number", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testAddAddressToUserSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act
        boolean added = accountService.addAddressToUser(validUsername, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005);

        // Assert
        assertTrue(added);
        assertNotNull(customer1.getAddress());
        assertEquals(customer1.getAddress().getApartmentNo(), 1005);
        assertEquals(customer1.getAddress().getCity(), "Montreal");
        assertEquals(customer1.getAddress().getCountry(), "Canada");
        assertEquals(customer1.getAddress().getNumber(), 1400);
        assertEquals(customer1.getAddress().getPostalCode(), "H3G1B1");
        assertEquals(customer1.getAddress().getStateOrProvince(), "Quebec");
        assertEquals(customer1.getAddress().getStreet(), "Sherbrooke");

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    // ***************************************** updateAddressForUser tests *********************************************************
    @Test
    public void testUpdateAddressForUserInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(invalidUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidUserType() {
        // Arrange
        String validUsername = "emp1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(employee1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The given user is not a Customer, and therefore does not have an address to update", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserNoAddress() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(2)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No address exists with the given address ID", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);

    }

    @Test
    public void testUpdateAddressForUserInvalidStreet() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, null, "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a street name", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidPostalCode() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "HGG1B1", 1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid postal code: A#A#A#", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidNumber() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", -1400, "Montreal", "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid house number", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidCity() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, null, "Quebec", "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a city", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidStateOrProvince() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", null, "Canada", 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a state/province", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserInvalidCountry() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", null, 1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a country", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void tesUpdateAddressForUserInvalidApartmentNumber() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", -1005));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You must enter a valid apartment number", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testUpdateAddressForUserSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        when(addressRepo.findByAddressID(1)).thenReturn(address1);
        customer1.setAddress(address1);

        // Act 
        boolean updated = accountService.updateAddressForUser(validUsername, 1, "Sherbrooke", "H3G1B1", 1400, "Montreal", "Quebec", "Canada", 1005);

        // Assert
        assertTrue(updated);
        Address address = customer1.getAddress();
        assertNotNull(address);
        assertEquals(address.getApartmentNo(), 1005);
        assertEquals(address.getCity(), "Montreal");
        assertEquals(address.getCountry(), "Canada");
        assertEquals(address.getNumber(), 1400);
        assertEquals(address.getPostalCode(), "H3G1B1");
        assertEquals(address.getStateOrProvince(), "Quebec");
        assertEquals(address.getStreet(), "Sherbrooke");
    }

    // ******************************************** getAddressForUser tests *******************************************
    @Test
    public void testGetAddressForUserInvalidUsername() {
        // Arrange
        String invalidUsername = "oops";
        when(userAccountRepo.findByUsername(invalidUsername)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getAddressForUser(invalidUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with given username not found", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(1)).findByUsername(invalidUsername);
    }

    @Test
    public void testGetAddressForUserInvalidUserType() {
        // Arrange
        String validUsername = "emp1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(employee1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getAddressForUser(validUsername));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("The given user is not a Customer, and therefore does not have an address to get", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testGetAddressForUserNoAddress() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> accountService.getAddressForUser(validUsername));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("The customer does not have an address on their account", exception.getReason());

        // Verify that the repository method was called
        verify(userAccountRepo, times(2)).findByUsername(validUsername);
    }

    @Test
    public void testGetAddressForUserSuccess() {
        // Arrange
        String validUsername = "cust1";
        when(userAccountRepo.findByUsername(validUsername)).thenReturn(customer1);
        customer1.setAddress(address1);

        // Act
        Address address = accountService.getAddressForUser(validUsername);

        // Assert
        assertNotNull(address);
        assertEquals(address.getAddressID(), address1.getAddressID());
        assertEquals(address.getApartmentNo(), address1.getApartmentNo());
        assertEquals(address.getCity(), address1.getCity());
        assertEquals(address.getCountry(), address1.getCountry());
        assertEquals(address.getNumber(), address1.getNumber());
        assertEquals(address.getPostalCode(), address1.getPostalCode());
        assertEquals(address.getStateOrProvince(), address1.getStateOrProvince());
        assertEquals(address.getStreet(), address1.getStreet());
    }
}
