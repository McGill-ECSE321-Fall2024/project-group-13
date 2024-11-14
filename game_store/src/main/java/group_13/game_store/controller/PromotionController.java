package group_13.game_store.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.PromotionListResponseDto;
import group_13.game_store.dto.PromotionRequestDto;
import group_13.game_store.dto.PromotionResponseDto;
import group_13.game_store.model.Game;
import group_13.game_store.model.Promotion;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.ReviewService;

@RestController
public class PromotionController {
    @Autowired
    BrowsingService browsingService;

    @Autowired
    GameStoreManagementService gameStoreManagementService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    AccountService accountService;


    /*
     * /games/promotions [GET, POST]
     */

    /**
     * Get all promotions in the system (Owner only)
     * 
     * @param loggedInUsername The username of the user who is logged in
     * 
     * @return A list of all promotions in the system
     */
    @GetMapping("/games/promotions")
    public PromotionListResponseDto getPromotions(@RequestParam String loggedInUsername) {
        // Check if the user has permission to see all promotions
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User does not have permission to view promotions.");
        }

        // Return a list of all promotions via the PromotionListResponseDto
        return new PromotionListResponseDto(gameStoreManagementService.getAllPromotions());
    }

    /**
     * Create a new promotion in the system (Owner only)
     * 
     * @param loggedInUsername The username of the user who is logged in
     * @param request The request object containing the information for the new promotion
     * 
     * @return The newly created promotion
     */
    @PostMapping("/games/promotions")
    public PromotionResponseDto createPromotion(@RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request) {

        // Check if the user has permission to create a promotion
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to create promotions.");
        }

        // Create a promotion with the information from the request
        Promotion promotion = gameStoreManagementService.addPromotion(
                request.getPercentage(),
                Date.valueOf(request.getStartDate()),
                Date.valueOf(request.getEndDate()),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object 
        return new PromotionResponseDto(promotion);
    }

    /*
     * /games/{gameID}/promotions [GET, POST]
     */

    /**
     * Get the promotion for a specific game
     * 
     * @param gameID The ID of the game to get the promotion for
     * @param loggedInUsername The username of the user who is logged in
     * 
     * @return The promotion for the game
     */
    @GetMapping("/games/{gameID}/promotions")
    public PromotionResponseDto getPromotionByGame(@PathVariable int gameID, @RequestParam String loggedInUsername) {
        // This will automoatically thow an error if the gamne is not found
        Game game = browsingService.getGameById(gameID);

        Promotion mainPromotion = game.getPromotion();
    
        return new PromotionResponseDto(mainPromotion);
    }

    /**
     * Add a promotion to a game (Owner only)
     * 
     * @param gameID The ID of the game to add the promotion to
     * @param promotionID The ID of the promotion to add to the game
     * @param loggedInUsername The username of the user who is logged in
     * 
     * @return The promotion that was added to the game
     */
    @PostMapping("/games/{gameID}/promotions/{promotionID}")
    public PromotionResponseDto addPromotionToGame(@PathVariable int gameID, @PathVariable int promotionID, @RequestParam String loggedInUsername) {
        // Check if the user has permission to add a promotion to a game
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to add promotions to a game.");
        }

        // Add a promotion to a game by their unique IDs
        Promotion promotion = gameStoreManagementService.addPromotionToGame(promotionID, gameID);

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

    /**
     * Remove a promotion from a game (Owner only)
     * 
     * @param gameID The ID of the game to remove the promotion from
     * @param promotionID The ID of the promotion to remove from the game
     * @param loggedInUsername The username of the user who is logged in
     */
    @DeleteMapping("/games/{gameID}/promotions/{promotionID}")
    public void removePromotionFromGame(@PathVariable int gameID, @PathVariable int promotionID, @RequestParam String loggedInUsername) {
        // Check if the user has permission to remove a promotion from a game
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to remove promotions from a game.");
        }
        
        // Remove a promotion from a game by their unique IDs
        gameStoreManagementService.removePromotionFromGame(gameID, promotionID);
    }

    /*
     * /games/promotions/{promotionID} [GET, PUT, DELETE]
     */

    /**
     * Get a promotion by its unique ID
     * 
     * @param promotionID The ID of the promotion to get
     * 
     * @return The promotion with the given ID
     */
    @GetMapping("/games/promotions/{promotionID}")
    public PromotionResponseDto getPromotionById(@PathVariable int promotionID) {

        Promotion promotion = gameStoreManagementService.getPromotion(promotionID);

        return new PromotionResponseDto(promotion);
    }

    /**
     * Update a promotion by its unique ID (Owner only)
     * 
     * @param promotionID The ID of the promotion to update
     * @param loggedInUsername The username of the user who is logged in
     * @param request The request object containing the new information for the promotion
     * 
     * @return The updated promotion
     */
    @PutMapping("/games/promotions/{promotionID}")
    public PromotionResponseDto updatePromotion(@PathVariable int promotionID, @RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request
    ) {

        // Check if the user has permission to update promotions 
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to update promotions.");
        }

        // Update a promotion by its unique ID if it does not exist we get an error
        Promotion promotion = gameStoreManagementService.updatePromotion(promotionID,
                request.getPercentage(),
                Date.valueOf(request.getStartDate()),
                Date.valueOf(request.getEndDate()),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

    /**
     * Delete a promotion by its unique ID (Owner only)
     * 
     * @param promotionID The ID of the promotion to delete
     * @param loggedInUsername The username of the user who is logged in
     */
    @DeleteMapping("/games/promotions/{promotionID}")
    public void deletePromotion(@PathVariable int promotionID, 
        @RequestParam String loggedInUsername
    ){
        // Check if the user has permission to delete promotions 
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to delete promotions.");
        }

        //Delete a promotion by its unique ID
        gameStoreManagementService.deletePromotion(promotionID);
    }
}
