package group_13.game_store.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group_13.game_store.model.Owner;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.service.AccountService;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    
    @Autowired
    private OwnerRepository ownerRepo;

    @PostConstruct
    public void initializeData() {
        // Check if the default owner account already exists, and if not, create it
        if (ownerRepo.findByUsername("owner") == null) {
            String hashedPassword = AccountService.hashPassword("ownerPassword");
            Owner owner = new Owner("Owner", "owner", "owner@gmail.com", hashedPassword, "123-456-7890");
            
            // Save to database so that there is a default owner
            ownerRepo.save(owner);
        }
    }
}
