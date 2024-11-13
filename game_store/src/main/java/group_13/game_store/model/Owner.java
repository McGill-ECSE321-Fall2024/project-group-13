package group_13.game_store.model;

import jakarta.persistence.Entity;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 43 "model.ump"
// line 173 "model.ump"
@Entity
public class Owner extends UserAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  //No args constructor for hibernate.
  protected Owner()
  {
  }

  public Owner(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber);
    setPermissionLevel(3);
  }

  //------------------------
  // INTERFACE
  //------------------------

  //Method to get the permission level from the owner
  public int getPermissionLevel()
  {
    return 3;
  }

  public void delete()
  {
    super.delete();
  }

}



