package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// line 28 "model.ump"
// line 168 "model.ump"
@Entity
public class PaymentInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentInformation Attributes
  @Id
  @GeneratedValue
  private int paymentInfoID;
  private int cardNumber;
  private String billingName;
  private Date expiryDate;
  private int cvvCode;

  //PaymentInformation Associations
  @ManyToOne
  private Address billingAddress;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentInformation(int aPaymentInfoID, int aCardNumber, String aBillingName, Date aExpiryDate, int aCvvCode, Address aBillingAddress)
  {
    paymentInfoID = aPaymentInfoID;
    cardNumber = aCardNumber;
    billingName = aBillingName;
    expiryDate = aExpiryDate;
    cvvCode = aCvvCode;
    if (!setBillingAddress(aBillingAddress))
    {
      throw new RuntimeException("Unable to create PaymentInformation due to aBillingAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPaymentInfoID(int aPaymentInfoID)
  {
    boolean wasSet = false;
    paymentInfoID = aPaymentInfoID;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardNumber(int aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setBillingName(String aBillingName)
  {
    boolean wasSet = false;
    billingName = aBillingName;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiryDate(Date aExpiryDate)
  {
    boolean wasSet = false;
    expiryDate = aExpiryDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvvCode(int aCvvCode)
  {
    boolean wasSet = false;
    cvvCode = aCvvCode;
    wasSet = true;
    return wasSet;
  }

  public int getPaymentInfoID()
  {
    return paymentInfoID;
  }

  public int getCardNumber()
  {
    return cardNumber;
  }

  public String getBillingName()
  {
    return billingName;
  }

  public Date getExpiryDate()
  {
    return expiryDate;
  }

  public int getCvvCode()
  {
    return cvvCode;
  }
  /* Code from template association_GetOne */
  public Address getBillingAddress()
  {
    return billingAddress;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setBillingAddress(Address aNewBillingAddress)
  {
    boolean wasSet = false;
    if (aNewBillingAddress != null)
    {
      billingAddress = aNewBillingAddress;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    billingAddress = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "paymentInfoID" + ":" + getPaymentInfoID()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "billingName" + ":" + getBillingName()+ "," +
            "cvvCode" + ":" + getCvvCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "expiryDate" + "=" + (getExpiryDate() != null ? !getExpiryDate().equals(this)  ? getExpiryDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "billingAddress = "+(getBillingAddress()!=null?Integer.toHexString(System.identityHashCode(getBillingAddress())):"null");
  }
}



