package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// line 52 "model.ump"
// line 183 "model.ump"
@Entity
@Table(name = "cart_assistant")
public class CartItem implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  @EmbeddedId
  private CartItemId id;
  
  //CartItem Attributes
  @Column(name = "no_game_copies", nullable = false)
  private int quantity;

  //CartItem Associations
  @MapsId("username")
  @ManyToOne
  @JoinColumn(name = "username", nullable = false)
  private Customer customer;

  @MapsId("gameId")
  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
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


@Embeddable
class CartItemId implements Serializable {
  @Column(name = "username")
  private String username;

  @Column(name = "game_id")
  private Long gameId;

  // Default constructor
  public CartItemId() {}

  // Constructor
  public CartItemId(String username, Long gameId) {
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
    CartItemId that = (CartItemId) o;
    return username.equals(that.username) && gameId.equals(that.gameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, gameId);
  }
}

