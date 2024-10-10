package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    public Customer findByUsername(String customerUsername);
}