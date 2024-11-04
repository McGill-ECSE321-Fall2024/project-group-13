package group_13.game_store.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.DeliveryInformation;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.OrderRepository;
import jakarta.transaction.Transactional;

public class PaymentService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private GameCopyRepository gameCopyRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Transactional
    public boolean purchaseCart(String username)
    {
        try 
        {
            Customer customer = customerRepo.findByUsername(username);

            if (customer == null)
            {
                System.out.print("No such customer with username exists");
                return false;
            }

            DeliveryInformation deliveryInformation = customer.getDeliveryInformation();

            if (deliveryInformation == null)
            {
                System.out.print("Customer does not have delivery information set up");
                return false;
            }

            if (!deliveryInformation.getDeliveryAddress().getPostalCode().matches("^[A-Za-z][0-9][A-Za-z][0-9][A-Za-z][0-9]$"))
            {
                System.out.print("Customer delivery address postal code information is not valid");
                return false;
            }

            PaymentInformation paymentInformation = customer.getPaymentInformation();
            
            if (paymentInformation == null)
            {
                System.out.print("Customer does not have valid payment information ");
                return false;
            }

            if (Long.toString(paymentInformation.getCardNumber()).length() != 16)
            {
                System.out.print("Customer does not have valid credit card number");
                return false;
            }

            if (Integer.toString(paymentInformation.getCvvCode()).length() != 3)
            {
                System.out.print("Customer does not have valid CVV code");
                return false;
            }

            if (!paymentInformation.getBillingAddress().getPostalCode().matches("^[A-Za-z][0-9][A-Za-z][0-9][A-Za-z][0-9]$"))
            {
                System.out.print("Customer billing address postal code information is not valid");
                return false;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            if (paymentInformation.getExpiryDate().before(currentDate))
            {
                System.out.print("Customer credit card is expired");
                return false;
            }

            CartItem[] cartItems = getCartItems(customer);

            if (cartItems == null || cartItems.length <= 0)
            {
                System.out.print("Customer cart is empty");
                return false;
            }

            GameCopy gameCopy;
            Game game;

            for(int i = 0; i < cartItems.length; i++)
            { 
                    game = cartItems[i].getKey().getGame();
                    if (cartItems[i].getQuantity() > game.getStock())
                    {
                        System.out.print(String.format("There is not enough stock of %s to complete purchase", game.getTitle()));
                        return false;
                    }
            }

            Order order = new Order(currentDate, null, customer);

            for(int i = 0; i < cartItems.length; i++)
            {
                game = cartItems[i].getKey().getGame();
                game.setStock(game.getStock() - cartItems[i].getQuantity());
                game = gameRepo.save(game);
                for(int j = 0; j < cartItems[i].getQuantity(); j++)
                {   
                    gameCopy = new GameCopy(order, game);
                    gameCopy = gameCopyRepo.save(gameCopy);
                }
            }

            clearCart(customer);
            order = orderRepo.save(order);
            return true;
        }
        catch(Exception e)
        {
            System.out.print("Unexpected error encountered when purchasing cart:");
            System.out.print(e.getMessage());
            return false;
        }
    }
    
    @Transactional
    public CartItem[] getCartItems(Customer customer)
    {
        try
        {
            CartItem[] cartItems = cartItemRepo.findByKeyCustomerAccountUsername(customer.getUsername()).toArray(new CartItem[0]);
            return cartItems;
        }
        catch(Exception e)
        {
            System.out.print("Unexpected error encountered when finding cart items:");
            System.out.print(e.getMessage());
            return null;
        }

    }

    @Transactional
    public void clearCart(Customer customer)
    {
        try
        {
            cartItemRepo.deleteByKeyCustomerAccountUsername(customer.getUsername());
        }
        catch(Exception e)
        {
            System.out.print("Unexpected error encountered when clearing cart:");
            System.out.print(e.getMessage());
        }
    }
}
