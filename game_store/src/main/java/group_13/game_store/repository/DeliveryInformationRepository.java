package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.DeliveryInformation;

public interface DeliveryInformationRepository extends CrudRepository<DeliveryInformation, Integer> {
    public DeliveryInformation findByDeliveryInfoID(int deliveryID);
}
