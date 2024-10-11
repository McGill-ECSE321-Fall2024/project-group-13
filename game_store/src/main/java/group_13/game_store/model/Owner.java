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

  public Owner(String aName, String aUsername, String aEmail, String aPassword, String aPhoneNumber, int aPermissionLevel)
  {
    super(aName, aUsername, aEmail, aPassword, aPhoneNumber, aPermissionLevel);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}



