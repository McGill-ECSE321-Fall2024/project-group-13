package group_13.game_store.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 91 "model.ump"
// line 205 "model.ump"
@Entity
public class GameCategory
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum VisibilityStatus { Visible, Archived, PendingArchive, PendingVisible }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCategory Attributes
  @Id
  @GeneratedValue
  private int categoryID;
  private String description;
  private VisibilityStatus status;
  private String name;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCategory(String aDescription, VisibilityStatus aStatus, String aName)
  {
    description = aDescription;
    status = aStatus;
    name = aName;
  }

  // Constructor for Hibernate
  public GameCategory() {
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCategoryID(int aCategoryID)
  {
    boolean wasSet = false;
    categoryID = aCategoryID;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(VisibilityStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public int getCategoryID()
  {
    return categoryID;
  }

  public String getDescription()
  {
    return description;
  }

  public VisibilityStatus getStatus()
  {
    return status;
  }

  public String getName()
  {
    return name;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "categoryID" + ":" + getCategoryID()+ "," +
            "description" + ":" + getDescription()+ "," +
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null");
  }
}


