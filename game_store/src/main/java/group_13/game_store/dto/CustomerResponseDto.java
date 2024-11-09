package group_13.game_store.dto;

import group_13.game_store.model.Customer;
import group_13.game_store.model.PaymentInformation;


public class CustomerResponseDto extends UserAccountResponseDto {
    private PaymentInformation paymentInformation;

    public CustomerResponseDto(Customer aCustomerAccount) {
        super(aCustomerAccount);
        this.paymentInformation = aCustomerAccount.getPaymentInformation();
    }

    // do not wanna reveal sensitive information*
    public int getPaymentInformationID() {
        return paymentInformation.getPaymentInfoID();
    }
}
