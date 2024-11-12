package group_13.game_store.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_13.game_store.model.CartItem;
import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.UserAccountRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
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

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserAccountRepository userAccountRepo;

    @Autowired
    private PaymentInformationRepository paymentInfoRepo;

    /**
     * Processes the purchase of all items in a customer's cart.
     * 
     * @param username The username of the customer making the purchase.
     * @return Order if the purchase is successful, false otherwise.
     */
    @Transactional
    public Order purchaseCart(String username) {
        try {
            // Retrieve customer details by username
            Customer customer = customerRepo.findByUsername(username);
            if (customer == null) {
                System.out.print("No such customer with username exists");
                return null;
            }

            // Check if customer has payment information
            PaymentInformation paymentInformation = customer.getPaymentInformation();
            if (paymentInformation == null) {
                System.out.print("Customer does not have valid payment information");
                return null;
            }

            // Validate postal code format for address
            if (!paymentInformation.getBillingAddress().getPostalCode().matches("^[A-Za-z][0-9][A-Za-z][0-9][A-Za-z][0-9]$")) {
                System.out.print("Customer address postal code information is not valid");
                return null;
            }

            // Validate postal code format for address
            if (!paymentInformation.getBillingAddress().getPostalCode().matches("^[A-Za-z][0-9][A-Za-z][0-9][A-Za-z][0-9]$")) {
                System.out.print("Customer address postal code information is not valid");
                return null;
            }

            // Validate credit card number and CVV code
            if (paymentInformation.getCardNumber().length() != 16) {
                System.out.print("Customer does not have valid credit card number");
                return null;
            }
            if (Integer.toString(paymentInformation.getCvvCode()).length() != 3) {
                System.out.print("Customer does not have valid CVV code");
                return null;
            }

            // Validate postal code format for billing address
            if (!paymentInformation.getBillingAddress().getPostalCode().matches("^[A-Za-z][0-9][A-Za-z][0-9][A-Za-z][0-9]$")) {
                System.out.print("Customer billing address postal code information is not valid");
                return null;
            }

            // Check if credit card is expired
            Date currentDate = new Date(System.currentTimeMillis());
            if (paymentInformation.getExpiryDate().before(currentDate)) {
                System.out.print("Customer credit card is expired");
                return null;
            }

            // Retrieve all items in the customer's cart
            CartItem[] cartItems = getCartItems(customer);
            if (cartItems == null || cartItems.length <= 0) {
                System.out.print("Customer cart is empty");
                return null;
            }

            // Check if there is enough stock for each game in the cart
            Game game;
            for (int i = 0; i < cartItems.length; i++) {
                game = cartItems[i].getKey().getGame();
                if (cartItems[i].getQuantity() > game.getStock()) {
                    System.out.print(String.format("There is not enough stock of %s to complete purchase", game.getTitle()));
                    return null;
                }
            }

            // Create a new order for the customer
            Order order = new Order(currentDate, null, customer);

            // Reduce stock for each game and create GameCopy records
            GameCopy gameCopy;
            for (int i = 0; i < cartItems.length; i++) {
                game = cartItems[i].getKey().getGame();
                game.setStock(game.getStock() - cartItems[i].getQuantity());
                game = gameRepo.save(game);

                for (int j = 0; j < cartItems[i].getQuantity(); j++) {
                    gameCopy = new GameCopy(order, game);
                    gameCopy = gameCopyRepo.save(gameCopy);
                }
            }

            // Clear customer's cart and save the order
            clearCart(customer);
            order = orderRepo.save(order);
            return order;
        } catch (Exception e) {
            System.out.print("Unexpected error encountered when purchasing cart:");
            System.out.print(e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all items in the customer's cart.
     * 
     * @param customer The customer whose cart items are to be retrieved.
     * @return An array of CartItem objects representing items in the cart.
     */
    @Transactional
    public CartItem[] getCartItems(Customer customer) {
        try {
            CartItem[] cartItems = cartItemRepo.findByKeyCustomerAccountUsername(customer.getUsername()).toArray(new CartItem[0]);
            return cartItems;
        } catch (Exception e) {
            System.out.print("Unexpected error encountered when finding cart items:");
            System.out.print(e.getMessage());
            return null;
        }
    }

    /**
     * Clears all items from the customer's cart.
     * 
     * @param customer The customer whose cart is to be cleared.
     */
    @Transactional
    public void clearCart(Customer customer) {
        try {
            cartItemRepo.deleteByKeyCustomerAccountUsername(customer.getUsername());
        } catch (Exception e) {
            System.out.print("Unexpected error encountered when clearing cart:");
            System.out.print(e.getMessage());
        }
    }


    @Transactional
    public boolean addAddressToUser(String username, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null || !(user instanceof Customer)) {
            System.out.println("User not found or not a customer.");
            return false;
        }

        // Ensure customer has a PaymentInformation instance
        Customer customer = (Customer) user;
        PaymentInformation paymentInfo = customer.getPaymentInformation();
        if (paymentInfo == null) {
            System.out.println("No payment information found for the customer.");
            return false;
        }

        // Validate address fields
        if (street == null || street.trim().isEmpty() || postalCode == null || postalCode.trim().isEmpty() || number <= 0 ||
            city == null || city.trim().isEmpty() || stateOrProvince == null || stateOrProvince.trim().isEmpty() ||
            country == null || country.trim().isEmpty() || (apartmentNo != null && apartmentNo <= 0)) {
            System.out.println("Invalid address information.");
            return false;
        }

        // Create and save new address
        Address newAddress = new Address(street, postalCode, number, city, stateOrProvince, country, apartmentNo != null ? apartmentNo : 0);
        addressRepo.save(newAddress);

        // Set address in PaymentInformation and save
        paymentInfo.setBillingAddress(newAddress);
        paymentInfoRepo.save(paymentInfo);

        System.out.println("Address added successfully.");
        return true;
    }

    @Transactional
    public boolean updateAddressForUser(String username, int addressId, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null || !(user instanceof Customer)) {
            System.out.println("User not found or not a customer.");
            return false;
        }

        // Retrieve customer's PaymentInformation and validate address exists
        Customer customer = (Customer) user;
        PaymentInformation paymentInfo = customer.getPaymentInformation();
        if (paymentInfo == null || paymentInfo.getBillingAddress() == null || paymentInfo.getBillingAddress().getAddressID() != addressId) {
            System.out.println("No matching address found for the customer.");
            return false;
        }

        // Validate address fields
        if (street == null || street.trim().isEmpty() || postalCode == null || postalCode.trim().isEmpty() || number <= 0 ||
            city == null || city.trim().isEmpty() || stateOrProvince == null || stateOrProvince.trim().isEmpty() ||
            country == null || country.trim().isEmpty() || (apartmentNo != null && apartmentNo <= 0)) {
            System.out.println("Invalid address information.");
            return false;
        }

        // Update address fields
        Address address = paymentInfo.getBillingAddress();
        address.setStreet(street);
        address.setPostalCode(postalCode);
        address.setNumber(number);
        address.setCity(city);
        address.setStateOrProvince(stateOrProvince);
        address.setCountry(country);
        address.setApartmentNo(apartmentNo != null ? apartmentNo : 0);

        addressRepo.save(address);

        System.out.println("Address updated successfully.");
        return true;
    }

    @Transactional
    public Address getAddressForUser(String username) {
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null || !(user instanceof Customer)) {
            System.out.println("User not found or not a customer.");
            return null;
        }

        // Retrieve customer's PaymentInformation and get billing address
        Customer customer = (Customer) user;
        PaymentInformation paymentInfo = customer.getPaymentInformation();
        Address address = paymentInfo != null ? paymentInfo.getBillingAddress() : null;
        if (address == null) {
            System.out.println("No billing address found for this customer.");
            return null;
        }

        return address;
    }


}