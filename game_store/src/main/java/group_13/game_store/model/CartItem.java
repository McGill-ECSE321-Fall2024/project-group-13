package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 57 "model.ump"
// line 195 "model.ump"
public class CartItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CartItem Attributes
  private int cartItemID;

  //CartItem Associations
  private Game addedGames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CartItem(int aCartItemID, Game aAddedGames)
  {
    cartItemID = aCartItemID;
    if (!setAddedGames(aAddedGames))
    {
      throw new RuntimeException("Unable to create CartItem due to aAddedGames. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCartItemID(int aCartItemID)
  {
    boolean wasSet = false;
    cartItemID = aCartItemID;
    wasSet = true;
    return wasSet;
  }

  public int getCartItemID()
  {
    return cartItemID;
  }
  /* Code from template association_GetOne */
  public Game getAddedGames()
  {
    return addedGames;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setAddedGames(Game aNewAddedGames)
  {
    boolean wasSet = false;
    if (aNewAddedGames != null)
    {
      addedGames = aNewAddedGames;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    addedGames = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "cartItemID" + ":" + getCartItemID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "addedGames = "+(getAddedGames()!=null?Integer.toHexString(System.identityHashCode(getAddedGames())):"null");
  }
}


