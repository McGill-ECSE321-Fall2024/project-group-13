package group_13.game_store.dto;
import java.time.LocalDate;

public class PaymentInformationRequestDto {
    private String cardNumber;
    private String billingName;
    private LocalDate expiryDate;
    private int cvvCode;
    private int addressId;

    // Default constructor for hibernate
    protected PaymentInformationRequestDto() {
    }

    public PaymentInformationRequestDto(String cardNumber, String billingName, LocalDate expiryDate, int cvvCode, int addressId) {
        this.cardNumber = cardNumber;
        this.billingName = billingName;
        this.expiryDate = expiryDate;
        this.cvvCode = cvvCode;
        this.addressId = addressId;
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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int newAddressId) {
        this.addressId = newAddressId;
    }
}
