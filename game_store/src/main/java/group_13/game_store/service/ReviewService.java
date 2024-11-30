package group_13.game_store.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.Owner;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.model.ReviewLike;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.ReplyRepository;
import group_13.game_store.repository.ReviewLikeRepository;
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

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    // Retrieve all reviews
    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    // Retrieve all reviews for a specific game
    public List<Review> getAllReviewsForGame(int gameID) {
        return (List<Review>) reviewRepository.findByReviewedGame_GameID(gameID);
    }

    // Method retrieve a specific review
    public Review getReview(int reviewID) {
        return reviewRepository.findByReviewID(reviewID);
    }

    // Method to retrieve the reply associated to a specific review
    public Reply getReplyByReview(int reviewID) {
        return replyRepo.findByReview_ReviewID(reviewID);
    }

    // Method to create a new review based on the inputed parameters, it will return
    // false if it failed to create the review and true if it succeeded
    @Transactional
    public Review createReview(String aDescription, int aScore, String reviewerID,
            int gameID) {
        // Find the reviewer based on the reviewerId provided. If its not a customer it
        // wont find it and because it retunrs null no review will be left
        Customer aReviewer = customerRepo.findByUsername(reviewerID);

        // If no reviewer were found throw an exception
        if (aReviewer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (aScore < 1 || aScore > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Score must be between 1 and 5.");
        }

        // Get the list of games associated to the customer
        List<Game> games = gameRepository.findGamesByCustomer(aReviewer);

        Game aReviewedGame = gameRepository.findByGameID(gameID);

        // If no game were found return null and do nothing
        if (aReviewedGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewed game was not found.");
        }

        // Go through the list of games associated to the customer and check if the
        // reviewed game is there
        boolean customerHasGame = false;
        for (Game game : games) {
            if (game.getGameID() == aReviewedGame.getGameID()) {
                customerHasGame = true;
            }
        }

        // If the customer does not have the game, throw an exception
        if (!customerHasGame) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer does not have the game.");
        }

        // Create the reviews with the inputed parameters as well as the current date
        Review review = new Review(aDescription, aScore, Date.valueOf(LocalDate.now()), aReviewer, aReviewedGame);

        // Save the created review;
        return reviewRepository.save(review);

    }

    // Method to check if a customer owns a game or not
    // false if it the ucstomer does not own the game and true if it does
    @Transactional
    public Boolean checkOwnership(String reviewerID, int gameID) {
        // Find the reviewer based on the reviewerId provided. If its not a customer it
        // wont find it and because it retunrs null no review will be left
        Customer aReviewer = customerRepo.findByUsername(reviewerID);

        // If no reviewer were found throw an exception
        if (aReviewer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        // Get the list of games associated to the customer
        List<Game> games = gameRepository.findGamesByCustomer(aReviewer);

        Game aReviewedGame = gameRepository.findByGameID(gameID);

        // If no game were found return null and do nothing
        if (aReviewedGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewed game was not found.");
        }

        // Go through the list of games associated to the customer and check if the
        // reviewed game is there
        boolean customerHasGame = false;
        for (Game game : games) {
            if (game.getGameID() == aReviewedGame.getGameID()) {
                customerHasGame = true;
            }
        }

        if(customerHasGame == false){
            return customerHasGame;
        }

        boolean customerNotReviewed = false;
        List<Review> reviews = reviewRepository.findByReviewedGame_GameID(gameID);
        for (Review review : reviews) {
            if (review.getReviewer().getUsername().equals(reviewerID)) {
                customerNotReviewed = true;
            }
        }

        // If the customer does not already have a review, return true as we can leave a review
        return !customerNotReviewed;
    }

    // Update an existing review
    @Transactional
    public Review updateReview(int reviewID, String aDescription, int aScore, String reviewerID) {
        // Validate fields
        if (aDescription == null || aDescription.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be null or empty.");
        }
        if (aScore == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Score cannot be zero.");
        }
        if (reviewID <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review ID must be greater than 0.");
        }
        if (aScore < 1 || aScore > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Score must be between 1 and 5.");
        }

        Review review = reviewRepository.findByReviewID(reviewID);
        if (review != null) {
            review.setDescription(aDescription);
            review.setScore(aScore);

            // Update the date to being today
            review.setDate(Date.valueOf(LocalDate.now()));
            reviewRepository.save(review);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        return review;
    }

    // Method to add a like to a review based on the reviewID and the customerID we
    // return false if it failed to add the like and true if it succeeded
    @Transactional
    public int addLike(int reviewID, String customerUsername) {
        /// Retrieve the customer mad review
        Customer customer = customerRepo.findByUsername(customerUsername);
        Review review = reviewRepository.findByReviewID(reviewID);

        // If either the customer or the review is not found we throw an exception
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (review == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        // If the customer has already liked the review you cannot like it again
        if (reviewLikeRepository.existsByReviewAndCustomer(review, customer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer has already liked the review.");
        }

        // Create a new ReviewLike entity
        ReviewLike reviewLike = new ReviewLike(review, customer);
        reviewLikeRepository.save(reviewLike);

        review.addReviewLike(reviewLike);

        return reviewRepository.save(review).getLikes();
    }

    // Method to remove a like from a review based on the reviewID and the
    // customerID we return false if it failed to remove the like and true if it
    // succeeded
    @Transactional
    public int removeLike(int reviewID, String customerUsername) {
        /// Retrieve the customer mad review
        Customer customer = customerRepo.findByUsername(customerUsername);
        Review review = reviewRepository.findByReviewID(reviewID);

        // If either the customer or the review is not found we throw an exception
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (review == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        // If the customer has already liked the review you cannot like it again
        if (!reviewLikeRepository.existsByReviewAndCustomer(review, customer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer has not liked the review.");
        }

        ReviewLike reviewLike = reviewLikeRepository.findByReviewAndCustomer(review, customer);

        review.removeReviewLike(reviewLike);

        reviewLikeRepository.delete(reviewLike);

        return reviewRepository.save(review).getLikes();
    }

    // Method to let the owner reply to a review if there are no current replies to
    // it
    @Transactional
    public Reply replyToReview(int reviewID, String replyerId, String reply) {
        

        // Look for the user based on the replyerId
        Owner replyerOwner = ownerRepo.findByUsername(replyerId);
        Customer replyerCustomer = customerRepo.findByUsername(replyerId);
        Employee replyerEmployee = employeeRepo.findByUsername(replyerId);

        // If no owner were found but another type of account was then the user given is
        // not a owner and cannot reply to reviews
        if (replyerOwner == null && (replyerCustomer != null || replyerEmployee != null)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an owner.");

            // If no owner were found and no other type of account was found then the user
            // does not exist
        } else if (replyerOwner == null && replyerCustomer == null && replyerEmployee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

        // Find the review based on the reviewID
        Review review = reviewRepository.findByReviewID(reviewID);

        // If the review is not found throw an exception
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found.");
        }

        // If the review already has a reply then we cannot reply to it
        if (review.hasReply()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Review already has a reply.");
        }

        if (reply == null || reply.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reply cannot be null or empty.");
        }

        // Create a reply with the inputed parameters and the current date
        Date today = Date.valueOf(LocalDate.now());
        Reply replyToReview = new Reply(reply, today);

        // Set the association on the owning side
        replyToReview.setReview(review);

        // Set the reply to the review and save it
        replyRepo.save(replyToReview);

        review.setReply(replyToReview);
        
        return reviewRepository.save(review).getReply();
    }

    // Method to show all unanswered reviews
    @Transactional
    public List<Review> getUnansweredReviews() {
        // Get the list of reviews that have no reply
        List<Review> reviews = reviewRepository.findByReplyIsNull();
        return reviews;
    }

    // Method to get the average rating of a game based on reviews it will return -1
    // if it failed
    @Transactional
    public int getGameRating(int gameID) {
        // Get the list of scores associated to our game via our reviewRepo
        List<Review> reviews = reviewRepository.findByReviewedGame_GameID(gameID);

        // If scores could not be found return -1 to indicate an error
        if (reviews == null) {
            return 0;
        }

        // Calculate the total sum of scores by iterating through the scores list
        Integer sum = 0;
        for (Review review : reviews) {
            sum += review.getScore();
        }

        if(reviews.size() == 0){
            return 0;
        }

        // Return the average score
        return sum / reviews.size();
    }
}
