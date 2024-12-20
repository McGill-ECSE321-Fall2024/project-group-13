package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.GameListResponseDto;
import group_13.game_store.dto.GameResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.ReviewService;

import group_13.game_store.model.Game;
import group_13.game_store.model.WishlistItem;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WishListController {

    // Services

    @Autowired
    BrowsingService browsingService;

    @Autowired
    GameStoreManagementService gameStoreManagementService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    AccountService accountService;

    /**
     * Get the wishlist for a customer (Customer only)
     * @param customerID the username of the customer
     * @return a list of games in the wishlist
     */
    @GetMapping("/customers/{customerID}/wishlist")
    public GameListResponseDto getWishList(@PathVariable String customerID) {

        // Check if the customer exists and is a valid customer
        if (!accountService.hasPermission(customerID, 1)) {
            // throw permission denied exception
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "only customers can access their wishlists");
        }

        // Get the wishlist items for the customer
        List<WishlistItem> wishlistItems = browsingService.getCustomerWishlistByUsername(customerID);

        List<GameResponseDto> games = new ArrayList<>();

        // for each wishlist item, get the game associated with it and create a
        // GameResponseDto and add it to the list
        for (WishlistItem item : wishlistItems) {
            // get the game associated with the wishlist item
            Game game = item.getKey().getGame();

            String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";
            // create a GameResponseDto
            GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                    game.getDescription(), game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(),
                    game.getStatus().toString(), game.getCategory().getCategoryID(), promotionTitle, 
                    game.getCategory().getName(), 
                    (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0, 
                    reviewService.getGameRating(game.getGameID()));

            // add the GameResponseDto to the list
            games.add(gameResponseDto);
        }

        return new GameListResponseDto(games);
    }

    /**
     * Add a game to the wishlist (Customer only)
     * @param customerID the username of the customer
     * @param gameID the id of the game to add to the wishlist
     * @return a GameResponseDto of the game added to the wishlist
     */
    @PutMapping("/customers/{customerID}/wishlist/{gameID}")
    public GameResponseDto addGameToWishlist(@PathVariable String customerID, @PathVariable int gameID) {

        // Check if the customer exists and is a valid customer
        if (!accountService.hasPermission(customerID, 1)) {
            // throw permission denied exception
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "only customers can access their wishlists");
        }

        // Use the browsing service to add the game to the wishlist
        browsingService.addGameToWishlist(gameID, customerID);

        // Get the game associated with the gameID
        Game game = browsingService.getGameById(gameID);

        String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";

        // Create a GameResponseDto
        GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(), game.getDescription(),
            game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus().toString(),
            game.getCategory().getCategoryID(), promotionTitle, game.getCategory().getName(), 
            (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0, reviewService.getGameRating(game.getGameID()));

        return gameResponseDto;
    }

     /**
      * Remove a game from the wishlist (Customer only)
      * @param customerID the username of the customer
      * @param gameID the id of the game to remove from the wishlist
      * @return the removed game from the wishlist
      */
    @DeleteMapping("/customers/{customerID}/wishlist/{gameID}")
    public GameResponseDto removeGameFromWishlist(@PathVariable String customerID, @PathVariable int gameID) {

        // Check if the customer exists and is a valid customer
        if (!accountService.hasPermission(customerID, 1)) {
            // throw permission denied exception
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "only customers can access their wishlists");
        }

        // Use the browsing service to remove the game from the wishlist
        browsingService.removeGameFromWishlist(customerID, gameID);

        // Get the game associated with the gameID
        Game game = browsingService.getGameById(gameID);

        String promotionTitle = (game.getPromotion() != null) ? game.getPromotion().getTitle() : "";

        // Create a GameResponseDto
        GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(), game.getDescription(),
                game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus().toString(),
                game.getCategory().getCategoryID(), promotionTitle, game.getCategory().getName(), 
                (game.getPromotion() != null) ? game.getPromotion().getPercentage() : 0, reviewService.getGameRating(game.getGameID()));

        return gameResponseDto;
    }

    /**
     * Clear the wishlist (Customer only)
     * @param customerID the username of the customer
     * @return an empty list / an empty wishlist
     */
    @DeleteMapping("/customers/{customerID}/wishlist")
    public GameListResponseDto clearWishlist(@PathVariable String customerID) {

        // Check if the customer exists and is a valid customer
        if (!accountService.hasPermission(customerID, 1)) {
            // throw permission denied exception
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "only customers can access their wishlists");
        }

        // Use the browsing service to clear the wishlist
        browsingService.clearWishlist(customerID);

       // return an empty list
        List<GameResponseDto> games = new ArrayList<>();

        return new GameListResponseDto(games);

    }

}
