package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    public Owner findByUsername(String ownerUsername);
}
