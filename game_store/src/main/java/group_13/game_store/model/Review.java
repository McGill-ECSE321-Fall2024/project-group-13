package group_13.game_store.model;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

// line 104 "model.ump"
// line 230 "model.ump"
public class Review
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  private int reviewID;
  private String description;
  private int score;
  private int likes;
  private Date date;

  //Review Associations
  private Reply reply;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(int aReviewID, String aDescription, int aScore, int aLikes, Date aDate)
  {
    reviewID = aReviewID;
    description = aDescription;
    score = aScore;
    likes = aLikes;
    date = aDate;
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

  public boolean setLikes(int aLikes)
  {
    boolean wasSet = false;
    likes = aLikes;
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
  public Reply getReply()
  {
    return reply;
  }

  public boolean hasReply()
  {
    boolean has = reply != null;
    return has;
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
            "  " + "reply = "+(getReply()!=null?Integer.toHexString(System.identityHashCode(getReply())):"null");
  }
}


