package group_13.game_store.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 37 "model.ump"
// line 231 "model.ump"
@Entity
@DiscriminatorValue("Employee")
public class Employee extends UserAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Attributes
  private boolean isActive;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // Constructor for Hibernate
  protected Employee()
  {
  }

  public Employee(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber, boolean aIsActive)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber);
    isActive = aIsActive;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsActive(boolean aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  //Method to get the permission level from an employee
  public int getPermissionLevel()
  {
    return 2;
  }

  public boolean getIsActive()
  {
    return isActive;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActive()
  {
    return isActive;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "isActive" + ":" + getIsActive()+ "]";
  }
}



