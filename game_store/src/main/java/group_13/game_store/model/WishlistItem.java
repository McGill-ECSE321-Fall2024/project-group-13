package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 48 "model.ump"
// line 178 "model.ump"
@Entity
@Table(name = "wishlist_assistant")
public class WishlistItem implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Composite Key
  @EmbeddedId
  private WishlistItemId id;
  
  //WishlistItem Associations
  @MapsId("username")
  @ManyToOne
  @JoinColumn(name = "username", nullable = false)
  private Customer customer;

  @MapsId("gameId")
  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
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


@Embeddable
class WishlistItemId implements Serializable {
  @Column(name = "username")
  private String username;

  @Column(name = "game_id")
  private Long gameId;

  // Default constructor
  public WishlistItemId() {}

  // Constructor
  public WishlistItemId(String username, Long gameId) {
    this.username = username;
    this.gameId = gameId;
  }

  // Getters and setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  // Equals and hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WishlistItemId that = (WishlistItemId) o;
    return username.equals(that.username) && gameId.equals(that.gameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, gameId);
  }
}
