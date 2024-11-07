package group_13.game_store.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.dto.ReviewListResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
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

    @GetMapping("/games/reviews")
    public ReviewListResponseDto getReviews(){
        //Return a list of all reviews via the ReviewListResponseDto
        return new ReviewListResponseDto(reviewService.getAllReviews());
    }

    @GetMapping("/games/{gameID}/reviews")
    public ReviewListResponseDto getReviewsByGame(@PathVariable int gameID){
        //Return a list of reviews associated with a game via the ReviewListResponseDto
        return new ReviewListResponseDto(reviewService.getAllReviewsForGame(gameID));
    }

    @PostMapping("/games/{gameID}/reviews?loggedInUser={loggedInUsername}")
    public ReviewResponseDto createReview(@PathVariable int gameID,
        @RequestParam String loggedInUsername,
        @RequestBody ReviewRequestDto request
    ) {

        //Create a base review with the information from the request. It takes in the logged in user's username as well as the current date on top of the request information
        Review review = reviewService.createReview(request.getDescription(), 
                request.getScore(), 
                request.getLikes(), 
                Date.valueOf(LocalDate.now()), 
                
                //CreateReview will not let you create a review if the user is not logged in as a customer that has bought the game
                loggedInUsername, 
                gameID);
        
        //Return the review as a response object
        return new ReviewResponseDto(review);
    }

    @GetMapping("/games/{gameID}/reviews/{reviewID}")
    public ReviewResponseDto getReview(@PathVariable int reviewID){
        //Return a review by its unique ID via the ReviewResponseDto
        return new ReviewResponseDto(reviewService.getReview(reviewID));
    }

}
