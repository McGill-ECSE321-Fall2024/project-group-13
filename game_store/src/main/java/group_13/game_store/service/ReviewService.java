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
        try {
            //Find the reviewer based on the reviewerId provided. If its not a customer it wont find it and because it retunrs null no review will be left
            Customer aReviewer = customerRepo.findByUsername(reviewerID);
    
            //If no reviewer were found return null and do nothing
            if(aReviewer == null) {
                System.out.println("No customer found with the provided ID for the create Review function");
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
                System.out.println("Customer does not have the game so it can't review it");
                return null;
            }
    
            //Create the reviews with the inputed parameters 
            Review review = new Review(aDescription, aScore, aLikes, aDate, aReviewer, aReviewedGame);
    
            //Save the created review
            return reviewRepository.save(review);
        
        } catch (Exception e) {
            //If an error occurs return null and print the error
            System.out.println("Error in createReview: " + e);
            return null;
        }
    }
        

    //Function to add a like to a review based on the reviewID and the customerID we return -1 if it failed to add the like and the new amount of likes if it succeeded
    @Transactional
    public int addLike(int reviewID, String customerUsername){
        try {
            //Unfortunately no real way to know if this works or not unless we test the repo itself
            List<Review> likedReviews = reviewRepository.findReviewsLikedByCustomer(customerUsername);
            
            //Check if the customer has already liked the review
            boolean customerHasLiked = false;
            for(Review review : likedReviews) {
                if (review.getReviewID() == reviewID) {
                    customerHasLiked = true;
                }
            }

            //If the customer has already liked the review we cannot like it again and return -1 to indicate an error
            if(customerHasLiked) {
                System.out.println("Customer has already liked the review so it can't like it again");
                return -1;
            }

            //Find the review based on the reviewID and add a like to it
            Review review = reviewRepository.findByReviewID(reviewID);
            
            //Add the customer to the list of customers that liked the review
            List<Customer> customerThatLiked = review.getLikedByCustomers();
            Customer customer = customerRepo.findByUsername(customerUsername);
            customerThatLiked.add(customer);
            review.setLikedByCustomers(customerThatLiked);
            
            //Save the review with the new like added to it
            review.setLikes(review.getLikes() + 1);
            return reviewRepository.save(review).getLikes();

        } catch (Exception e) {
            //If an error occurs return -1 and print the error
            System.out.println("Error in addLike: " + e);
            return -1;
        }
    }

    //Function to remove a like from a review based on the reviewID and the customerID we return -1 if it failed to remove the like and the new amount of likes if it succeeded
    @Transactional
    public int removeLike(int reviewID, String customerUsername){
        try {
            //Unfortunately no real way to know if this works or not unless we test the repo itself
            List<Review> likedReviews = reviewRepository.findReviewsLikedByCustomer(customerUsername);
            
            //Check if the customer has already liked the review
            boolean customerHasLiked = false;
            for(Review review : likedReviews) {
                if (review.getReviewID() == reviewID) {
                    customerHasLiked = true;
                }
            }
    
            //If the customer has not liked the review we cannot remove a like and return -1 to indicate an error
            if(!customerHasLiked) {
                System.out.println("Customer has not liked the review so it can't unlike it");
                return -1;
            }
            
            //Find the review based on the reviewID
            Review review = reviewRepository.findByReviewID(reviewID);
            
            //Remove the customer from the list of customers that liked the review
            List<Customer> customerThatLiked = review.getLikedByCustomers();
            Customer customer = customerRepo.findByUsername(customerUsername);
            customerThatLiked.remove(customer);
            review.setLikedByCustomers(customerThatLiked);

            //Remove the like from the review
            review.setLikes(review.getLikes() - 1);
            return reviewRepository.save(review).getLikes();

        } catch (Exception e) {
            //If an error occurs return -1 and print the error
            System.out.println("Error in removeLike: " + e);
            return -1;
        }
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
        try {
            //Get the list of scores associated to our game via our reviewRepo
            List<Review> reviews = reviewRepository.findByReviewedGame_GameID(gameID);
    
            //If scores could not be found return -1 to indicate an error
            if(reviews == null) {
                return -1;
            }
    
            //Calculate the total sum of scores by iterating through the scores list
            Integer sum = 0;
            for(Review review : reviews){
                sum += review.getScore();
            } 
    
            //Return the average score
            return sum/reviews.size();

        } catch (Exception e) {
            //If an error occurs return -1 and print the error
            System.out.println("Error in getGameRating: " + e);
            return -1;
        }
    }

}
