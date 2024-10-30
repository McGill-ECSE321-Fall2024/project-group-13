package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.DeliveryInformation;

public interface DeliveryInformationRepository extends CrudRepository<DeliveryInformation, Integer> {
    // allows instantiation of a DeliveryInformation instance that is stored in the local database by its unique ID
    public DeliveryInformation findByDeliveryInfoID(int deliveryID);
}
