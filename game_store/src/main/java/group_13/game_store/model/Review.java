package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
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
  private int likes;
  private Date date;

  //Review Associations
  @ManyToOne
  private Customer reviewer;

  @ManyToOne
  private Game reviewedGame;

  @OneToOne(optional = true, mappedBy = "review")
  private Reply reply;

  //Link the review to the customers that liked it
  @OneToMany
  private List<Customer> likedByCustomers = new ArrayList<>();

  //------------------------
  // CONSTRUCTOR
  //------------------------

   // Hibernate needs a no-args constructor, but it can be protected
  protected Review() {
  }

  public Review(String aDescription, int aScore, Date aDate, Customer aReviewer, Game aReviewedGame, List<Customer> likedByCustomers)
  {
    description = aDescription;
    score = aScore;

    if (likedByCustomers != null && !likedByCustomers.isEmpty()) {
      likes = likedByCustomers.size();
      this.likedByCustomers = likedByCustomers;

    } else {
      likes = 0;
      this.likedByCustomers = new ArrayList<>();
    }
    
    date = aDate;
    if (!setReviewer(aReviewer))
    {
      throw new RuntimeException("Unable to create Review due to aReviewer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setReviewedGame(aReviewedGame))
    {
      throw new RuntimeException("Unable to create Review due to aReviewedGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  // Get the list of customers that liked the review
  public List<Customer> getLikedByCustomers() {
    return likedByCustomers;
  }

  // Lets you set the list of customers that liked the review
  public void setLikedByCustomers(List<Customer> likedByCustomers) {
    this.likedByCustomers = likedByCustomers;
    this.likes = likedByCustomers.size();
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
    return likes;
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



