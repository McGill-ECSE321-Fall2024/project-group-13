package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

// line 59 "model.ump"
// line 189 "model.ump"
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
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aOrderID, Date aPurchaseDate, int aTotalPrice, Date aReturnDate, boolean aIsReturned, Customer aCustomer)
  {
    orderID = aOrderID;
    purchaseDate = aPurchaseDate;
    totalPrice = aTotalPrice;
    returnDate = aReturnDate;
    isReturned = aIsReturned;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create Order due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    customer = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "orderID" + ":" + getOrderID()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "," +
            "isReturned" + ":" + getIsReturned()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "returnDate" + "=" + (getReturnDate() != null ? !getReturnDate().equals(this)  ? getReturnDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}


