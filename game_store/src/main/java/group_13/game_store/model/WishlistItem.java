package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 48 "model.ump"
// line 178 "model.ump"
public class WishlistItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WishlistItem Associations
  private Customer customer;
  private Game addedgames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WishlistItem(Customer aCustomer, Game aAddedgames)
  {
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create WishlistItem due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setAddedgames(aAddedgames))
    {
      throw new RuntimeException("Unable to create WishlistItem due to aAddedgames. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public Game getAddedgames()
  {
    return addedgames;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
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
    customer = null;
    addedgames = null;
  }

}
