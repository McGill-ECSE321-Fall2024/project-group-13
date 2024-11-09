package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import group_13.game_store.dto.OrderRequestDto;
import group_13.game_store.dto.OrderResponseDto;
import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.dto.UserAccountResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.PaymentService;
import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class UserAccountController {
    @Autowired
    private OrderManagementService orderManagementService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private GameStoreManagementService gameStoreManagementService;
    @Autowired
    private AccountService accountService;

    // creating a customer account while user is logged out 
    @PostMapping("/customers")
    public CustomerResponseDto createCustomer(@RequestBody UserAccountRequestDto request, @RequestParam String loggedInUsername) {
        // validate that user is neither employee, owner, or customer (will need to figure that out)
        if (accountService.hasPermission(loggedInUsername, 1) || accountService.hasPermission(loggedInUsername, 2) || accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be logged out to create a customer account");
        }
        // check if the account can be created at all
        Customer createdCustomerAccount = accountService.createCustomerAccount(request.getName(), request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        if (createdCustomerAccount == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account creation has not been made.");
        }
        return new CustomerResponseDto(createdCustomerAccount);
    }

    @GetMapping("/customers")
    public CustomerListDto findAllCustomers(@RequestParam String loggedInUsername) {
        // validate that user is either employee or owner
        if (accountService.hasPermission(loggedInUsername, 2)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be an owner or employee to view all customers");
        }

        List<CustomerResponseDto> allCustomers = new ArrayList<CustomerResponseDto>();
        for (Customer customer : gameStoreManagementService.getAllCustomers()) {
            allCustomers.add(new CustomerResponseDto(customer));
        }

        // retrieve all customers
        return new CustomerListDto(allCustomers);
    }

    // get a specific customer
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

    // updating a user's phone number or password
    @PutMapping("/users/{username}")
    public UserAccountResponseDto updateGeneralUserInformation(@PathVariable String username, @RequestBody UserAccountRequestDto request, @RequestParam String loggedInUsername) {
        // validating that a logged in account is update the password or phone number
        // every user has permission to change their own password
        if (!accountService.hasPermissionAtLeast(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Must be registered user to change phone number");
        }
        
        
        UserAccount aUser = accountService.findUserByUsername(username);
        if (aUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User accound " + username + " has not been made.");
        }
        // changing phone number of user whether it is owner, employee, or customer
        accountService.changePhoneNumber(request.getPhoneNumber(), request.getUsername());

        // changing password of user whether it is owner, employee, or customer
        accountService.changePassword(request.getPassword(), request.getUsername());

        return new UserAccountResponseDto(aUser);
        
    }

    @GetMapping("/customers/{username}/orders")
    public OrderListDto findAllOrdersOfCustomer(@RequestParam String loggedInUsername, @PathVariable String username) {
        // only customer should be able to see their order history
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to view their own orders");
        }

        List<OrderResponseDto> allOrdersOfCustomers = new ArrayList<OrderResponseDto>();
        for (Order order : orderManagementService.getOrderHistoryOfCustomer(username)) {
            allOrdersOfCustomers.add(new OrderResponseDto(order));
        }

        return new OrderListDto(allOrdersOfCustomers);
    }

    @PostMapping("/customers/{username}/orders") 
    public OrderResponseDto createOrder(@PathVariable String username, @RequestBody OrderRequestDto request, @RequestParam String loggedInUsername) {
        // only customer should be able to add orders to their order history
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to make own orders");
        }

        // purchasing the cart creates the order, which is now saved in the database
        Order createdOrder = paymentService.purchaseCart(username);
        if (createdOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order made by " + username + " has not been made.");
        }

        // will need to the return variable for PaymentService.purchaseCart for this method to allow the return of a created OrderResponseDto
        return new OrderResponseDto(createdOrder);    
    }

    @GetMapping("/customers/{username}/orders/{orderId}")
    public OrderResponseDto findOrderOfCustomer(@PathVariable String username, @PathVariable int orderId, @RequestParam String loggedInUsername) {
        // only customer should be able to check their own order
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to check their own order");
        }

        Order foundOrder = orderManagementService.getOrderById(orderId);
        if (foundOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order " + orderId + " made by " + username + " has not been made.");
        }
        return new OrderResponseDto(foundOrder);
    }

    @PutMapping("/customers/{username}/orders/{orderId}/games/{gameId}")
    public OrderResponseDto returnOrder(@PathVariable String username, @PathVariable int orderId, @PathVariable int gameId, @RequestParam String loggedInUsername) {
        // only customer should be able to return their own order
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be a customer to return their order");
        }

        Order returnedOrder = orderManagementService.returnOrder(orderId, gameId);
        if (returnedOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order " + orderId + " from " + username +  "for game " + gameId + "has not been made.");
        }

        return new OrderResponseDto(returnedOrder);
    }
}
