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
//import group_13.game_store.model.GameCopy;

import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.GameRepository;
//import group_13.game_store.repository.GameCopyRepository;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private GameRepository gameRepo;

    @Transactional
    public boolean returnOrder(int orderID, int gameID)  {
        // validation
        // check if order exists
        Order orderToReturn = orderRepo.findByOrderID(orderID);
        if (orderToReturn == null) {
            throw new IllegalArgumentException("No order with order ID " + orderID + ".");
        }
        
        // check if game exists
        Game gameToReturn = gameRepo.findByGameID(gameID);
        if (gameToReturn == null) {
            throw new IllegalArgumentException("No game with game ID " + gameID + ".");
        }

        // check if 7 days passed after the purchas within a set amount of milliseconds
        Date dateToPotentiallyReturn = Date.valueOf(LocalDate.now());
        Date dateOfPurchase = orderToReturn.getPurchaseDate();
        long millisecondsElapsedSincePurchase = dateToPotentiallyReturn.getTime() - dateOfPurchase.getTime();
        long millisecondsInDay = 1000 * 60 * 60 * 24;
        long daysPassedSincePurchase = millisecondsElapsedSincePurchase/millisecondsInDay;
        if (daysPassedSincePurchase <= 7) {
             // increment stock count of the Game
            int currentStockOfGame = gameToReturn.getStock();
            gameToReturn.setStock(currentStockOfGame + 1);
            // modify isReturned status of the Oredr
            orderToReturn.setIsReturned(true);
            // add returnDate of Order
            orderToReturn.setReturnDate(dateToPotentiallyReturn);
            return true;
        } 
        
        // default response if purchase was not made within 7 days
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
