package group_13.game_store.dto;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import group_13.game_store.model.Address;

public class PaymentInformationResponseDto {

    private int paymentInfoID;
    private String cardNumber;
    private String billingName;
    private LocalDate expiryDate;
    private int cvvCode;
    private Address billingAddress;

    // Default constructor for hibernate
    protected PaymentInformationResponseDto() {
    }


    public PaymentInformationResponseDto(int paymentInfoID, String cardNumber, String billingName, LocalDate expiryDate, int cvvCode, Address aBillingAddress) {
        this.paymentInfoID = paymentInfoID;
        this.cardNumber = cardNumber;
        this.billingName = billingName;
        this.expiryDate = expiryDate;
        this.cvvCode = cvvCode;
        if (!setBillingAddress(aBillingAddress))
        {
        throw new RuntimeException("Unable to create PaymentInformation due to aBillingAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    public int getPaymentInfoID() {
        return paymentInfoID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String newCardNumber) {
        this.cardNumber = newCardNumber;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String newBillingName) {
        this.billingName = newBillingName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate newExpiryDate) {
        this.expiryDate = newExpiryDate;
    }

    public int getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(int newCvvCode) {
        this.cvvCode = newCvvCode;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }
    
    public boolean setBillingAddress(Address aNewBillingAddress)
    {
        boolean wasSet = false;
        if (aNewBillingAddress != null)
        {
        billingAddress = aNewBillingAddress;
        wasSet = true;
        }
        return wasSet;
    }
}