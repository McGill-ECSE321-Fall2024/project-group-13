package group_13.game_store.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/games/gameID/reviews/{reviewID}/reply")
    public ReplyResponseDto getReplyToReview(@PathVariable int reviewID){
        //Reply to a review by its unique ID and return the review as a response object

        Reply reply = reviewService.getReplyByReview(reviewID);
        
        return  new ReplyResponseDto(reply);
    }

    @PostMapping("/games/{gameID}/reviews/{reviewID}/reply?loggedInUser={loggedInUsername}")
    public ReplyResponseDto replyToReview(@PathVariable int reviewID, @RequestParam String loggedInUsername, @RequestBody ReplyRequestDto request){
        //Reply to a review by its unique ID and return the review as a response object, date gets automatically set to the current date when reply to review is called
        //Reply to review makes sure that the user is the owner of the game before allowing them to reply
        Reply reply = reviewService.replyToReview(reviewID, loggedInUsername, request.getText());

        return new ReplyResponseDto(reply);
    }

    @GetMapping("/games/promotions")
    public PromotionListResponseDto getPromotions(){
        //Return a list of all promotions via the PromotionListResponseDto
        return new PromotionListResponseDto(gameStoreManagementService.getAllPromotions());
    }

    @PostMapping("/games/promotions?loggedInUser={loggedInUsername}")
    public PromotionResponseDto createPromotion(@RequestParam String loggedInUsername, @RequestBody PromotionRequestDto request){
        //Create a promotion with the information from the request
        //It already checks for permission based on the logged in user's username
        gameStoreManagementService.addPromotion(loggedInUsername, 
            request.getPercentage(),
            request.getStartDate(),
            request.getEndDate(),
            request.getTitle(),
            request.getDescription()
        );

        //Return the promotion as a response object
        return new PromotionResponseDto(request.getPercentage(), request.getTitle(),request.getDescription(), request.getStartDate(), request.getEndDate());
    }

    // @GetMapping("/games/{gameID}/promotions")
    // Nothing in the service layer to currently do this

    @PostMapping("/games/{gameID}/promotions?loggedInUser={loggedInUsername}")
    public PromotionResponseDto addPromotionToGame(@PathVariable int gameID,@RequestParam String loggedInUsername, @RequestBody PromotionRequestDto request){
        //Add a promotion to a game by its unique ID
        gameStoreManagementService.addPromotion(loggedInUsername, 
            request.getPercentage(),
            request.getStartDate(),
            request.getEndDate(),
            request.getTitle(),
            request.getDescription()
        );

        //Return the promotion as a response object
        return new PromotionResponseDto(request.getPercentage(), request.getTitle(),request.getDescription(), request.getStartDate(), request.getEndDate());
    }

    //@GetMapping("/games/{gameID}/promotions/{promotionID}")
    //Not implemented in the service layer yet

    // Not implemented in the service layer yet
    // @PutMapping("/games/promotions/{promotionID}?loggedInUser={loggedInUsername}")
    // public PromotionResponseDto updatePromotion(@PathVariable int promotionID, @RequestParam String loggedInUsername, @RequestBody PromotionRequestDto request){
    //     //Update a promotion by its unique ID
    //    //Add a promotion to a game by its unique ID

    //    gameStoreManagementService.addPromotion(loggedInUsername, 
    //         request.getPercentage(),
    //         request.getStartDate(),
    //         request.getEndDate(),
    //         request.getTitle(),
    //         request.getDescription()
    //     );

    //     //Return the promotion as a response object
    //     return new PromotionResponseDto(request.getPercentage(), request.getTitle(),request.getDescription(), request.getStartDate(), request.getEndDate());
    // }


    // Not implemented in the service layer yet
    // @DeleteMapping("/games/promotions/{promotionID}?loggedInUser={loggedInUsername}")
    // public void deletePromotion(@PathVariable int promotionID, @RequestParam String loggedInUsername){
    //     //Delete a promotion by its unique ID
    //     gameStoreManagementService.deletePromotion(loggedInUsername, promotionID);
    // }
    

}
