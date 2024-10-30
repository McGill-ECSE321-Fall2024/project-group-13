package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    // allows instantiation of an Owner instance that is stored in the local database by its unique username as its unique ID
    public Owner findByUsername(String ownerUsername);
}
