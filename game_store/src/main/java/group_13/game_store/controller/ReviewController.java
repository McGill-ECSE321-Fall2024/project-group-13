package group_13.game_store.controller;

import java.util.List;
import java.util.stream.Collectors;

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

     /**
      * Get all reviews or all reviews that have not been replied to (Owner only)
      *
      * @param isPendingReply true if you want to get all reviews that have not been replied to and false if you want to get all reviews
      * @param loggedInUsername the username of the user that is logged in
      *
      * @return a list of all reviews or a list of reviews that have not been replied to
      */
    @GetMapping("/games/reviews")
    public ReviewListResponseDto getReviews(@RequestParam boolean isPendingReply,
    @RequestParam String loggedInUsername) {
        if(!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to view reviews.");
        }

        if (!isPendingReply) {

            // Return a list of ResponseDtos via the ReviewListResponseDto
            List<ReviewResponseDto> reviewDtos = reviewService.getAllReviews()
                .stream() 
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
            ReviewListResponseDto responseDto = new ReviewListResponseDto(reviewDtos);
            return responseDto;
        } else {

            // Return a list of all reviews that have not been replied to via the ReviewListResponseDto
            List<ReviewResponseDto> reviewDtos = reviewService.getUnansweredReviews()
                .stream() 
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
            ReviewListResponseDto responseDto = new ReviewListResponseDto(reviewDtos);
            return responseDto;
        }
    }  

    /*
     * /games/{gameID}/reviews [GET, POST]
     */

    /**
     * Get all reviews for a specific game by gameID
     * 
     * @param gameID the ID of the game to get reviews for
     * 
     * @return a list of reviews for the game
     */
    @GetMapping("/games/{gameID}/reviews")
    public ReviewListResponseDto getReviewsByGame(@PathVariable int gameID) {
        // Return a list of reviews associated with a game via the ReviewListResponseDto
        List<ReviewResponseDto> reviewDtos = reviewService.getAllReviewsForGame(gameID)
            .stream() 
            .map(ReviewResponseDto::new)
            .collect(Collectors.toList());
        ReviewListResponseDto responseDto = new ReviewListResponseDto(reviewDtos);
        return responseDto;
    }

    /**
     * Create a review for a specific game by gameID (Customer and above only)
     * 
     * @param gameID the ID of the game to create a review for
     * @param loggedInUsername the username of the user that is logged in
     * @param request the request object containing the review information. It should contain the description and score of the review
     * 
     * @return the review that was created
     */
    @PostMapping("/games/{gameID}/reviews")
    public ReviewResponseDto createReview(@PathVariable int gameID,
            @RequestParam String loggedInUsername,
            @RequestBody ReviewRequestDto request) {
        // Check if the user has permission to create a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to create/update reviews.");
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

    /**
     * Get a review by its unique ID 
     * 
     * @param reviewID the ID of the review to get
     * 
     * @return the review with the given ID
     */
    @GetMapping("/games/reviews/{reviewID}")
    public ReviewResponseDto getReview(@PathVariable int reviewID) {
        // Return a review by its unique ID via the ReviewResponseDto
        return new ReviewResponseDto(reviewService.getReview(reviewID));
    }

    /**
     * Update a review by its unique ID (Customer and above only)
     * 
     * @param reviewID the ID of the review to update
     * @param loggedInUsername the username of the user that is logged in
     * @param request the request object containing the updated review information. It should contain the description and score of the review
     * 
     * @return the updated review
     */
    @PutMapping("/games/reviews/{reviewID}")
    public ReviewResponseDto updateReview(@PathVariable int reviewID,
            @RequestParam String loggedInUsername,
            @RequestBody ReviewRequestDto request) {
        // Check if the user has permission to update a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to create/update reviews.");
        }

        Review updatedReview = reviewService.updateReview(reviewID, request.getDescription(), request.getScore(), loggedInUsername);

        return new ReviewResponseDto(updatedReview);
    }

    /*
     * /games/{gameID}/reviews/{reviewID}/likes [POST, DELETE, GET]
     */

    /**
     * Get a boolean to see if a user has liked a review by its unique ID and the username of the user that is logged in
     * 
     * @param reviewID the ID of the review to get the number of likes for
     * 
     * @return the number of likes for the review
     */
    @GetMapping("/games/{gameID}/reviews/{reviewID}/likes")
    public Boolean hasLikedReview(@PathVariable int reviewID,
    @RequestParam String loggedInUsername) 
    {
        return reviewService.checkHasLiked(reviewID, loggedInUsername);
    }

    /**
     * Add a like to a review by its unique ID (Customer and above only)
     * 
     * @param reviewID the ID of the review to add a like to
     * @param loggedInUsername the username of the user that is logged in
     * 
     * @return the review with the like added
     */
    @PostMapping("/games/{gameID}/reviews/{reviewID}/likes")
    public ReviewResponseDto addLike(@PathVariable int reviewID,
            @RequestParam String loggedInUsername
        ) {
        // Check if the user has permission to like a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to like a review.");
        }

        reviewService.addLike(reviewID, loggedInUsername);

        return new ReviewResponseDto(reviewService.getReview(reviewID));
    }

    /**
     * Remove a like from a review by its unique ID (Customer and above only)
     * 
     * @param reviewID the ID of the review to remove a like from
     * @param loggedInUsername the username of the user that is logged in
     * 
     * @return the review with the like removed
     */
    @DeleteMapping("/games/{gameID}/reviews/{reviewID}/likes")
    public ReviewResponseDto removeLike(@PathVariable int reviewID,
            @RequestParam String loggedInUsername) {
        // Check if the user has permission to like a review
        if (!accountService.hasPermission(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to unlike a review.");
        }

        reviewService.removeLike(reviewID, loggedInUsername);  

        return new ReviewResponseDto(reviewService.getReview(reviewID));
    }


    /*
     * /games/{id}/reviews/{reviewID}/reply [GET, POST]
     */

    /**
     * Get a reply to a review by its unique ID 
     * 
     * @param reviewID the ID of the review to get the reply for
     * 
     * @return the reply to the review
     */
    @GetMapping("/games/reviews/{reviewID}/replies")
    public ReplyResponseDto getReplyToReview(@PathVariable int reviewID) {
        // Reply to a review by its unique ID and return the review as a response object

        Reply reply = reviewService.getReplyByReview(reviewID);

        // If no reply is found, return a NOT_FOUND status code
        if (reply == null) {
            return null;
        }

        return new ReplyResponseDto(reply);
    }

    /**
     * Reply to a review by its unique ID (Owner only)
     * 
     * @param reviewID the ID of the review to reply to
     * @param loggedInUsername the username of the user that is logged in
     * @param request the request object containing the reply information. It should contain the text of the reply
     * 
     * @return the reply to the review
     */
    @PostMapping("/games/reviews/{reviewID}/replies")
    public ReplyResponseDto replyToReview(@PathVariable int reviewID, @RequestParam String loggedInUsername,
            @RequestBody ReplyRequestDto request) {

        // Check if the user has permission to reply to reviews
        if (!accountService.hasPermission(loggedInUsername, 3)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to reply to reviews.");
        }

        // Reply to a review by its unique ID and return the review as a response
        // object, date gets automatically set to the current date when reply to review
        // is called
        Reply reply = reviewService.replyToReview(reviewID, loggedInUsername, request.getText());

        return new ReplyResponseDto(reply);
    }
}
