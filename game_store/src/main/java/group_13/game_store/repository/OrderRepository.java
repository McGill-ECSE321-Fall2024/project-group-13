package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Order;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    // allows instantiation of an Order instance that is stored in the local database by its unique ID
    public Order findByOrderID(int orderID);

    // returns all orders associated with customer username
    public List<Order> findByUsername(String customer);
}