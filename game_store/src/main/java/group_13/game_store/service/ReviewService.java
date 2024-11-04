package group_13.game_store.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.Review;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private GameRepository gameRepository;
    
    @Transactional
    public Review createReview(String aDescription, int aScore, int aLikes, Date aDate, String reviewerID, Game aReviewedGame){

        //Find the reviewer based on the reviewerId provided. If its not a customer it wont find it and because it retunrs null no review will be left
        Customer aReviewer = customerRepo.findByUsername(reviewerID);

        //If no reviewer were found return null and do nothing
        
        if(aReviewer == null) {
            return null;
        }

        //Get the list of games associated to the customer
        List<Game> games = gameRepository.findGamesByCustomer(aReviewer);
        
        //Go through the list of games associated to the customer and check if the reviewed game is there
        boolean customerHasGame = false;
        for(Game game : games) {
            if (game.getGameID() == aReviewedGame.getGameID()) {
                customerHasGame = true;
            }
        }

        //If the customer does not have the game you cannot write a review and return null
        if (!customerHasGame) {
            return null;
        }

        //Create the reviews with the inputed parameters 
        Review review = new Review(aDescription, aScore, aLikes, aDate, aReviewer, aReviewedGame);

        //Save the created review
        return reviewRepository.save(review);
    }
        

    //Function to add a like to a review
    @Transactional
    public int addLike(int reviewID){
        /*
         * Need to find a way to make sure customer hasnt already liked it
         */
        Review review = reviewRepository.findByReviewID(reviewID);

        review.setLikes(review.getLikes() + 1);

        return reviewRepository.save(review).getLikes();
    }

    //Function to remove a like from a review
    @Transactional
    public int removeLike(int reviewID){
        /*
         * Need to find a way to make sure customer has liked it before
         */
        Review review = reviewRepository.findByReviewID(reviewID);

        review.setLikes(review.getLikes() - 1);

        return reviewRepository.save(review).getLikes();
    }

    //Function to let the owner reply a review
    @Transactional
    public String replyToReview(int reviewerID, String replyerId) {
        /*
         * Implement logic to only let owner reply and to not let ownere reply more than once 
         */
        return "I disagree with your review";
    }

    //Function to let the owner reply a review
    @Transactional
    public List<Review> getUnansweredReviews(int gameID) {
        /*
         * Implement logic to find all the unsanwered reviews for a game
         */
        return null;
    }


    //Function to get the average rating of a game based on reviews it will return -1 if it failed
    @Transactional
    public int getGameRating(int gameID) {
        //Get the list of scores associated to our game via our reviewRepo
        List<Integer> scores = reviewRepository.findScoresByGameId(gameID);

        //If scores could not be found return -1 to indicate an error
        if(scores == null) {
            return -1;
        }

        //Calculate the total sum of scores by iterating through the scores list
        Integer sum = 0;
        for(Integer score : scores){
            sum += score;
        } 

        //Return the average score
        return sum/scores.size();
    }

}
