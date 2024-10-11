package group_13.game_store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 121 "model.ump"
// line 221 "model.ump"
@Entity
public class Address
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Address Attributes
  @Id
  @GeneratedValue 
  private int addressID;
  private String street;
  private String postalCode;
  private int number;
  private String city;
  private String stateOrProvince;
  private String country;
  private int apartmentNo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Address(String aStreet, String aPostalCode, int aNumber, String aCity, String aStateOrProvince, String aCountry, int aApartmentNo)
  {
    street = aStreet;
    postalCode = aPostalCode;
    number = aNumber;
    city = aCity;
    stateOrProvince = aStateOrProvince;
    country = aCountry;
    apartmentNo = aApartmentNo;
  }

  // Constructor for Hibernate
  public Address() {
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAddressID(int aAddressID)
  {
    boolean wasSet = false;
    addressID = aAddressID;
    wasSet = true;
    return wasSet;
  }

  public boolean setStreet(String aStreet)
  {
    boolean wasSet = false;
    street = aStreet;
    wasSet = true;
    return wasSet;
  }

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    number = aNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCity(String aCity)
  {
    boolean wasSet = false;
    city = aCity;
    wasSet = true;
    return wasSet;
  }

  public boolean setStateOrProvince(String aStateOrProvince)
  {
    boolean wasSet = false;
    stateOrProvince = aStateOrProvince;
    wasSet = true;
    return wasSet;
  }

  public boolean setCountry(String aCountry)
  {
    boolean wasSet = false;
    country = aCountry;
    wasSet = true;
    return wasSet;
  }

  public boolean setApartmentNo(int aApartmentNo)
  {
    boolean wasSet = false;
    apartmentNo = aApartmentNo;
    wasSet = true;
    return wasSet;
  }

  public int getAddressID()
  {
    return addressID;
  }

  public String getStreet()
  {
    return street;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public int getNumber()
  {
    return number;
  }

  public String getCity()
  {
    return city;
  }

  public String getStateOrProvince()
  {
    return stateOrProvince;
  }

  public String getCountry()
  {
    return country;
  }

  public int getApartmentNo()
  {
    return apartmentNo;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "addressID" + ":" + getAddressID()+ "," +
            "street" + ":" + getStreet()+ "," +
            "postalCode" + ":" + getPostalCode()+ "," +
            "number" + ":" + getNumber()+ "," +
            "city" + ":" + getCity()+ "," +
            "stateOrProvince" + ":" + getStateOrProvince()+ "," +
            "country" + ":" + getCountry()+ "," +
            "apartmentNo" + ":" + getApartmentNo()+ "]";
  }
}


