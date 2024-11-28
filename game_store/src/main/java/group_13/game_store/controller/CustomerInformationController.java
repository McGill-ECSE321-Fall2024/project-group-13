package group_13.game_store.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.PaymentInformationRequestDto;
import group_13.game_store.dto.PaymentInformationResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.service.AccountService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerInformationController {
    // Get all the necessary service files
    @Autowired
    AccountService accountService;
    
    /**
     * Get the payment information of a customer.   (Only customers)
     * 
     * @param loggedInUsername  The user name of the person currently logged in. If no one is logged in, it is equal to 'guest'.
     * @return                  The obtained payment information for the customer, including its id.
     */
    @GetMapping("/customers/{loggedInUsername}/paymentInfo")
    public PaymentInformationResponseDto getPaymentInformation(@PathVariable String loggedInUsername) {
        PaymentInformation paymentInfo = accountService.getPaymentInformationByCustomerUsername(loggedInUsername);
        
        PaymentInformationResponseDto paymentInfoResponse = new PaymentInformationResponseDto(
            paymentInfo.getPaymentInfoID(),
            paymentInfo.getCardNumber(),
            paymentInfo.getBillingName(),
            paymentInfo.getExpiryDate().toLocalDate(),
            paymentInfo.getCvvCode(),
            paymentInfo.getBillingAddress());
        
        return paymentInfoResponse;
    }

    /**
     * Add a payment information for a customer.    (Only customers)
     * 
     * @param loggedInUsername          The user name of the person currently logged in. If no one is logged in, it is equal to 'guest'.
     * @param paymentInfoRequestDto     The payment information we wish to add for the user.
     * @return                          The payment information we added to the customer, including its id.
     */
    @PostMapping("/customers/{loggedInUsername}/paymentInfo")
    public PaymentInformationResponseDto addPaymentInformation( @PathVariable String loggedInUsername, @RequestBody PaymentInformationRequestDto paymentInfoRequestDto) {
        Address billingAddress = accountService.getAddressById(paymentInfoRequestDto.getAddressId());
        accountService.changePaymentInfo(
            loggedInUsername,
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            Date.valueOf(paymentInfoRequestDto.getExpiryDate()),
            paymentInfoRequestDto.getCvvCode(),
            billingAddress
        );
        int paymentInfoId = accountService.getPaymentInfoIdByUsername(loggedInUsername);

        PaymentInformationResponseDto paymentInfoResponse = new PaymentInformationResponseDto(
            paymentInfoId,
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            paymentInfoRequestDto.getExpiryDate(),
            paymentInfoRequestDto.getCvvCode(),
            billingAddress);
        
        return paymentInfoResponse;
    }

    /**
     * Updates the payment information of a customer.   (Only customers)
     * 
     * @param paymentInfoRequestDto     The updated payment information we wish to put for the user.
     * @param loggedInUsername          The user name of the person currently logged in. If no one is logged in, it is equal to 'guest'.
     * @param paymentInfoId             The id of the payment information that we wish to update.
     * @return                          The updated payment information, including its id.
     */
    @PutMapping("/customers/{loggedInUsername}/paymentInfo/{paymentInfoId}")
    public PaymentInformationResponseDto editPaymentInfo(@RequestBody PaymentInformationRequestDto paymentInfoRequestDto, @PathVariable String loggedInUsername, @PathVariable int paymentInfoId) {
        Address billingAddress = accountService.getAddressById(paymentInfoRequestDto.getAddressId());
        accountService.changePaymentInfo(
            loggedInUsername,
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            Date.valueOf(paymentInfoRequestDto.getExpiryDate()),
            paymentInfoRequestDto.getCvvCode(),
            billingAddress
        );

        PaymentInformationResponseDto paymentInfoResponse = new PaymentInformationResponseDto(
            paymentInfoId, 
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            paymentInfoRequestDto.getExpiryDate(),
            paymentInfoRequestDto.getCvvCode(),
            billingAddress);
    
        return paymentInfoResponse;
    }
}