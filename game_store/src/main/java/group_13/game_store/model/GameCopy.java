package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 86 "model.ump"
// line 200 "model.ump"
public class GameCopy
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCopy Associations
  private Order order;
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCopy(Order aOrder, Game aGame)
  {
    if (!setOrder(aOrder))
    {
      throw new RuntimeException("Unable to create GameCopy due to aOrder. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create GameCopy due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setOrder(Order aNewOrder)
  {
    boolean wasSet = false;
    if (aNewOrder != null)
    {
      order = aNewOrder;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setGame(Game aNewGame)
  {
    boolean wasSet = false;
    if (aNewGame != null)
    {
      game = aNewGame;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    order = null;
    game = null;
  }

}



