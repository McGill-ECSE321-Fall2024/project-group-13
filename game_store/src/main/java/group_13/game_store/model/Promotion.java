package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// line 135 "model.ump"
// line 226 "model.ump"
@Entity
public class Promotion
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Promotion Attributes
  @Id
  @GeneratedValue
  private int promotionID;
  private int percentage;
  private Date startDate;
  private Date endDate;
  private String title;
  private String description;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // Hibernate needs a no-args constructor, but it can be protected
  protected Promotion()
  {
  }

  public Promotion(int aPromotionID, int aPercentage, Date aStartDate, Date aEndDate, String aTitle, String aDescription)
  {
    promotionID = aPromotionID;
    percentage = aPercentage;
    startDate = aStartDate;
    endDate = aEndDate;
    title = aTitle;
    description = aDescription;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPromotionID(int aPromotionID)
  {
    boolean wasSet = false;
    promotionID = aPromotionID;
    wasSet = true;
    return wasSet;
  }

  public boolean setPercentage(int aPercentage)
  {
    boolean wasSet = false;
    percentage = aPercentage;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
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

  public int getPromotionID()
  {
    return promotionID;
  }

  public int getPercentage()
  {
    return percentage;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "promotionID" + ":" + getPromotionID()+ "," +
            "percentage" + ":" + getPercentage()+ "," +
            "title" + ":" + getTitle()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this)  ? getEndDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}



