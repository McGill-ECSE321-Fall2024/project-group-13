package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

// line 99 "model.ump"
// line 210 "model.ump"
@Entity
public class Review
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  @Id
  @GeneratedValue
  private int reviewID;
  private String description;
  private int score;
  private Date date;

  //Review Associations
  @ManyToOne
  private Customer reviewer;

  @ManyToOne
  private Game reviewedGame;

  @OneToOne(optional = true, mappedBy = "review")
  private Reply reply;

  //Link the review to the customers that liked it
  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<ReviewLike> reviewLikes = new ArrayList<>();

  //------------------------
  // CONSTRUCTOR
  //------------------------

   // Hibernate needs a no-args constructor, but it can be protected
  protected Review() {
  }

  public Review(String aDescription, int aScore, Date aDate, Customer aReviewer, Game aReviewedGame)
  {
    this.description = aDescription;
    this.score = aScore;
    this.date = aDate;
    this.reviewer = aReviewer;
    this.reviewedGame = aReviewedGame;
    this.reviewLikes = new ArrayList<>(); // Initialize the reviewLikes list
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReviewID(int aReviewID)
  {
    boolean wasSet = false;
    reviewID = aReviewID;
    wasSet = true;
    return wasSet;
  }

  //Get the list of likes for this review
  public List<ReviewLike> getReviewLikes() {
    return reviewLikes;
  }
  
  //Set the list of likes for this review
  public void setReviewLikes(List<ReviewLike> reviewLikes) {
    this.reviewLikes = reviewLikes;
  }

  public void addReviewLike(ReviewLike reviewLike) {
    reviewLikes.add(reviewLike);
  }
  
  public void removeReviewLike(ReviewLike reviewLike) {
    reviewLikes.remove(reviewLike);
  }
  
  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getReviewID()
  {
    return reviewID;
  }

  public String getDescription()
  {
    return description;
  }

  public int getScore()
  {
    return score;
  }

  public int getLikes()
  {
    return reviewLikes.size();
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public Customer getReviewer()
  {
    return reviewer;
  }
  /* Code from template association_GetOne */
  public Game getReviewedGame()
  {
    return reviewedGame;
  }
  /* Code from template association_GetOne */
  public Reply getReply()
  {
    return reply;
  }

  public boolean hasReply()
  {
    boolean has = reply != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setReviewer(Customer aNewReviewer)
  {
    boolean wasSet = false;
    if (aNewReviewer != null)
    {
      reviewer = aNewReviewer;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setReviewedGame(Game aNewReviewedGame)
  {
    boolean wasSet = false;
    if (aNewReviewedGame != null)
    {
      reviewedGame = aNewReviewedGame;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setReply(Reply aNewReply)
  {
    boolean wasSet = false;
    reply = aNewReply;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    reviewer = null;
    reviewedGame = null;
    reply = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "reviewID" + ":" + getReviewID()+ "," +
            "description" + ":" + getDescription()+ "," +
            "score" + ":" + getScore()+ "," +
            "likes" + ":" + getLikes()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewer = "+(getReviewer()!=null?Integer.toHexString(System.identityHashCode(getReviewer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewedGame = "+(getReviewedGame()!=null?Integer.toHexString(System.identityHashCode(getReviewedGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reply = "+(getReply()!=null?Integer.toHexString(System.identityHashCode(getReply())):"null");
  }
}



