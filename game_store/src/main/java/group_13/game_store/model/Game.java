package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 73 "model.ump"
// line 206 "model.ump"
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum VisibilityStatus { Visible, Archived, PendingArchive, PendingVisible }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int gameID;
  private String title;
  private String description;
  private String img;
  private double price;
  private String parentalRating;
  private VisibilityStatus status;

  //Game Associations
  private List<GameCopy> copies;
  private GameCategory category;
  private List<Review> reviews;
  private Promotion promotion;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aGameID, String aTitle, String aDescription, String aImg, double aPrice, String aParentalRating, VisibilityStatus aStatus, GameCategory aCategory)
  {
    gameID = aGameID;
    title = aTitle;
    description = aDescription;
    img = aImg;
    price = aPrice;
    parentalRating = aParentalRating;
    status = aStatus;
    copies = new ArrayList<GameCopy>();
    if (!setCategory(aCategory))
    {
      throw new RuntimeException("Unable to create Game due to aCategory. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    reviews = new ArrayList<Review>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGameID(int aGameID)
  {
    boolean wasSet = false;
    gameID = aGameID;
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

  public boolean setImg(String aImg)
  {
    boolean wasSet = false;
    img = aImg;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(double aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setParentalRating(String aParentalRating)
  {
    boolean wasSet = false;
    parentalRating = aParentalRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(VisibilityStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public int getGameID()
  {
    return gameID;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public String getImg()
  {
    return img;
  }

  public double getPrice()
  {
    return price;
  }

  public String getParentalRating()
  {
    return parentalRating;
  }

  public VisibilityStatus getStatus()
  {
    return status;
  }
  /* Code from template association_GetMany */
  public GameCopy getCopy(int index)
  {
    GameCopy aCopy = copies.get(index);
    return aCopy;
  }

  public List<GameCopy> getCopies()
  {
    List<GameCopy> newCopies = Collections.unmodifiableList(copies);
    return newCopies;
  }

  public int numberOfCopies()
  {
    int number = copies.size();
    return number;
  }

  public boolean hasCopies()
  {
    boolean has = copies.size() > 0;
    return has;
  }

  public int indexOfCopy(GameCopy aCopy)
  {
    int index = copies.indexOf(aCopy);
    return index;
  }
  /* Code from template association_GetOne */
  public GameCategory getCategory()
  {
    return category;
  }
  /* Code from template association_GetMany */
  public Review getReview(int index)
  {
    Review aReview = reviews.get(index);
    return aReview;
  }

  public List<Review> getReviews()
  {
    List<Review> newReviews = Collections.unmodifiableList(reviews);
    return newReviews;
  }

  public int numberOfReviews()
  {
    int number = reviews.size();
    return number;
  }

  public boolean hasReviews()
  {
    boolean has = reviews.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = reviews.indexOf(aReview);
    return index;
  }
  /* Code from template association_GetOne */
  public Promotion getPromotion()
  {
    return promotion;
  }

  public boolean hasPromotion()
  {
    boolean has = promotion != null;
    return has;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCopies()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCopy(GameCopy aCopy)
  {
    boolean wasAdded = false;
    if (copies.contains(aCopy)) { return false; }
    copies.add(aCopy);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCopy(GameCopy aCopy)
  {
    boolean wasRemoved = false;
    if (copies.contains(aCopy))
    {
      copies.remove(aCopy);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCopyAt(GameCopy aCopy, int index)
  {  
    boolean wasAdded = false;
    if(addCopy(aCopy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCopies()) { index = numberOfCopies() - 1; }
      copies.remove(aCopy);
      copies.add(index, aCopy);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCopyAt(GameCopy aCopy, int index)
  {
    boolean wasAdded = false;
    if(copies.contains(aCopy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCopies()) { index = numberOfCopies() - 1; }
      copies.remove(aCopy);
      copies.add(index, aCopy);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCopyAt(aCopy, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCategory(GameCategory aNewCategory)
  {
    boolean wasSet = false;
    if (aNewCategory != null)
    {
      category = aNewCategory;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviews()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (reviews.contains(aReview)) { return false; }
    reviews.add(aReview);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReview(Review aReview)
  {
    boolean wasRemoved = false;
    if (reviews.contains(aReview))
    {
      reviews.remove(aReview);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewAt(Review aReview, int index)
  {  
    boolean wasAdded = false;
    if(addReview(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(reviews.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReviewAt(aReview, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPromotion(Promotion aNewPromotion)
  {
    boolean wasSet = false;
    promotion = aNewPromotion;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    copies.clear();
    category = null;
    reviews.clear();
    promotion = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameID" + ":" + getGameID()+ "," +
            "title" + ":" + getTitle()+ "," +
            "description" + ":" + getDescription()+ "," +
            "img" + ":" + getImg()+ "," +
            "price" + ":" + getPrice()+ "," +
            "parentalRating" + ":" + getParentalRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "category = "+(getCategory()!=null?Integer.toHexString(System.identityHashCode(getCategory())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "promotion = "+(getPromotion()!=null?Integer.toHexString(System.identityHashCode(getPromotion())):"null");
  }
}


