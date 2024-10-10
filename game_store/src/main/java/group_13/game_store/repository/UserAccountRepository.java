package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
    public UserAccount findByUsername(String username);
}