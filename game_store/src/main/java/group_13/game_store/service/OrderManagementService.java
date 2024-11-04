package group_13.game_store.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;

import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.GameRepository;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private GameRepository gameRepo;

    @Transactional
    public boolean returnOrder(int orderID, int copyID, int gameID, Date aReturnDate)  {
        Date dateOfPurchase = Date.valueOf(LocalDate.now());
        // validation
            // check if order exists
            
            // check if copy exists

            // check if game exists

            // check if it purchase was made within previous 7 days


        // increment stock count of the Game
        // modify isReturned status of the GameCopy
        // add returnDate of GameCopy

        
        // default response
        return false;
    }

    @Transactional
    public List<Order> getOrderHistoryOfCustomer(String aCustomer) {
        // validation
        // check if customer exists
        // also checks if user is not an Owner or Employee
        Customer customerToLookup = customerRepo.findByUsername(aCustomer);
        if (customerToLookup == null) {
            // placeholder exception
            throw new IllegalArgumentException("No customer with username ID " + aCustomer + ".");
        }

        
        // get list of all orders associated with customer

            
            
        return null;
    }

    // might need another service method ...
    // findbyOrderID

}
