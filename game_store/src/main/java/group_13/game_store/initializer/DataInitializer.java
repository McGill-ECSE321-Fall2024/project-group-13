package group_13.game_store.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    
    @Autowired
    private UserAccountRepository userRepo;

    @PostConstruct
    public void initializeData() {
        // Check if the default owner account already exists, and if not, create it
        if (userRepo.findByUsername("owner") == null) {
            UserAccount owner = new UserAccount("Owner", "owner", "owner@gmail.com", "ownerPassword", "123-456-7890");
            
            // Save to database so that there is a default owner
            userRepo.save(owner);
        }
    }
}
