package group_13.game_store.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.sql.Date;
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
    public Order createOrder(Date aPurchaseDate, Date aReturnDate, Customer aCustomer) {
        // validation
            // check if customer exists

        // create order

        // save it to repo
        return null;
    }

    @Transactional
    public boolean returnOrder(int orderID, int copyID, int gameID, Date aReturnDate)  {
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
    public List<Order> getAllOrdersOfCustomer(String aCustomer) {
        // validation
            // check if customer exists
            // check if user is a customer
        return null;
    }

    // might need another service method ...

}
