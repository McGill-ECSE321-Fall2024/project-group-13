package group_13.game_store.service;

import java.sql.Date;
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
    private AccountService accountService;

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
        try {
            // Find the reviewer based on the reviewerId provided. If its not a customer it
            // wont find it and because it retunrs null no review will be left
            Customer aReviewer = customerRepo.findByUsername(reviewerID);

            // If no reviewer were found return null and do nothing
            if (aReviewer == null) {
                System.out.println("No customer found with the provided ID for the create Review function");
                return null;
            }

            // Get the list of games associated to the customer
            List<Game> games = gameRepository.findGamesByCustomer(aReviewer);

            Game aReviewedGame = gameRepository.findByGameID(gameID);

            // If no game were found return null and do nothing
            if (aReviewedGame == null) {
                System.out.println("No game found with the provided ID for the create Review function");
                return null;
            }

            // Go through the list of games associated to the customer and check if the
            // reviewed game is there
            boolean customerHasGame = false;
            for (Game game : games) {
                if (game.getGameID() == aReviewedGame.getGameID()) {
                    customerHasGame = true;
                }
            }

            // If the customer does not have the game you cannot write a review and return
            // null
            if (!customerHasGame) {
                System.out.println("Customer does not have the game so it can't review it");
                return null;
            }

            // Create the reviews with the inputed parameters as well as the current date
            Review review = new Review(aDescription, aScore, Date.valueOf(LocalDate.now()), aReviewer, aReviewedGame);

            // Save the created review;
            return reviewRepository.save(review);

        } catch (Exception e) {
            // If an error occurs return null and print the error
            System.out.println("Error in createReview: " + e);
            return null;
        }
    }

    // Update an existing review
    @Transactional
    public Review updateReview(int reviewID, String aDescription, int aScore, String reviewerID) {
        // Check if the user has permission to update a game
        if (!accountService.hasPermission(reviewerID, 1)) {
            throw new IllegalArgumentException("User does not have permission to update a game.");
        }

        // Validate fields
        if (aDescription == null || aDescription.isEmpty()) {
            throw new IllegalArgumentException("Descirption cannot be null or empty.");
        }
        if (aScore == 0) {
            throw new IllegalArgumentException("Score cannot be null or empty.");
        }
        if (reviewID <= 0 || aScore <= 0) {
            throw new IllegalArgumentException("Review ID must be greater than zero.");
        }

        Review review = reviewRepository.findByReviewID(reviewID);
        if (review != null) {
            review.setDescription(aDescription);
            review.setScore(aScore);

            // Update the date to being today
            review.setDate(Date.valueOf(LocalDate.now()));
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Review with ID " + reviewID + " not found.");
        }

        return review;
    }

    // Method to add a like to a review based on the reviewID and the customerID we
    // return false if it failed to add the like and true if it succeeded
    @Transactional
    public boolean addLike(int reviewID, String customerUsername) {
        /// Retrieve the customer mad review
        Customer customer = customerRepo.findByUsername(customerUsername);
        Review review = reviewRepository.findByReviewID(reviewID);

        //If either the customer or the review is not found we throw an exception
        if (customer == null || review == null) {
            throw new IllegalArgumentException("Customer or review not found.");
        }

        //If the customer has already liked the review you cannot like it again
        if (reviewLikeRepository.existsByReviewAndCustomer(review, customer)) {
            throw new IllegalArgumentException("Customer has already liked the review.");
        }

        // Create a new ReviewLike entity
        ReviewLike reviewLike = new ReviewLike(review, customer);
        reviewLikeRepository.save(reviewLike);

        review.addReviewLike(reviewLike);
        reviewRepository.save(review);

        return true;
    }

    // Method to remove a like from a review based on the reviewID and the
    // customerID we return false if it failed to remove the like and true if it
    // succeeded
    @Transactional
    public boolean removeLike(int reviewID, String customerUsername) {
        /// Retrieve the customer mad review
        Customer customer = customerRepo.findByUsername(customerUsername);
        Review review = reviewRepository.findByReviewID(reviewID);

        //If either the customer or the review is not found we throw an exception
        if (customer == null || review == null) {
            throw new IllegalArgumentException("Customer or review not found.");
        }

        //If the customer has already liked the review you cannot like it again
        if (!reviewLikeRepository.existsByReviewAndCustomer(review, customer)) {
            throw new IllegalArgumentException("Customer hasnt yet liked the review.");
        }
        ReviewLike reviewLike = reviewLikeRepository.findByReviewAndCustomer(review, customer);

        review.removeReviewLike(reviewLike);
        reviewRepository.save(review);

        reviewLikeRepository.delete(reviewLike);;

        return true;
    }

    // Method to let the owner reply to a review if there are no current replies to
    // it
    @Transactional
    public Reply replyToReview(int reviewID, String replyerId, String reply) {
        try {
            // Look for the user based on the replyerId
            Owner replyerOwner = ownerRepo.findByUsername(replyerId);
            Customer replyerCustomer = customerRepo.findByUsername(replyerId);
            Employee replyerEmployee = employeeRepo.findByUsername(replyerId);

            // If no owner were found but another type of account was then the user given is
            // not a owner and cannot reply to reviews
            if (replyerOwner == null && (replyerCustomer != null || replyerEmployee != null)) {
                System.out.println("User does not have permission to reply to reviews");
                return null;

                // If no owner were found and no other type of account was found then the user
                // does not exist
            } else if (replyerOwner == null && replyerCustomer == null && replyerEmployee == null) {
                System.out.println("User not found");
                return null;
            }

            // Create a reply with the inputed parameters and the current date
            Date today = Date.valueOf(LocalDate.now());
            Reply replyToReview = new Reply(reply, today);
            replyRepo.save(replyToReview);

            // Find the review based on the reviewID
            Review review = reviewRepository.findByReviewID(reviewID);

            // If the review is not found return an error message
            if (review == null) {
                System.out.println("Review not found");
                return null;
            }

            // If the review already has a reply return an error message
            if (review.hasReply()) {
                System.out.println("Review already has a reply");
                return null;
            }

            // Set the reply to the review and save it
            review.setReply(replyToReview);
            return reviewRepository.save(review).getReply();
        } catch (Exception e) {
            // If an error occurs return false and print the error
            System.out.println("Error in replyToReview: " + e);
            return null;
        }

    }

    // Method to show all unanswered reviews
    @Transactional
    public List<Review> getUnansweredReviews() {
        try {
            // Get the list of reviews that have no reply
            List<Review> reviews = reviewRepository.findByReplyIsNull();
            return reviews;

        } catch (Exception e) {
            // If an error occurs return null and print the error
            System.out.println("Error in getUnansweredReviews: " + e);
            return null;
        }
    }

    // Method to get the average rating of a game based on reviews it will return -1
    // if it failed
    @Transactional
    public int getGameRating(int gameID) {
        try {
            // Get the list of scores associated to our game via our reviewRepo
            List<Review> reviews = reviewRepository.findByReviewedGame_GameID(gameID);

            // If scores could not be found return -1 to indicate an error
            if (reviews == null) {
                return -1;
            }

            // Calculate the total sum of scores by iterating through the scores list
            Integer sum = 0;
            for (Review review : reviews) {
                sum += review.getScore();
            }

            // Return the average score
            return sum / reviews.size();

        } catch (Exception e) {
            // If an error occurs return -1 and print the error
            System.out.println("Error in getGameRating: " + e);
            return -1;
        }
    }
}
