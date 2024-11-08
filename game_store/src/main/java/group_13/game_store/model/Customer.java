package group_13.game_store.model;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

// line 12 "model.ump"
// line 155 "model.ump"
@Entity
public class Customer extends UserAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  @OneToOne(optional=true)
  private PaymentInformation paymentInformation;
  @OneToOne(optional=true)
  private Address address;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // Constructor for Hibernate
  protected Customer()
  {
  }

  public Customer(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public PaymentInformation getPaymentInformation()
  {
    return paymentInformation;
  }

  //Method to get the permission level from a customer
  public int getPermissionLevel()
  {
    return 1;
  }

  public boolean hasPaymentInformation()
  {
    boolean has = paymentInformation != null;
    return has;
  }

  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPaymentInformation(PaymentInformation aNewPaymentInformation)
  {
    boolean wasSet = false;
    paymentInformation = aNewPaymentInformation;
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_GetOne */
  public Address getAddress()
  {
    return address;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setAddress(Address aNewAddress)
  {
    boolean wasSet = false;
    if (aNewAddress != null)
    {
      address = aNewAddress;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    paymentInformation = null;
    address = null;
    super.delete();
  }

}



