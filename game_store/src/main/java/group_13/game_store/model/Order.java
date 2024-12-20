package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// line 59 "model.ump"
// line 191 "model.ump"
@Entity
// Need to add this annotation to specify the table name because order is a reserved keyword in SQL
@Table(name = "game_order")
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  @Id
  @GeneratedValue
  private int orderID;
  private Date purchaseDate;
  private double totalPrice;
  private Date returnDate;
  private boolean isReturned;

  //Order Associations
  @ManyToOne
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // Hibernate needs a no-args constructor, but it can be protected
  protected Order() {
  }

  public Order(Date aPurchaseDate, Date aReturnDate, Customer aCustomer)
  {
    purchaseDate = aPurchaseDate;
    returnDate = aReturnDate;  
    isReturned = (returnDate != null);
    
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

  public boolean setTotalPrice(double aTotalPrice)
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
    isReturned = true;
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

  public double getTotalPrice()
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


