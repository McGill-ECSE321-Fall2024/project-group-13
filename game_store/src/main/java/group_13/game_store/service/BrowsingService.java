package group_13.game_store.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import jakarta.transaction.Transactional;
import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import java.util.List;

@Service
public class BrowsingService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // ************************** EMPLOYEE AND OWNER BROWSING **************************

    // Gets all the games in the database
    @Transactional
    public Iterable<Game> getAllGames() {
        Iterable<Game> allGames = gameRepository.findAll();
        
        if (!allGames.iterator().hasNext()) {
            // Indicate that there are no games
        }
        return allGames;
    }

    // Gets a game by its ID
    @Transactional
    public Game getGameById(int gameID) {
        Game game = gameRepository.findByGameID(gameID);

        if (game == null) {
            // Indicate that the game does not exist
        }

        return game;
    }

    // Gets all the games in a category
    @Transactional
    public List<Game> getGamesByCategoryName(String category) {
        List<Game> games = gameRepository.findByCategory_Name(category);

        if (games.isEmpty()) {
            // Indicate that there are no games in the category
        }

        return games;
    }

    // Gets all the games with a title starting with the given string
    @Transactional
    public List<Game> getGamesByTitleStartingWith(String title) {
        List<Game> games = gameRepository.findByTitleStartingWith(title);

        if (games.isEmpty()) {
            // Indicate that there are no games with the given name
        }

        return games;
    }

    // ************************** CUSTOMER BROWSING **************************

    // Gets all the games in the database that are available
    @Transactional
    public List<Game> getAllAvailableGames() {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);
        List<Game> games = gameRepository.findByStockGreaterThanAndStatusIn(0, visibleStatuses);

        if (games.isEmpty()) {
            // Indicate that there are no games available
        }

        return games;
    }

    // Gets a game by its ID if available
    @Transactional
    public Game getAvailableGameById(int gameID) {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);

        Game game = gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(gameID, 0, visibleStatuses);

        if (game == null) {
            // Indicate that the game does not exist
        }

        return game;
    }

    // Gets all the games in a category that are available
    @Transactional
    public List<Game> getAvailableGamesByCategoryName(String category) {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);
        
        List<Game> games = gameRepository.findByCategory_NameAndStockGreaterThanAndStatusIn(category, 0, visibleStatuses);

        if (games.isEmpty()){
            // Indicate that there are no available games in this category
        }

        return games;

    }

    // Gets all the games with a title starting with the given string
    @Transactional
    public List<Game> getAvailableGamesByTitleStartingWith(String title){
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);

        List<Game> games = gameRepository.findByTitleStartingWithAndStockGreaterThanAndStatusIn(title, 0, visibleStatuses);

        if (games.isEmpty()) {
            // Indicate that there are no available games in starting with this title
        }

        return games;
    }

     // ************************** CUSTOMER CART AND WISHLIST **************************

     // Allows a customer to add a game to their cart using the gameID
     @Transactional 
     public boolean addGameToCart(int gameID, String username, int quantity){
        // This method through throw an exeception if the game is not found / will come back to error handling
        Game addedGame = getAvailableGameById(gameID);

        // Check if the user exists and is a customer
        Customer loggedInCustomer = customerRepository.findByUsername(username);

        if (loggedInCustomer == null) {
            // indicate that there is an issue with the loggedIn customer (might not be needed)
            return false; // come back to this
        }

        if (quantity < 1) {
            // indicate invalid quantity
            return false; // come back to this
        }

        // Add the game to the customer's cart by creating a cartItem
        CartItem.Key cartItemKey = new CartItem.Key(loggedInCustomer, addedGame);
        CartItem addedCartItem = new CartItem(cartItemKey, quantity);

        cartItemRepository.save(addedCartItem);
        
        return true; // come back to this
     }

     // Returns the cart of a given customer
     public List<CartItem> getCustomerCartByUsername(String username){
        List<CartItem> customerCart = cartItemRepository.findByKey_CustomerAccount_Username(username);
        return customerCart;
     }

     // Remove Game from cart
     public boolean removeGameFromCart(String username, int gameID){
        // Check if game is in cart
        CartItem.Key cartItemKey = new CartItem.Key(customerRepository.findByUsername(username), gameRepository.findByGameID(gameID));
        CartItem cartItem = cartItemRepository.findByKey(cartItemKey);

        if (cartItem == null) {
            // Indicate that the game is not in the cart
            return false;
        }

        // Remove the game from the cart
        cartItemRepository.delete(cartItem);
        return true;

     }

    // Update the quantity of a game in the cart
    public boolean updateGameQuantityInCart(String username, int gameID, int newQuantity){
        // Check if game is in cart
        CartItem.Key cartItemKey = new CartItem.Key(customerRepository.findByUsername(username), gameRepository.findByGameID(gameID));
        CartItem cartItem = cartItemRepository.findByKey(cartItemKey);

        if (cartItem == null) {
            // Indicate that the game is not in the cart
            return false;
        }

        if (newQuantity < 1) {
            // Indicate invalid quantity
            return false;
        }

        // Update the quantity of the game in the cart
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return true;
    }


    

}
