package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.service.OrderManagementService;
import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.dto.OrderListDto;
import group_13.game_store.dto.ReturnOrderRequestDto;
import group_13.game_store.dto.ReturnOrderResponseDto;
import group_13.game_store.dto.OrderCreationRequestDto;
import group_13.game_store.dto.OrderCreationResponseDto;
import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.dto.UserAccountResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.PaymentService;
import group_13.game_store.service.ReviewService;
import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import org.springframework.web.server.ResponseStatusException;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserAccountController {
    
    @Autowired
    private OrderManagementService orderManagementService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private GameStoreManagementService gameStoreManagementService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AccountService accountService;
 
    /**
     * Creates a new customer account while the user is logged out.
     *
     * @param request          The user account request data.
     * @param loggedInUsername The username of the logged-in user.
     * @return The created customer account.
     */
    @PostMapping("/customers")
    public CustomerResponseDto createCustomer(@RequestBody UserAccountRequestDto request, @RequestParam String loggedInUsername) {
        // validate that user is neither employee, owner, or customer (will need to figure that out)
        
        // COULD POTENTIALLY REMOVE THIS LINE NOW THAT WE HAVE ACCOUNTSERVICE.LOGINTOACCOUNT
        if (accountService.hasPermission(loggedInUsername, 1) || accountService.hasPermission(loggedInUsername, 2) || accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be logged out to create a customer account");
        }

        // check if the account can be created at all
        Customer createdCustomerAccount = accountService.createCustomerAccount(request.getName(), request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        return new CustomerResponseDto(createdCustomerAccount);
    }

    /**
     * Retrieves all customers.
     *
     * @param loggedInUsername The username of the logged-in user.
     * @return A list of all customers.
     */
    @GetMapping("/customers")
    public CustomerListDto findAllCustomers(@RequestParam String loggedInUsername) {
        // validate that user is either employee or owner
        if (!accountService.hasPermissionAtLeast(loggedInUsername, 2)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be an owner or employee to view all customers");
        }

        Iterable<Customer> customers = gameStoreManagementService.getAllCustomers();
        //List<Customer> customers = gameStoreManagementService.getAllCustomers();
        List<CustomerResponseDto> allCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            allCustomers.add(new CustomerResponseDto(customer));
        }

        // retrieve all customers
        return new CustomerListDto(allCustomers);
    }

    /**
     * Retrieves a specific customer by username.
     *
     * @param username         The username of the customer to find.
     * @param loggedInUsername The username of the logged-in user.
     * @return The customer with the specified username.
     */
    @GetMapping("/customers/{username}")
    public CustomerResponseDto findCustomer(@PathVariable String username, @RequestParam String loggedInUsername) {
        // validate that it is an employee or owner who is searching for customer
        if (!accountService.hasPermissionAtLeast(loggedInUsername, 2)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be an owner or employee");
        }

        Customer customerToFind = accountService.findCustomerByUsername(username);
        if (customerToFind == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer " + username + " was not found.");
        }

        return new CustomerResponseDto(customerToFind);
    }

    /**
     * Updates a user's phone number or password.
     *
     * @param username         The username of the user to update.
     * @param request          The user account request data containing updates.
     * @param loggedInUsername The username of the logged-in user.
     * @return The updated user account.
     */
    @PutMapping("/users/{username}")
    public UserAccountResponseDto updateGeneralUserInformation(@PathVariable String username, @RequestBody UserAccountRequestDto request, @RequestParam String loggedInUsername) {
        // validating that a logged in account is update the password or phone number
        // every user has permission to change their own password
        if (!accountService.hasPermissionAtLeast(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be registered user to change phone number or password");
        }
        
        
        UserAccount aUser = accountService.findUserByUsername(username);
        if (aUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account " + username + " has not been made.");
        }
        // changing phone number of user whether it is owner, employee, or customer
        accountService.changePhoneNumber(request.getPhoneNumber(), request.getUsername());

        // changing password of user whether it is owner, employee, or customer
        accountService.changePassword(request.getPassword(), request.getUsername());

        return new UserAccountResponseDto(aUser);
    }

    /**
     * Checks if a user has a game.
     *
     * @param username         The username of the user to check.
     * @param gameID           The ID of the game to check.
     * @return True if the user has the game, false otherwise.
     */
    @GetMapping("/users/{username}/{gameID}")
    public Boolean checkIfUserHasGame(@PathVariable String username, @PathVariable int gameID) {
        
        return reviewService.checkOwnership(username, gameID);
    }



    /**
     * Retrieves all orders of a customer.
     *
     * @param loggedInUsername The username of the logged-in user.
     * @param username         The username of the customer.
     * @return A list of all orders made by the customer.
     */
    @GetMapping("/customers/{username}/orders")
    public OrderListDto findAllOrdersOfCustomer(@RequestParam String loggedInUsername, @PathVariable String username) {
        // only customer should be able to see their order history
        if (!accountService.hasPermission(loggedInUsername, 1) || !loggedInUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to view their own orders");
        }

        List<OrderCreationResponseDto> allOrdersOfCustomers = new ArrayList<OrderCreationResponseDto>();
        for (Order order : orderManagementService.getOrderHistoryOfCustomer(username)) {
            allOrdersOfCustomers.add(new OrderCreationResponseDto(order));
        }

        return new OrderListDto(allOrdersOfCustomers);
    }

    /**
     * Creates an order for a customer by purchasing the cart.
     *
     * @param username         The username of the customer.
     * @param request          The order request data.
     * @param loggedInUsername The username of the logged-in user.
     * @return The created order.
     */
    @PostMapping("/customers/{username}/orders") 
    public OrderCreationResponseDto createOrder(@PathVariable String username, @RequestBody OrderCreationRequestDto request, @RequestParam String loggedInUsername) {
        // only customer should be able to add orders to their order history
        if (!accountService.hasPermission(loggedInUsername, 1) || !username.equals(loggedInUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to make own orders");
        }

        // purchasing the cart creates the order, which is now saved in the database
        Order createdOrder = paymentService.purchaseCart(username);
        if (createdOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order made by " + username + " has not been made.");
        }

        // will need to the return variable for PaymentService.purchaseCart for this method to allow the return of a created OrderResponseDto
        return new OrderCreationResponseDto(createdOrder);    
    }

    /**
     * Retrieves a specific order of a customer.
     *
     * @param username         The username of the customer.
     * @param orderId          The ID of the order.
     * @param loggedInUsername The username of the logged-in user.
     * @return The specified order.
     */
    @GetMapping("/customers/{username}/orders/{orderId}")
    public OrderCreationResponseDto findOrderOfCustomer(@PathVariable String username, @PathVariable int orderId, @RequestParam String loggedInUsername) {
        // only customer should be able to check their own order
        if (!accountService.hasPermission(loggedInUsername, 1) || !username.equals(loggedInUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to check their own order");
        }

        Order foundOrder = orderManagementService.getOrderById(orderId);
        if (foundOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order " + String.valueOf(orderId) + " made by " + username + " has not been made.");
        }
        return new OrderCreationResponseDto(foundOrder);
    }

    /**
     * Returns an order for a specific game.
     *
     * @param username         The username of the customer.
     * @param orderId          The ID of the order.
     * @param gameId           The ID of the game to return.
     * @param loggedInUsername The username of the logged-in user.
     * @return The updated order after return.
     */
    @PutMapping("/customers/{username}/orders/{orderId}")
    public ReturnOrderResponseDto returnOrder(@PathVariable String username, @PathVariable int orderId, @RequestBody ReturnOrderRequestDto request, @RequestParam String loggedInUsername) {
        // only customer should be able to return their own order
        if (!accountService.hasPermission(loggedInUsername, 1) || !loggedInUsername.equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to return their order");
        }

        // need to include date input to make tge service method returnOrder testable
        //Date dateToReturn = Date.valueOf(LocalDate.now());
        Order returnedOrder = orderManagementService.returnOrder(orderId, request.getReturnDate());

        return new ReturnOrderResponseDto(returnedOrder);
    }
}
