package group_13.game_store.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
//import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;
//import group_13.game_store.model.GameCopy;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.GameRepository;
//import group_13.game_store.repository.GameCopyRepository;
import org.springframework.http.HttpStatus;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private GameRepository gameRepo;


    @Transactional
    public Order getOrderById(int orderId) {
        Order order = orderRepo.findByOrderID(orderId);
        if (order == null) {
            //indicate no order was found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order with order ID " + orderId + ".");
        }
        return order;
    }
    

    @Transactional
    public Order returnOrder(int orderID, int gameID, Date dateToReturn)  {
        // validation
        // check if order exists
        Order orderToReturn = orderRepo.findByOrderID(orderID);
        if (orderToReturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order with order ID " + orderID + ".");
        }
        // check if game exists
        Game gameToReturn = gameRepo.findByGameID(gameID);
        if (gameToReturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game with game ID " + gameID + ".");
        }
        // check if order was already returned
        if (orderToReturn.isIsReturned() == true) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order " + orderID + " was already returned.");
        }

        // check if 7 days passed after the purchas within a set amount of milliseconds
        //Date dateToReturn = Date.valueOf(LocalDate.now());
        Date dateOfPurchase = orderToReturn.getPurchaseDate();
        long millisecondsElapsedSincePurchase = dateToReturn.getTime() - dateOfPurchase.getTime();
        long millisecondsInDay = 1000 * 60 * 60 * 24;
        long daysPassedSincePurchase = millisecondsElapsedSincePurchase/millisecondsInDay;
        if (daysPassedSincePurchase <= 7) {
             // increment stock count of the Game
            int currentStockOfGame = gameToReturn.getStock();
            gameToReturn.setStock(currentStockOfGame + 1);
            // add returnDate of Order and modifying the isReturned status of the Order
            orderToReturn.setReturnDate(dateToReturn);

            // save these changes in the database
            gameToReturn = gameRepo.save(gameToReturn);
            orderToReturn = orderRepo.save(orderToReturn);

            return orderToReturn;
        } 
        
        // default response if purchase was not made within 7 days
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order " + orderID + " cannot be returned, because 7 days have already passed since its purchase.");
    }

    @Transactional
    public List<Order> getOrderHistoryOfCustomer(String aCustomer) {
        // validation
        // check if customer exists
        // also checks if user is not an Owner or Employee
        Customer customerToLookup = customerRepo.findByUsername(aCustomer);
        if (customerToLookup == null) {
            // placeholder exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer with username ID " + aCustomer + ".");
        }

        
        // get list of all orders associated with customer

        //will need to get all customers
        List<Order> listOfCustomerOrder = orderRepo.findByCustomer_Username(aCustomer);
        return listOfCustomerOrder;
    }
}
