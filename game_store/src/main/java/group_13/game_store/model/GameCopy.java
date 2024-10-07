package group_13.game_store.model;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 90 "model.ump"
// line 218 "model.ump"
public class GameCopy
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCopy Attributes
  private String copyID;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCopy(String aCopyID)
  {
    copyID = aCopyID;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCopyID(String aCopyID)
  {
    boolean wasSet = false;
    copyID = aCopyID;
    wasSet = true;
    return wasSet;
  }

  public String getCopyID()
  {
    return copyID;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "copyID" + ":" + getCopyID()+ "]";
  }
}


