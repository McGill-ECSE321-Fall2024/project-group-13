package group_13.game_store.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Owner;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.service.AccountService;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    
    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerRepository customerRepo;

    @PostConstruct
    public void initializeData() {
        // Check if the default owner account already exists, and if not, create it
        if (ownerRepo.findByUsername("owner") == null) {
            String hashedPassword = accountService.hashPassword("own3rPassword");
            Owner owner = new Owner("Owner", "owner", "owner@gmail.com", hashedPassword, "123-456-7890");
            
            // Save to database so that there is a default owner
            ownerRepo.save(owner);
        }

        if (customerRepo.findByUsername("defaultCustomer") == null) {
            String hashedPassword = accountService.hashPassword("custom3rPassword");
            Customer defaultCustomer = new Customer("Default Customer", "defaultCustomer", "customer@gmail.com", hashedPassword, "123-456-7890");
            customerRepo.save(defaultCustomer);
        }
    }
}
