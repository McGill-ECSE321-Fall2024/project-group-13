package group_13.game_store.dto;

import group_13.game_store.model.Customer;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.DeliveryInformation;


public class CustomerResponseDto extends UserAccountResponseDto {
    private PaymentInformation paymentInformation;
    private DeliveryInformation deliveryInformation;

    public CustomerResponseDto(Customer aCustomerAccount) {
        super(aCustomerAccount);
        this.paymentInformation = aCustomerAccount.getPaymentInformation();
        this.deliveryInformation = aCustomerAccount.getDeliveryInformation();
    }

    // do not wanna reveal sensitive information*
    public int getPaymentInformationID() {
        return paymentInformation.getPaymentInfoID();
    }

    public DeliveryInformation getDeliveryInformation() {
        return deliveryInformation;
    }
}
