package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
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
  @Id
  @GeneratedValue
  private int gameID;
  private String title;
  private String description;
  private String img;
  private int stock;
  private double price;
  private String parentalRating;
  private VisibilityStatus status;

  //Game Associations
  @ManyToOne
  private GameCategory category;
  @ManyToOne
  private Promotion promotion;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aTitle, String aDescription, String aImg, int aStock, double aPrice, String aParentalRating, VisibilityStatus aStatus, GameCategory aCategory)
  {
    title = aTitle;
    description = aDescription;
    img = aImg;
    stock = aStock;
    price = aPrice;
    parentalRating = aParentalRating;
    status = aStatus;
    if (!setCategory(aCategory))
    {
      throw new RuntimeException("Unable to create Game due to aCategory. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // Constructor for Hibernate
  protected Game() {
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

  public boolean setStock(int aStock)
  {
    boolean wasSet = false;
    stock = aStock;
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

  public int getStock()
  {
    return stock;
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
  /* Code from template association_GetOne */
  public GameCategory getCategory()
  {
    return category;
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
    category = null;
    promotion = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameID" + ":" + getGameID()+ "," +
            "title" + ":" + getTitle()+ "," +
            "description" + ":" + getDescription()+ "," +
            "img" + ":" + getImg()+ "," +
            "stock" + ":" + getStock()+ "," +
            "price" + ":" + getPrice()+ "," +
            "parentalRating" + ":" + getParentalRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "category = "+(getCategory()!=null?Integer.toHexString(System.identityHashCode(getCategory())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "promotion = "+(getPromotion()!=null?Integer.toHexString(System.identityHashCode(getPromotion())):"null");
  }
}



