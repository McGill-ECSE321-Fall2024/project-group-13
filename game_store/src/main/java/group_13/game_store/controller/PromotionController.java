package group_13.game_store.controller;

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
    @GetMapping("/games/promotions")
    public PromotionListResponseDto getPromotions(@RequestParam String loggedInUsername) {
        // Check if the user has permission to see all promotions
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User does not have permission to view promotions.");
        }

        // Return a list of all promotions via the PromotionListResponseDto
        return new PromotionListResponseDto(gameStoreManagementService.getAllPromotions());
    }

    @PostMapping("/games/promotions")
    public PromotionResponseDto createPromotion(@RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request) {

        // Check if the user has permission to create a promotion
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to create promotions.");
        }

        // Check if the request body is empty
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is empty.");
        }

        // Check if the percentage is between 0 and 100
        if (request.getPercentage() < 0 || request.getPercentage() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percentage must be between 0 and 100.");
        }

        // Check if the start date and end date are provided
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date must be provided.");
        }

        // Check if the title and description are provided
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must be provided.");
        }

        // Check if the description is provided
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be provided.");
        }

        // Create a promotion with the information from the request
        Promotion promotion = gameStoreManagementService.addPromotion(
                request.getPercentage(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTitle(),
                request.getDescription());

        // Check if the promotion could not be created
        if (promotion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Promotion could not be created.");
        }

        // Return the promotion as a response object 
        return new PromotionResponseDto(promotion);
    }

    /*
     * /games/{gameID}/promotions [GET, POST]
     */
    @GetMapping("/games/{gameID}/promotions")
    public PromotionListResponseDto getPromotionsByGame(@PathVariable int gameID, @RequestParam String loggedInUsername) {
        // Check if the user has permission to add a promotion to a game
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);
        
        if (!isOwner) {
            //If it is not the owner return only the valid promotions
            return new PromotionListResponseDto(browsingService.getAllValigPromotions(gameID));

        } else {
            //If it is the owner return all the promotions even the inactive ones
            return new PromotionListResponseDto(gameStoreManagementService.getAllGamePromotions(gameID));
        }
    }

    @PostMapping("/games/{gameID}/promotions")
    public PromotionResponseDto addPromotionToGame(@PathVariable int gameID, @RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request) {
        // Check if the user has permission to add a promotion to a game
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to add promotions to a game.");
        }

        // Add a promotion to a game by its unique ID
        Promotion promotion = gameStoreManagementService.addPromotion(
                request.getPercentage(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

    /*
     * /games/promotions/{promotionID} [GET, PUT, DELETE]
     */
    @GetMapping("/games/promotions/{promotionID}")
    public PromotionResponseDto getPromtotionById(@PathVariable int promotionID) {

        Promotion promotion = gameStoreManagementService.getPromotion(promotionID);

        return new PromotionResponseDto(promotion);
    }

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
                request.getStartDate(),
                request.getEndDate(),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

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
