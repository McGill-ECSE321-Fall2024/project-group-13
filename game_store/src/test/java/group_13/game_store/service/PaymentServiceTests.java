package group_13.game_store.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Address;
import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Order;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.Game.VisibilityStatus;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
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

        addressInvalidPostalCode = new Address("123 Test St", "INVALID", 1, "TestCity", "TestProvince", "TestCountry", 1);

        // Initialize payment information
        paymentInfoValid = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.plusMonths(1)),  123, addressValid);

        paymentInfoInvalidPostalCode = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.plusMonths(1)),  123, addressInvalidPostalCode);

        paymentInfoInvalidCardNumber = new PaymentInformation("12345678", "Tim", Date.valueOf(today.plusMonths(1)),  123, addressValid);

        paymentInfoInvalidCVV = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.plusMonths(1)),  12, addressValid);

        paymentInfoExpiredCard = new PaymentInformation("1234567812345678", "Tim", Date.valueOf(today.minusMonths(1)),  123, addressValid);

        // Initialize customer
        customer1 = new Customer("tim", "customer1", "tim@yahoo", "123", "6047640574");
        customer1.setPaymentInformation(paymentInfoValid);

        gameCategory = new GameCategory("Boring Category", GameCategory.VisibilityStatus.Visible, "Boring");
        
        // Initialize games
        game1 = new Game("Game1", "A Game", "anImg", 10, 59.99, "R", VisibilityStatus.Visible, gameCategory);

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
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertTrue(result, "Purchase should be successful");
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verify(gameRepo, times(1)).save(game1);
        verify(gameCopyRepo, times(1)).save(any(GameCopy.class));
        verify(orderRepo, times(1)).save(any(Order.class));
        verify(cartItemRepo, times(1)).deleteByKeyCustomerAccountUsername(username);
    }

    // Test when customer does not exist
    @Test
    public void testPurchaseCartCustomerNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(customerRepo.findByUsername(username)).thenReturn(null);

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when customer does not exist");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when payment information is null");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when postal code is invalid");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when card number is invalid");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when CVV code is invalid");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when card is expired");
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

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when cart is empty");
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verifyNoMoreInteractions(gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when not enough stock
    @Test
    public void testPurchaseCartNotEnoughStock() {
        // Arrange
        String username = "customer1";
        CartItem cartItemInsufficientStock = new CartItem(new CartItem.Key(customer1, game1), 20); // quantity > stock
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(List.of(cartItemInsufficientStock));

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when not enough stock");
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verifyNoMoreInteractions(gameRepo, gameCopyRepo, orderRepo);
    }

    // Test when exception occurs during gameRepo.save(game)
    @Test
    public void testPurchaseCartGameRepoSaveException() {
        // Arrange
        String username = "customer1";
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(List.of(cartItem1));
        when(gameRepo.save(any(Game.class))).thenThrow(new RuntimeException("GameRepo save exception"));

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when exception occurs during gameRepo.save()");
        verify(customerRepo, times(1)).findByUsername(username);
        verify(cartItemRepo, times(1)).findByKeyCustomerAccountUsername(username);
        verify(gameRepo, times(1)).save(game1);
        verifyNoMoreInteractions(gameCopyRepo, orderRepo);
    }

    // Test when getCartItems returns null
    @Test
    public void testPurchaseCartGetCartItemsReturnsNull() {
        // Arrange
        String username = "customer1";
        when(customerRepo.findByUsername(username)).thenReturn(customer1);
        when(cartItemRepo.findByKeyCustomerAccountUsername(username)).thenReturn(null);

        // Act
        boolean result = paymentService.purchaseCart(username);

        // Assert
        assertFalse(result, "Purchase should fail when getCartItems returns null");
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

    // Test getCartItems when exception occurs
    @Test
    public void testGetCartItemsException() {
        // Arrange
        when(cartItemRepo.findByKeyCustomerAccountUsername(customer1.getUsername()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        CartItem[] result = paymentService.getCartItems(customer1);

        // Assert
        assertNull(result);
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

    // Test clearCart when exception occurs
    @Test
    public void testClearCartException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(cartItemRepo)
                .deleteByKeyCustomerAccountUsername(customer1.getUsername());

        // Act
        paymentService.clearCart(customer1);

        // Assert
        verify(cartItemRepo, times(1)).deleteByKeyCustomerAccountUsername(customer1.getUsername());
        // No exception is thrown because the method catches it internally
    }
}