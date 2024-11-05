package group_13.game_store.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.Owner;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.ReplyRepository;
import group_13.game_store.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReplyRepository replyRepo;

    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private GameRepository gameRepository;
    
    @Transactional
    public boolean createReview(String aDescription, int aScore, int aLikes, Date aDate, String reviewerID, Game aReviewedGame){
        try {
            //Find the reviewer based on the reviewerId provided. If its not a customer it wont find it and because it retunrs null no review will be left
            Customer aReviewer = customerRepo.findByUsername(reviewerID);
    
            //If no reviewer were found return null and do nothing
            if(aReviewer == null) {
                System.out.println("No customer found with the provided ID for the create Review function");
                return false;
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
                return false;
            }
    
            //Create the reviews with the inputed parameters 
            Review review = new Review(aDescription, aScore, aLikes, aDate, aReviewer, aReviewedGame);
    
            //Save the created review
            reviewRepository.save(review);
            return true;
        
        } catch (Exception e) {
            //If an error occurs return null and print the error
            System.out.println("Error in createReview: " + e);
            return false;
        }
    }
        

    //Function to add a like to a review based on the reviewID and the customerID we return -1 if it failed to add the like and the new amount of likes if it succeeded
    @Transactional
    public boolean addLike(int reviewID, String customerUsername){
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
                return false;
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
            reviewRepository.save(review);
            return true;

        } catch (Exception e) {
            //If an error occurs return -1 and print the error
            System.out.println("Error in addLike: " + e);
            return false;
        }
    }

    //Function to remove a like from a review based on the reviewID and the customerID we return -1 if it failed to remove the like and the new amount of likes if it succeeded
    @Transactional
    public boolean removeLike(int reviewID, String customerUsername){
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
                return false;
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
            reviewRepository.save(review);
            return true;

        } catch (Exception e) {
            //If an error occurs return -1 and print the error
            System.out.println("Error in removeLike: " + e);
            return false;
        }
    }

    //Function to let the owner reply a review
    @Transactional
    public boolean replyToReview(int reviewID, String replyerId, String reply) {
        try {
            //Look for the user based on the replyerId
            Owner replyerOwner = ownerRepo.findByUsername(replyerId);
            Customer replyerCustomer = customerRepo.findByUsername(replyerId);
            Employee replyerEmployee = employeeRepo.findByUsername(replyerId);
    
            //If no owner were found but another type of account was then the user given is not a owner and cannot reply to reviews
            if(replyerOwner == null && (replyerCustomer != null || replyerEmployee != null)) {
                System.out.println("User does not have permission to reply to reviews");
                return false;
    
            //If no owner were found and no other type of account was found then the user does not exist
            } else if (replyerOwner == null && replyerCustomer == null && replyerEmployee == null) {
                    System.out.println("User not found");
                    return false;
            }
    
            //Create a reply with the inputed parameters and the current date
            Date today = Date.valueOf(LocalDate.now());
            Reply replyToReview = new Reply(reply, today);
            replyRepo.save(replyToReview);
    
            //Find the review based on the reviewID
            Review review = reviewRepository.findByReviewID(reviewID);
    
            //If the review is not found return an error message
            if (review == null) {
                System.out.println("Review not found");
                return false;
            }
    
            //Set the reply to the review and save it
            review.setReply(replyToReview);
            reviewRepository.save(review);
            return true;
        } catch (Exception e) {
            //If an error occurs return false and print the error
            System.out.println("Error in replyToReview: " + e);
            return false;
        }

    }

    //Function to let the owner reply a review
    @Transactional
    public List<Review> getUnansweredReviews() {
        try{
            //Get the list of reviews that have no reply
            List<Review> reviews = reviewRepository.findByReplyIsNull();
            return reviews;

        } catch (Exception e) {
            //If an error occurs return null and print the error
            System.out.println("Error in getUnansweredReviews: " + e);
            return null;
        }
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
