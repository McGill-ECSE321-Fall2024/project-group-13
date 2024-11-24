package group_13.game_store.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Address;
import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Order;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class PaymentServiceTests {

    // Repository Mocks
    @Mock
    private OrderRepository orderRepo;

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private GameCopyRepository gameCopyRepo;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private CartItemRepository cartItemRepo;

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private UserAccountRepository userAccountRepo;

    @Mock
    private PaymentInformationRepository paymentInfoRepo;

    // Service under test
    @InjectMocks
    private PaymentService paymentService;

    // Sample data
    private Customer customer1;
    private PaymentInformation paymentInfoValid;
    private PaymentInformation paymentInfoInvalidPostalCode;
    private PaymentInformation paymentInfoInvalidCardNumber;
    private PaymentInformation paymentInfoInvalidCVV;
    private PaymentInformation paymentInfoExpiredCard;

    private Address addressValid;
    private Address addressInvalidPostalCode;

    private Game game1;
    private GameCategory gameCategory;
    private CartItem cartItem1;

    private LocalDate today;

    @BeforeEach
    public void setup() {
        // Initialize dates
        today = LocalDate.now();

        // Initialize addresses
        addressValid = new Address("123 Test St", "A1A1A1", 1, "TestCity", "TestProvince", "TestCountry", 1);

        addressInvalidPostalCode = new Address("123 Test St", "INVALID", 1, "TestCity", "TestProvince", "TestCountry",
                1);

        // Initialize payment information
        paymentInfoValid = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.plusMonths(1)), 123,
                addressValid);

        paymentInfoInvalidPostalCode = new PaymentInformation("1234567812345678", "Tim",
                Date.valueOf(today.plusMonths(1)), 123, addressInvalidPostalCode);

        paymentInfoInvalidCardNumber = new PaymentInformation("12345678", "Tim", Date.valueOf(today.plusMonths(1)), 123,
                addressValid);

        paymentInfoInvalidCVV = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.plusMonths(1)),
                12, addressValid);

        paymentInfoExpiredCard = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.minusMonths(1)),
                123, addressValid);

        // Initialize customer
        customer1 = new Customer("tim", "customer1", "tim@yahoo", "123", "6047640574");
        customer1.setPaymentInformation(paymentInfoValid);

        gameCategory = new GameCategory("Boring Category", GameCategory.VisibilityStatus.Visible, "Boring");

        // Initialize games
        game1 = new Game("Game1", "A Game", "anImg", 10, 59.99, "R", Game.VisibilityStatus.Visible, gameCategory);

        // Initialize cart items
        cartItem1 = new CartItem(new CartItem.Key(customer1, game1), 1);
    }

    // Test when purchaseCart is successful
    @Test
    public void testPurchaseCartSuccess() {
        // Arrange
        String username = "customer1";
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(List.of(cartItem1));
        when(gameRepo.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameCopyRepo.save(any(GameCopy.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = paymentService.purchaseCart(username);

        // Assert
        assertNotNull(result, "Order should be created successfully");
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verify(gameRepo, times(1)).save(game1);
        verify(gameCopyRepo, times(1)).save(any(GameCopy.class));
        verify(orderRepo, times(2)).save(any(Order.class));
        verify(cartItemRepo, times(1)).deleteByKeyCustomerAccountUsername(username);
    }

    // Test when customer does not exist
    @Test
    public void testPurchaseCartCustomerNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(customerRepo.findByUsername(username)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("404 NOT_FOUND \"No such customer with username exists\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when paymentInformation is null
    @Test
    public void testPurchaseCartPaymentInfoNull() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(null);
        when(customerRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer does not have valid payment information\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when postal code is invalid
    @Test
    public void testPurchaseCartInvalidPostalCode() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(paymentInfoInvalidPostalCode);
        when(customerRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer address postal code information is not valid\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when card number is invalid
    @Test
    public void testPurchaseCartInvalidCardNumber() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(paymentInfoInvalidCardNumber);
        when(customerRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer does not have valid credit card number\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when CVV code is invalid
    @Test
    public void testPurchaseCartInvalidCVV() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(paymentInfoInvalidCVV);
        when(customerRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer does not have valid CVV code\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when card is expired
    @Test
    public void testPurchaseCartExpiredCard() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(paymentInfoExpiredCard);
        when(customerRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer credit card is expired\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(cartItemRepo, gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when cart is empty
    @Test
    public void testPurchaseCartEmptyCart() {
        // Arrange
        String username = "customer1";
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(List.of());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"Customer cart is empty\"", exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verifyNoMoreInteractions(gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when not enough stock
    @Test
    public void testPurchaseCartNotEnoughStock() {
        // Arrange
        String username = "customer1";
        game1.setStock(5);
        CartItem cartItemInsufficientStock = new CartItem(new CartItem.Key(customer1, game1), 10); // quantity > stock
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(List.of(cartItemInsufficientStock));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.purchaseCart(username);
        });
        assertEquals("400 BAD_REQUEST \"There is not enough stock of Game1 to complete purchase\"",
                exception.getMessage());
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verifyNoMoreInteractions(gameRepo, gameCopyRepo, orderRepo);
    }

    // Test getCartItems success
    @Test
    public void testGetCartItemsSuccess() {
        // Arrange
        when(cartItemRepo.findByKeyCustomerAccountUsername(customer1.getUsername()))
                .thenReturn(List.of(cartItem1));

        // Act
        CartItem[] result = paymentService.getCartItems(customer1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(cartItem1, result[0]);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(customer1.getUsername());
    }

    // Test getCartItems when no items
    @Test
    public void testGetCartItemsNoItems() {
        // Arrange
        when(cartItemRepo.findByKeyCustomerAccountUsername(customer1.getUsername())).thenReturn(List.of());

        // Act
        CartItem[] result = paymentService.getCartItems(customer1);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(customer1.getUsername());
    }

    // Test clearCart success
    @Test
    public void testClearCartSuccess() {
        // Arrange
        doNothing().when(cartItemRepo).deleteByKeyCustomerAccountUsername(customer1.getUsername());

        // Act
        paymentService.clearCart(customer1);

        // Assert
        verify(cartItemRepo, times(1)).deleteByKeyCustomerAccountUsername(customer1.getUsername());
    }

    // Test addAddressToUser success
    @Test
    public void testAddAddressToUserSuccess() {
        // Arrange
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        when(addressRepo.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentInfoRepo.save(any(PaymentInformation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        boolean result = paymentService.addAddressToUser(username, "New Street", "A1A1A1", 123, "City", "State",
                "Country", 1);

        // Assert
        assertTrue(result);
        verify(userAccountRepo, times(1)).findByUsername(username);
        verify(addressRepo, times(1)).save(any(Address.class));
        verify(paymentInfoRepo, times(1)).save(any(PaymentInformation.class));
    }

    // Test addAddressToUser when user not found
    @Test
    public void testAddAddressToUserUserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(userAccountRepo.findByUsername(username)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "New Street", "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("404 NOT_FOUND \"User not found or not a customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(addressRepo, paymentInfoRepo);
    }

    // Test addAddressToUser when invalid address
    @Test
    public void testAddAddressToUserInvalidAddress() {
        // Arrange
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, null, "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(addressRepo, paymentInfoRepo);
    }

    // Test updateAddressForUser success
    @Test
    public void testUpdateAddressForUserSuccess() {
        // Arrange
        String username = "customer1";
        Address existingAddress = addressValid;
        PaymentInformation paymentInfo = paymentInfoValid;
        paymentInfo.setBillingAddress(existingAddress);
        customer1.setPaymentInformation(paymentInfo);

        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        when(addressRepo.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        boolean result = paymentService.updateAddressForUser(username, existingAddress.getAddressID(), "Updated Street",
                "B2B2B2", 456, "New City", "New State", "New Country", 2);

        // Assert
        assertTrue(result);
        verify(userAccountRepo, times(1)).findByUsername(username);
        verify(addressRepo, times(1)).save(existingAddress);
    }

    // Test updateAddressForUser when user not found
    @Test
    public void testUpdateAddressForUserUserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(userAccountRepo.findByUsername(username)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.updateAddressForUser(username, 1, "Street", "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("404 NOT_FOUND \"User not found or not a customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(addressRepo);
    }

    // Test getAddressForUser success
    @Test
    public void testGetAddressForUserSuccess() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(paymentInfoValid);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);

        // Act
        Address result = paymentService.getAddressForUser(username);

        // Assert
        assertNotNull(result);
        assertEquals(addressValid, result);
        verify(userAccountRepo, times(1)).findByUsername(username);
    }

    // Test getAddressForUser when no address
    @Test
    public void testGetAddressForUserNoAddress() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(null);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.getAddressForUser(username);
        });
        assertEquals("404 NOT_FOUND \"No billing address found for this customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
    }

    // Test getAddressForUser when user not found
    @Test
    public void testGetAddressForUserUserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(userAccountRepo.findByUsername(username)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.getAddressForUser(username);
        });
        assertEquals("404 NOT_FOUND \"User not found or not a customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
    }

    // Additional test to cover the branch where paymentInfo is null in addAddressToUser
    @Test
    public void testAddAddressToUserNoPaymentInfo() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(null);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"No payment information found for the customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(addressRepo, paymentInfoRepo);
    }

    // Additional test to cover the branch where address does not match in updateAddressForUser
    @Test
    public void testUpdateAddressForUserAddressMismatch() {
        // Arrange
        String username = "customer1";
        Address existingAddress = addressValid;
        PaymentInformation paymentInfo = paymentInfoValid;
        paymentInfo.setBillingAddress(existingAddress);
        customer1.setPaymentInformation(paymentInfo);

        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.updateAddressForUser(username, existingAddress.getAddressID() + 1, "Street", "A1A1A1", 123,
                    "City", "State", "Country", 1);
        });
        assertEquals("404 NOT_FOUND \"No matching address found for the customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoMoreInteractions(addressRepo);
    }

    // Test addAddressToUser with null street
    @Test
    public void testAddAddressToUserInvalidStreetNull() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, null, "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with empty street
    @Test
    public void testAddAddressToUserInvalidStreetEmpty() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "", "A1A1A1", 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with null postalCode
    @Test
    public void testAddAddressToUserInvalidPostalCodeNull() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", null, 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with empty postalCode
    @Test
    public void testAddAddressToUserInvalidPostalCodeEmpty() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "", 123, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with number <= 0
    @Test
    public void testAddAddressToUserInvalidNumber() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 0, "City", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with null city
    @Test
    public void testAddAddressToUserInvalidCityNull() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, null, "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with empty city
    @Test
    public void testAddAddressToUserInvalidCityEmpty() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "", "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with null stateOrProvince
    @Test
    public void testAddAddressToUserInvalidStateNull() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", null, "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with empty stateOrProvince
    @Test
    public void testAddAddressToUserInvalidStateEmpty() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", "", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with null country
    @Test
    public void testAddAddressToUserInvalidCountryNull() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", "State", null, 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with empty country
    @Test
    public void testAddAddressToUserInvalidCountryEmpty() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", "State", "", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Test addAddressToUser with apartmentNo <= 0
    @Test
    public void testAddAddressToUserInvalidApartmentNo() {
        String username = "customer1";
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.addAddressToUser(username, "Street", "A1A1A1", 123, "City", "State", "Country", 0);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo, paymentInfoRepo);
    }
    // Similar tests for updateAddressForUser
    // Test updateAddressForUser with invalid street
    @Test
    public void testUpdateAddressForUserInvalidStreetNull() {
        String username = "customer1";
        Address existingAddress = addressValid;
        PaymentInformation paymentInfo = paymentInfoValid;
        paymentInfo.setBillingAddress(existingAddress);
        customer1.setPaymentInformation(paymentInfo);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.updateAddressForUser(username, existingAddress.getAddressID(), null, "A1A1A1", 123, "City",
                    "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo);
    }
    // Continue with tests for other invalid fields in updateAddressForUser...
    // Test updateAddressForUser with empty street
    @Test
    public void testUpdateAddressForUserInvalidStreetEmpty() {
        String username = "customer1";
        Address existingAddress = addressValid;
        PaymentInformation paymentInfo = paymentInfoValid;
        paymentInfo.setBillingAddress(existingAddress);
        customer1.setPaymentInformation(paymentInfo);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.updateAddressForUser(username, existingAddress.getAddressID(), "", "A1A1A1", 123, "City",
                    "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo);
    }
    // Test updateAddressForUser with null postalCode
    @Test
    public void testUpdateAddressForUserInvalidPostalCodeNull() {
        String username = "customer1";
        Address existingAddress = addressValid;
        PaymentInformation paymentInfo = paymentInfoValid;
        paymentInfo.setBillingAddress(existingAddress);
        customer1.setPaymentInformation(paymentInfo);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.updateAddressForUser(username, existingAddress.getAddressID(), "Street", null, 123, "City",
                    "State", "Country", 1);
        });
        assertEquals("400 BAD_REQUEST \"Invalid address information.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
        verifyNoInteractions(addressRepo);
    }

    @Test
    public void testPurchaseCartUnexpectedException() {
        // Arrange
        String username = "customer1";
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenThrow(new RuntimeException("Database error"));
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentService.purchaseCart(username);
        });
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
    }
    // Test getAddressForUser when paymentInfo is null
    @Test
    public void testGetAddressForUserPaymentInfoNull() {
        // Arrange
        String username = "customer1";
        customer1.setPaymentInformation(null);
        when(userAccountRepo.findByUsername(username)).thenReturn(customer1);
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            paymentService.getAddressForUser(username);
        });
        assertEquals("404 NOT_FOUND \"No billing address found for this customer.\"", exception.getMessage());
        verify(userAccountRepo, times(1)).findByUsername(username);
    }
}
