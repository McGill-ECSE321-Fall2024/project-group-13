package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    // allows instantiation of an Customer instance that is stored in the local database by its unique username acting as its unique ID
    public Customer findByUsername(String customerUsername);
}