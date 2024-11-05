package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
    // allows instantiation of an UserAccount instance that is stored in the local database by its unique username acting as its unique ID
    public UserAccount findByUsername(String username);

    // allows instantiation of an UserAccount instance that is stored in the local database by its email address
    public UserAccount findByEmail(String email);
}