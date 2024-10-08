package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 52 "model.ump"
// line 183 "model.ump"
public class CartItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CartItem Attributes
  private int quantity;

  //CartItem Associations
  private Customer customer;
  private Game addedGames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CartItem(int aQuantity, Customer aCustomer, Game aAddedGames)
  {
    quantity = aQuantity;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create CartItem due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setAddedGames(aAddedGames))
    {
      throw new RuntimeException("Unable to create CartItem due to aAddedGames. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setQuantity(int aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public int getQuantity()
  {
    return quantity;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public Game getAddedGames()
  {
    return addedGames;
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
    customer = null;
    addedGames = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "quantity" + ":" + getQuantity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "addedGames = "+(getAddedGames()!=null?Integer.toHexString(System.identityHashCode(getAddedGames())):"null");
  }
}



