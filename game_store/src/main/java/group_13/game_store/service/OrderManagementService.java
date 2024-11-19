package group_13.game_store.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCopy;
//import group_13.game_store.model.GameCopy;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCopyRepository;
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

    @Autowired
    private GameCopyRepository copyRepo;


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
    public Order returnOrder(int orderID, Date dateToReturn)  {
        // validation
        // check if order exists
        // @@@@@@@@@@@@@@@@@@@2 CANT SEEM TO FIND ORDER THAT I SAVED HERE
        Order orderToReturn = orderRepo.findByOrderID(orderID);
        if (orderToReturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order with order ID " + String.valueOf(orderID) + ".");
        }

         // check if order was already returned
         if (orderToReturn.isIsReturned() == true) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order " + String.valueOf(orderID) + " was already returned.");
        }

        List<GameCopy> gameCopiesOfOrder = copyRepo.findByOrder_OrderID(orderID);

        // check if games from this order exist
        /* for (GameCopy orderCopy : gameCopiesOfOrder) {
            Game gameToReturn = gameRepo.findByGameID(orderCopy.getGame().getGameID());
            if (gameToReturn == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game with game ID " + gameToReturn.getGameID() + ".");
            }
        } */

        // check if 7 days passed after the purchas within a set amount of milliseconds
        //Date dateToReturn = Date.valueOf(LocalDate.now());
        Date dateOfPurchase = orderToReturn.getPurchaseDate();
        long millisecondsElapsedSincePurchase = dateToReturn.getTime() - dateOfPurchase.getTime();
        long millisecondsInDay = 1000 * 60 * 60 * 24;
        long daysPassedSincePurchase = millisecondsElapsedSincePurchase/millisecondsInDay;
        if (daysPassedSincePurchase <= 7) {

            for (GameCopy copy : gameCopiesOfOrder) {
                Game gameStockToUpdate = gameRepo.findByGameID(copy.getGame().getGameID());
                int gameCurrentStock = gameStockToUpdate.getStock();
                // increment stock count of the Game
                gameStockToUpdate.setStock(gameCurrentStock + 1);
                // save these changes in the database after each iteration
                gameStockToUpdate = gameRepo.save(gameStockToUpdate);
            }

            // add returnDate of Order and modifying the isReturned status of the Order
            orderToReturn.setReturnDate(dateToReturn);

            // save these changes in the database
            orderToReturn = orderRepo.save(orderToReturn);

            return orderToReturn;
        } 
        
        // default response if purchase was not made within 7 days
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order " + String.valueOf(orderID) + " cannot be returned, because 7 days have already passed since its purchase.");
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
