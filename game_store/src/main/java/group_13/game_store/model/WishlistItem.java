package group_13.game_store.model;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 52 "model.ump"
// line 190 "model.ump"
public class WishlistItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WishlistItem Attributes
  private String wishlistItemID;

  //WishlistItem Associations
  private Game addedgames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WishlistItem(String aWishlistItemID, Game aAddedgames)
  {
    wishlistItemID = aWishlistItemID;
    if (!setAddedgames(aAddedgames))
    {
      throw new RuntimeException("Unable to create WishlistItem due to aAddedgames. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setWishlistItemID(String aWishlistItemID)
  {
    boolean wasSet = false;
    wishlistItemID = aWishlistItemID;
    wasSet = true;
    return wasSet;
  }

  public String getWishlistItemID()
  {
    return wishlistItemID;
  }
  /* Code from template association_GetOne */
  public Game getAddedgames()
  {
    return addedgames;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setAddedgames(Game aNewAddedgames)
  {
    boolean wasSet = false;
    if (aNewAddedgames != null)
    {
      addedgames = aNewAddedgames;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    addedgames = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "wishlistItemID" + ":" + getWishlistItemID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "addedgames = "+(getAddedgames()!=null?Integer.toHexString(System.identityHashCode(getAddedgames())):"null");
  }
}