package group_13.game_store.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.CartResponseDto;
import group_13.game_store.dto.GameResponseDto;
import group_13.game_store.dto.CartItemResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.ReviewService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import group_13.game_store.model.CartItem;
import group_13.game_store.model.Game;

import java.util.List;

@RestController
public class CartController {

    // Services
    @Autowired
    BrowsingService browsingService;

    @Autowired
    AccountService accountService;

    @Autowired
    ReviewService reviewService;

    /**
     * Get the cart of a customer by username (Customer only)
     * @param customerUsername the username of the customer
     * @return the customer's cart ie. a list of games and the subtotal price
     */
    @GetMapping("/customers/{customerUsername}/cart")
    public CartResponseDto getCart(@PathVariable String customerUsername) {
        // check if the customer exists and is logged in

        boolean isCustomer = accountService.hasPermission(customerUsername, 1);

        if (!isCustomer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only customers can access their own cart");
        }

        // Get the users cart (list of games)
        List<CartItem> cartItems = browsingService.getCustomerCartByUsername(customerUsername);

        System.out.println(cartItems);

        List<GameResponseDto> games = new ArrayList<>();

        // for each cart item, get the game associated with it and create a
        // GameResponseDto and add it to the list
        for (CartItem item : cartItems) {
            // get the game associated with the cart item
            Game game = item.getKey().getGame();

            // create a GameResponseDto
            String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";

            CartItemResponseDto gameResponseDto = new CartItemResponseDto(game.getGameID(), game.getTitle(),
                    game.getDescription(), game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(),
                    game.getStatus().toString(), game.getCategory().getCategoryID(), promotionTitle, 
                    game.getCategory().getName(), 
                    (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0, 
                    reviewService.getGameRating(game.getGameID()), item.getQuantity());

            // add the GameResponseDto to the list
            games.add(gameResponseDto);
        }

        // calculate the total price of the cart
        browsingService.getCartSubtotalPrice(customerUsername);

        // convert data into a CartResponseDto
        CartResponseDto cartResponseDto = new CartResponseDto(games,
                browsingService.getCartSubtotalPrice(customerUsername));

        return cartResponseDto;

    }

    /**
     * Clear the cart of a customer by username (Customer only)
     * @param customerUsername the username of the customer
     * @return the customer's cart which should be empty and have a subtotal price of 0
     */
    @DeleteMapping("/customers/{customerUsername}/cart")
    public CartResponseDto clearCart(@PathVariable String customerUsername) {
        // check if the customer exists and is logged in
        boolean isCustomer = accountService.hasPermission(customerUsername, 1);

        if (!isCustomer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only customers can clear their own cart");
        }

        // clear the cart
        browsingService.clearCart(customerUsername);

        // get the users cart (list of games)
        List<CartItem> cartItems = browsingService.getCustomerCartByUsername(customerUsername);

        List<GameResponseDto> games = new ArrayList<>();

        // for each cart item, get the game associated with it and create a
        // GameResponseDto and add it to the list
        for (CartItem item : cartItems) {
            // get the game associated with the cart item
            Game game = item.getKey().getGame();

            // create a GameResponseDto
            String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";
            int promotionPercentage = (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0;

            GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                    game.getDescription(), game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(),
                    game.getStatus().toString(), game.getCategory().getCategoryID(), promotionTitle,
                    game.getCategory().getName(), promotionPercentage, reviewService.getGameRating(game.getGameID()));

            // add the GameResponseDto to the list
            games.add(gameResponseDto);

        }

        // calculate the total price of the cart
        browsingService.getCartSubtotalPrice(customerUsername);

        // convert data into a CartResponseDto
        CartResponseDto cartResponseDto = new CartResponseDto(games,
                browsingService.getCartSubtotalPrice(customerUsername));

        return cartResponseDto; // review if this is needed

    }

    /**
     * Add a game to the cart of a customer by username (Customer only)
     * @param customerUsername the username of the customer
     * @param gameID the id of the game
     * @param quantity the quantity of the game
     * @return the game that was added to the cart
     */
    @PutMapping("/customers/{customerUsername}/cart/{gameID}")
    public GameResponseDto addToCart(@PathVariable String customerUsername, @PathVariable int gameID,
            @RequestParam int quantity) {
        // check if the customer exists and is logged in
        boolean isCustomer = accountService.hasPermission(customerUsername, 1);

        if (!isCustomer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to access this user's cart");
        }

        // add the game to the cart
        browsingService.addGameToCart(gameID, customerUsername, quantity);

        // get the game associated with the gameID
        Game game = browsingService.getGameById(gameID);

        String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";

        // create a GameResponseDto
        GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(), game.getDescription(),
                game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus().toString(),
                game.getCategory().getCategoryID(), promotionTitle, game.getCategory().getName(),
                (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0, reviewService.getGameRating(game.getGameID()));

        return gameResponseDto;

    }

    /**
     * Remove a game from the cart of a customer by username (Customer only)
     * @param customerUsername the username of the customer
     * @param gameID the id of the game
     * @return the game that was removed from the cart
     */
    @DeleteMapping("/customers/{customerUsername}/cart/{gameID}")
    public GameResponseDto removeFromCart(@PathVariable String customerUsername, @PathVariable int gameID) {
        // check if the customer exists and is logged in
        boolean isCustomer = accountService.hasPermission(customerUsername, 1);

        if (!isCustomer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to access this user's cart");
        }

        // remove the game from the cart
        browsingService.removeGameFromCart(customerUsername, gameID);

        // get the game associated with the gameID
        Game game = browsingService.getGameById(gameID);

        // create a GameResponseDto
        String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";
        GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(), game.getDescription(),
                game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus().toString(),
                game.getCategory().getCategoryID(), promotionTitle, game.getCategory().getName(), (game.getPromotion() != null ? game.getPromotion().getPercentage() : 0), reviewService.getGameRating(game.getGameID()));

        return gameResponseDto; // review if this is needed

    }

    /**
     * Update the quantity of a game in the cart of a customer by username (Customer only)
     * @param customerUsername the username of the customer
     * @param gameID the id of the game
     * @param quantity the quantity of the game
     * @return the game that was updated in the cart
     */
    @PutMapping("/customers/{customerUsername}/cart/{gameID}/quantity/{quantity}") 
                                                                 // double check
    public GameResponseDto updateQuantityInCart(@PathVariable String customerUsername, @PathVariable int gameID,
            @PathVariable int quantity) {
        // check if the customer exists and is logged in
        boolean isCustomer = accountService.hasPermission(customerUsername, 1);

        if (!isCustomer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to access this user's cart");
        }

        // update the quantity of the game in the cart
        browsingService.updateGameQuantityInCart(customerUsername, gameID, quantity);

        // get the game associated with the gameID
        Game game = browsingService.getGameById(gameID);

        String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";
        // create a GameResponseDto
        GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(), game.getDescription(),
                game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus().toString(),
                game.getCategory().getCategoryID(), promotionTitle, game.getCategory().getName(), (game.getPromotion() != null ? game.getPromotion().getPercentage() : 0), reviewService.getGameRating(game.getGameID()));

        return gameResponseDto; 

    }

}
