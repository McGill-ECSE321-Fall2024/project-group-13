package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.PaymentInformation;

public interface PaymentInformationRepository extends CrudRepository<PaymentInformation, Integer> {
    // allows instantiation of an PaymentInformation instance that is stored in the local database by its unique ID
    public PaymentInformation findByPaymentInfoID(int paymentInfoID);
}