package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 37 "model.ump"
// line 175 "model.ump"
public class Staff extends Employee
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Staff Attributes
  private boolean isActive;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Staff(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber, int aPermissionLevel, boolean aIsActive)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber, aPermissionLevel);
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


