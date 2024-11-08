package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.service.OrderManagementService;
import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.service.AccountService;

import group_13.game_store.model.UserAccount;

@RestController
public class UserAccountController {
    @Autowired
    private OrderManagementService orderManagementService;
    @Autowired
    private AccountService accountService;

    // creating a customer account while user is logged out 
    @PostMapping("/customers")
    public CustomerResponseDto createCustomer(@RequestBody UserAccountRequestDto request) {
        // validate that user is neither employee, owner, or customer

        // check if the account can be created at all
        boolean canCreateAccount = accountService.createCustomerAccount(request.getName(), request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber());

        if (canCreateAccount == true) {
            // retrieve the customer account from the database
            UserAccount customerCreated = accountService.findUserAccountByUsername(request.getUsername());
            return new CustomerResponseDto((Customer) customerCreated);
        } else {
            throw new IllegalArgumentException("Customer account cannot be created.");
        }

        

    }

}
