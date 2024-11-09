package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.ReplyRequestDto;
import group_13.game_store.dto.ReplyResponseDto;
import group_13.game_store.dto.ReviewListResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.ReviewService;

@RestController
public class ReviewController {
    
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
    public ReviewListResponseDto getReviews(@RequestParam boolean isPendingReply,
    @RequestParam String loggedInUsername) {
        if(!accountService.hasPermission(loggedInUsername, 3)) {
            throw new IllegalArgumentException("User does not have permission to see all reviews.");
        }

        if (!isPendingReply) {

            // Return a list of all reviews via the ReviewListResponseDto
            return new ReviewListResponseDto(reviewService.getAllReviews());
        } else {

            // Return a list of all reviews that have not been replied to via the ReviewListResponseDto
            return new ReviewListResponseDto(reviewService.getUnansweredReviews());
        }
    }  

    /*
     * /games/{gameID}/reviews [GET, POST]
     */
    @GetMapping("/games/{gameID}/reviews")
    public ReviewListResponseDto getReviewsByGame(@PathVariable int gameID) {
        // Return a list of reviews associated with a game via the ReviewListResponseDto
        return new ReviewListResponseDto(reviewService.getAllReviewsForGame(gameID));
    }

    @PostMapping("/games/{gameID}/reviews")
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

    @PutMapping("/games/{gameID}/reviews/{reviewID}")
    public ReviewResponseDto updateReview(@PathVariable int reviewID,
            @RequestParam String loggedInUsername,
            @RequestBody ReviewRequestDto request) {
        // Check if the user has permission to update a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new IllegalArgumentException("User does not have permission to create/update reviews.");
        }

        Review updatedReview = reviewService.updateReview(reviewID, request.getDescription(), request.getScore(), loggedInUsername);

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

    @PostMapping("/games/{gameID}/reviews/{reviewID}/reply")
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
}
