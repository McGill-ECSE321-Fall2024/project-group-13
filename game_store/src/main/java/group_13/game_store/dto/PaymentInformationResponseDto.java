package group_13.game_store.dto;

import java.sql.Date;

import group_13.game_store.model.Address;

public class PaymentInformationResponseDto {

    private int paymentInfoID;
    private long cardNumber;
    private String billingName;
    private Date expiryDate;
    private int cvvCode;
    private Address billingAddress;

    // Default constructor for hibernate
    protected PaymentInformationResponseDto() {
    }


    public PaymentInformationResponseDto(int paymentInfoID, long cardNumber, String billingName, Date expiryDate, int cvvCode, Address aBillingAddress) {
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

    public long cardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long newCardNumber) {
        this.cardNumber = newCardNumber;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String newBillingName) {
        this.billingName = newBillingName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date newExpiryDate) {
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