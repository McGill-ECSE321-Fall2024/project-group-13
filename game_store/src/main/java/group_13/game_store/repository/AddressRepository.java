package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    public Address findByAddressID(int addressID);
}
