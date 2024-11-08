package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.service.OrderManagementService;
import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.dto.CustomerListDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Customer;

@RestController
public class UserAccountController {
    @Autowired
    private OrderManagementService orderManagementService;
    @Autowired
    private GameStoreManagementService gameStoreManagementService;
    @Autowired
    private AccountService accountService;

    // creating a customer account while user is logged out 
    @PostMapping("/customers")
    public CustomerResponseDto createCustomer(@RequestBody UserAccountRequestDto request, @RequestParam String loggedInUsername) {
        // validate that user is neither employee, owner, or customer (will need to figure that out)
        if (accountService.hasPermission(loggedInUsername, 1) || accountService.hasPermission(loggedInUsername, 2) || accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User must be logged out to create a customer account");
        }
        // check if the account can be created at all
        Customer createdCustomerAccount = accountService.createCustomerAccount(request.getName(), request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        if (createdCustomerAccount != null) {
            return new CustomerResponseDto(createdCustomerAccount);
        } else {
            throw new IllegalArgumentException("Account creation has not been made");
        }   
    }

    @GetMapping("/customers")
    public CustomerListDto findAllCustomers(@RequestParam String loggedInUsername) {
        // validate that user is either employee or owner
        if (accountService.hasPermission(loggedInUsername, 1)) {
            throw new IllegalArgumentException("User must be an owner to view all customers");
        }

        List<CustomerResponseDto> allCustomers = new ArrayList<CustomerResponseDto>();
        for (Customer customer : gameStoreManagementService.getAllCustomers()) {
            allCustomers.add(new CustomerResponseDto(customer));
        }

        // retrieve all customers
        return new CustomerListDto(allCustomers);
    }

}
