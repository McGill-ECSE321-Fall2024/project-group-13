package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    public Order findByOrderID(int orderID);
}