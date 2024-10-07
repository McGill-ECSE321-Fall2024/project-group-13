package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;
import java.util.*;

// line 64 "model.ump"
// line 201 "model.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int orderID;
  private Date purchaseDate;
  private int totalPrice;
  private Date returnDate;
  private boolean isReturned;

  //Order Associations
  private List<GameCopy> gameCopies;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aOrderID, Date aPurchaseDate, int aTotalPrice, Date aReturnDate, boolean aIsReturned)
  {
    orderID = aOrderID;
    purchaseDate = aPurchaseDate;
    totalPrice = aTotalPrice;
    returnDate = aReturnDate;
    isReturned = aIsReturned;
    gameCopies = new ArrayList<GameCopy>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrderID(int aOrderID)
  {
    boolean wasSet = false;
    orderID = aOrderID;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPrice(int aTotalPrice)
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setReturnDate(Date aReturnDate)
  {
    boolean wasSet = false;
    returnDate = aReturnDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsReturned(boolean aIsReturned)
  {
    boolean wasSet = false;
    isReturned = aIsReturned;
    wasSet = true;
    return wasSet;
  }

  public int getOrderID()
  {
    return orderID;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public int getTotalPrice()
  {
    return totalPrice;
  }

  public Date getReturnDate()
  {
    return returnDate;
  }

  public boolean getIsReturned()
  {
    return isReturned;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsReturned()
  {
    return isReturned;
  }
  /* Code from template association_GetMany */
  public GameCopy getGameCopy(int index)
  {
    GameCopy aGameCopy = gameCopies.get(index);
    return aGameCopy;
  }

  public List<GameCopy> getGameCopies()
  {
    List<GameCopy> newGameCopies = Collections.unmodifiableList(gameCopies);
    return newGameCopies;
  }

  public int numberOfGameCopies()
  {
    int number = gameCopies.size();
    return number;
  }

  public boolean hasGameCopies()
  {
    boolean has = gameCopies.size() > 0;
    return has;
  }

  public int indexOfGameCopy(GameCopy aGameCopy)
  {
    int index = gameCopies.indexOf(aGameCopy);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGameCopies()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addGameCopy(GameCopy aGameCopy)
  {
    boolean wasAdded = false;
    if (gameCopies.contains(aGameCopy)) { return false; }
    gameCopies.add(aGameCopy);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGameCopy(GameCopy aGameCopy)
  {
    boolean wasRemoved = false;
    if (gameCopies.contains(aGameCopy))
    {
      gameCopies.remove(aGameCopy);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameCopyAt(GameCopy aGameCopy, int index)
  {  
    boolean wasAdded = false;
    if(addGameCopy(aGameCopy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameCopies()) { index = numberOfGameCopies() - 1; }
      gameCopies.remove(aGameCopy);
      gameCopies.add(index, aGameCopy);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameCopyAt(GameCopy aGameCopy, int index)
  {
    boolean wasAdded = false;
    if(gameCopies.contains(aGameCopy))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameCopies()) { index = numberOfGameCopies() - 1; }
      gameCopies.remove(aGameCopy);
      gameCopies.add(index, aGameCopy);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameCopyAt(aGameCopy, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    gameCopies.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "orderID" + ":" + getOrderID()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "," +
            "isReturned" + ":" + getIsReturned()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "returnDate" + "=" + (getReturnDate() != null ? !getReturnDate().equals(this)  ? getReturnDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}
