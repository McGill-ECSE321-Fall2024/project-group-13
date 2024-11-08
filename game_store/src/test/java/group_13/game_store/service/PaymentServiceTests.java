package group_13.game_store.service;

import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;

import group_13.game_store.model.Order;
import group_13.game_store.model.Customer;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Game;
import group_13.game_store.model.CartItem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class PaymentServiceTests {
    
    @Mock
    private OrderRepository mockOrderRepo;

    @Mock
    private CustomerRepository mockCustomerRepo;

    @Mock
    private GameCopyRepository mockGameCopyRepo;

    @Mock
    private GameRepository mockGameRepo;

    @Mock
    private CartItemRepository mockCartItemRepo;

    @InjectMocks
    private PaymentService service;


    @Test
    public void testPurchaseCartSuccess()
    {
        // Arrange
        // Whenever mockRepo.save(p) is called, return p
        when(mockOrderRepo.save(any(Order.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));
        when(mockCustomerRepo.save(any(Customer.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));
        when(mockGameCopyRepo.save(any(GameCopy.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));
        when(mockGameRepo.save(any(Game.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));
        when(mockCartItemRepo.save(any(CartItem.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        
    }
    
}
