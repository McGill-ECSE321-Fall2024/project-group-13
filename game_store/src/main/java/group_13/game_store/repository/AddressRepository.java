package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    // allows instantiation of an Address instance that is stored in the local database by its unique ID
    public Address findByAddressID(int addressID);
}
