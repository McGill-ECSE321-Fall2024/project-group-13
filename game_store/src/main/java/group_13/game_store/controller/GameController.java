package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.PromotionListResponseDto;
import group_13.game_store.dto.PromotionRequestDto;
import group_13.game_store.dto.PromotionResponseDto;
import group_13.game_store.dto.ReplyRequestDto;
import group_13.game_store.dto.ReplyResponseDto;
import group_13.game_store.dto.ReviewListResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.ReviewService;

@RestController
public class GameController {
    @Autowired
    BrowsingService browsingService;

    @Autowired
    GameStoreManagementService gameStoreManagementService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    AccountService accountService;

    /*
     * /games/reviews [GET]
     */
    @GetMapping("/games/reviews")
    public ReviewListResponseDto getReviews() {
        // Return a list of all reviews via the ReviewListResponseDto
        return new ReviewListResponseDto(reviewService.getAllReviews());
    }

    /*
     * /games/{gameID}/reviews [GET, POST]
     */
    @GetMapping("/games/{gameID}/reviews")
    public ReviewListResponseDto getReviewsByGame(@PathVariable int gameID) {
        // Return a list of reviews associated with a game via the ReviewListResponseDto
        return new ReviewListResponseDto(reviewService.getAllReviewsForGame(gameID));
    }

    @PostMapping("/games/{gameID}/reviews?loggedInUser={loggedInUsername}")
    public ReviewResponseDto createReview(@PathVariable int gameID,
            @RequestParam String loggedInUsername,
            @RequestBody ReviewRequestDto request) {
        // Check if the user has permission to create a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new IllegalArgumentException("User does not have permission to create/update reviews.");
        }

        // Create a base review with the information from the request. It takes in the
        // logged in user's username as well as the current date on top of the request
        // information
        Review review = reviewService.createReview(request.getDescription(),
                request.getScore(),
                request.getLikedByCustomers(),
                // Date is automatically set to today when a review is created
                // CreateReview will not let you create a review if the user is not logged in as
                // a customer that has bought the game
                loggedInUsername,
                gameID);

        // Return the review as a response object
        return new ReviewResponseDto(review);
    }

    /*
     * /games/{id}/reviews/{reviewID} [GET, PUT]
     */
    @GetMapping("/games/{gameID}/reviews/{reviewID}")
    public ReviewResponseDto getReview(@PathVariable int reviewID) {
        // Return a review by its unique ID via the ReviewResponseDto
        return new ReviewResponseDto(reviewService.getReview(reviewID));
    }

    @PutMapping("/games/{gameID}/reviews/{reviewID}?loggedInUser={loggedInUsername}")
    public ReviewResponseDto updateReview(@PathVariable int reviewID,
            @RequestParam String loggedInUsername,
            @RequestBody ReviewRequestDto request) {
        // Check if the user has permission to update a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new IllegalArgumentException("User does not have permission to create/update reviews.");
        }

        Review updatedReview = reviewService.updateReview(reviewID, request.getDescription(), request.getScore(),
                request.getLikedByCustomers(), loggedInUsername);

        return new ReviewResponseDto(updatedReview);
    }

    /*
     * /games/{id}/reviews/{reviewID}/reply [GET, POST]
     */
    @GetMapping("/games/gameID/reviews/{reviewID}/reply")
    public ReplyResponseDto getReplyToReview(@PathVariable int reviewID) {
        // Reply to a review by its unique ID and return the review as a response object

        Reply reply = reviewService.getReplyByReview(reviewID);

        return new ReplyResponseDto(reply);
    }

    @PostMapping("/games/{gameID}/reviews/{reviewID}/reply?loggedInUser={loggedInUsername}")
    public ReplyResponseDto replyToReview(@PathVariable int reviewID, @RequestParam String loggedInUsername,
            @RequestBody ReplyRequestDto request) {

        // Check if the user has permission to reply to reviews
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User does not have permission to reply to a reviews.");
        }

        // Reply to a review by its unique ID and return the review as a response
        // object, date gets automatically set to the current date when reply to review
        // is called
        Reply reply = reviewService.replyToReview(reviewID, loggedInUsername, request.getText());

        return new ReplyResponseDto(reply);
    }

    /*
     * /games/promotions [GET, POST]
     */
    @GetMapping("/games/promotions")
    public PromotionListResponseDto getPromotions() {
        // Return a list of all promotions via the PromotionListResponseDto
        return new PromotionListResponseDto(gameStoreManagementService.getAllPromotions());
    }

    @PostMapping("/games/promotions?loggedInUser={loggedInUsername}")
    public PromotionResponseDto createPromotion(@RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request) {

        // Check if the user has permission to create a promotion
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User does not have permission to create promotions.");
        }

        // Create a promotion with the information from the request
        Promotion promotion = gameStoreManagementService.addPromotion(loggedInUsername,
                request.getPercentage(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

    /*
     * /games/{gameID}/promotions [GET, POST]
     */
    @GetMapping("/games/{gameID}/promotions")
    public PromotionListResponseDto getPromotionsByGame(@PathVariable int gameID) {

        // Return a list of promotions associated with a game via the
        // PromotionListResponseDto
        return new PromotionListResponseDto(gameStoreManagementService.getAllGamePromotions(gameID));
    }

    @PostMapping("/games/{gameID}/promotions?loggedInUser={loggedInUsername}")
    public PromotionResponseDto addPromotionToGame(@PathVariable int gameID, @RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request) {
        // Check if the user has permission to add a promotion to a game
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User does not have permission to add promotions to a game.");
        }

        // Add a promotion to a game by its unique ID
        Promotion promotion = gameStoreManagementService.addPromotion(loggedInUsername,
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

    @PutMapping("/games/promotions/{promotionID}?loggedInUser={loggedInUsername}")
    public PromotionResponseDto updatePromotion(@PathVariable int promotionID, @RequestParam String loggedInUsername,
            @RequestBody PromotionRequestDto request
    ) {

        // Check if the user has permission to update promotions 
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User does not have permission to add promotions to a game.");
        }

        // Update a promotion by its unique ID if it does not exist we get an error
        Promotion promotion = gameStoreManagementService.updatePromotion(promotionID,
                loggedInUsername,
                request.getPercentage(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTitle(),
                request.getDescription());

        // Return the promotion as a response object
        return new PromotionResponseDto(promotion);
    }

    // Not implemented in the service layer yet
    // @DeleteMapping("/games/promotions/{promotionID}?loggedInUser={loggedInUsername}")
    // public void deletePromotion(@PathVariable int promotionID, @RequestParam
    // String loggedInUsername){
    // //Delete a promotion by its unique ID
    // gameStoreManagementService.deletePromotion(loggedInUsername, promotionID);
    // }

}
