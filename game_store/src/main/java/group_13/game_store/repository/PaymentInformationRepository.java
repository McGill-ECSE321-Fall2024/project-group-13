package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.PaymentInformation;

public interface PaymentInformationRepository extends CrudRepository<PaymentInformation, Integer> {
    public PaymentInformation findByPaymentInfoID(int paymentInfoID);
}