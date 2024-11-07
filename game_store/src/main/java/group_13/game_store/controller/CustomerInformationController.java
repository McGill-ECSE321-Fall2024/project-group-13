package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.DeliveryInformationRequestDto;
import group_13.game_store.dto.DeliveryInformationResponseDto;
import group_13.game_store.dto.PaymentInformationRequestDto;
import group_13.game_store.dto.PaymentInformationResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.DeliveryInformation;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.service.AccountService;

@RestController
public class CustomerInformationController {
    // Get all the necessary service files
    @Autowired
    AccountService accountService;
    
    // Get method for a customer's payment information
    @GetMapping("/customers/{loggedInUsername}/paymentInfo")
    public PaymentInformationResponseDto getPaymentInformation(@PathVariable String loggedInUsername) {
        PaymentInformation paymentInfo = accountService.getPaymentInformationByCustomerUsername(loggedInUsername);

        PaymentInformationResponseDto paymentInfoResponse = new PaymentInformationResponseDto(
            paymentInfo.getPaymentInfoID(),
            paymentInfo.getCardNumber(),
            paymentInfo.getBillingName(),
            paymentInfo.getExpiryDate(),
            paymentInfo.getCvvCode(),
            paymentInfo.getBillingAddress());
        
        return paymentInfoResponse;
    }

    // Post method for a customer's payment information
    @PostMapping("/customers/{loggedInUsername}/paymentInfo")
    public PaymentInformationResponseDto addPaymentInformation(@RequestBody PaymentInformationRequestDto paymentInfoRequestDto, @PathVariable String loggedInUsername) {
        Address billingAddress = accountService.getAddressById(paymentInfoRequestDto.getAddressId());
        accountService.changePaymentInfo(
            loggedInUsername,
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            paymentInfoRequestDto.getExpiryDate(),
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

    // Put method for a customer's delivery information
    @PutMapping("/customers/{loggedInUsername}/paymentInfo/{paymentInfoId}")
    public PaymentInformationResponseDto editPaymentInfo(@RequestBody PaymentInformationRequestDto paymentInfoRequestDto, @PathVariable String loggedInUsername, @PathVariable int paymentInfoId) {
        Address billingAddress = accountService.getAddressById(paymentInfoRequestDto.getAddressId());
        accountService.changePaymentInfo(
            loggedInUsername,
            paymentInfoRequestDto.getCardNumber(),
            paymentInfoRequestDto.getBillingName(),
            paymentInfoRequestDto.getExpiryDate(),
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

    // Get method for a customer's delivery information
    @GetMapping("/customers/{loggedInUsername}/deliveryInfo")
    public DeliveryInformationResponseDto getDeliveryInformation(@PathVariable String loggedInUsername) {
        DeliveryInformation deliveryInfo = accountService.getDeliveryInformationByCustomerUsername(loggedInUsername);

        DeliveryInformationResponseDto deliveryInfoResponse = new DeliveryInformationResponseDto(
            deliveryInfo.getDeliveryInfoID(),
            deliveryInfo.getDeliveryName(),
            deliveryInfo.getDeliveryAddress());
        
        return deliveryInfoResponse;
    }


    // Post method for a customer's delivery information
    @PostMapping("/customers/{loggedInUsername}/deliveryInfo")
    public DeliveryInformationResponseDto addDeliveryInformation(@RequestBody DeliveryInformationRequestDto deliveryInfoRequestDto, @PathVariable String loggedInAccount) {
        Address deliveryAddress = accountService.getAddressById(deliveryInfoRequestDto.getAddressId());
        accountService.changeDeliveryInfo(
            loggedInAccount,
            deliveryInfoRequestDto.getDeliveryName(),
            deliveryAddress
        );

        int deliveryInfoId = accountService.getDeliveryInfoIdByUsername(loggedInAccount);

        DeliveryInformationResponseDto deliveryInfoResponse = new DeliveryInformationResponseDto(
            deliveryInfoId, 
            loggedInAccount, 
            deliveryAddress
        );
        
        return deliveryInfoResponse;
    }

}