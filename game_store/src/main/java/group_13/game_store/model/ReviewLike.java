package group_13.game_store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReviewLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    // Constructors
    protected ReviewLike() {
    }

    public ReviewLike(Review review, Customer customer) {
        this.review = review;
        this.customer = customer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public Customer getCustomer() {
        return customer;
    }
}
