package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EmbeddedId;

// line 52 "model.ump"
// line 183 "model.ump"
@Entity
public class CartItem {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------
  @EmbeddedId
  private Key key;

  // CartItem Attributes
  private int quantity;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------
  protected CartItem() {
  }

  public CartItem(Key key, int quantity) {
    this.key = key;
    this.quantity = quantity;
  }

  public Key getKey() {
    return key;
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setQuantity(int aQuantity) {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public int getQuantity() {
    return quantity;
  }

  @Embeddable
  public static class Key implements Serializable {
    @ManyToOne
    private Customer customerAccount;

    @ManyToOne
    private Game game;

    public Key() {
      super();
    }

    public Key(Customer customerAccount, Game game) {
      this.customerAccount = customerAccount;
      this.game = game;
    }

    public Customer getCustomerAccount() {
      return customerAccount;
    }

    public Game getGame() {
      return game;
    }

    // Equals and hashCode
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Key)) {
        return false;
      }
      Key that = (Key) obj;
      return this.getCustomerAccount().getUsername() == that.getCustomerAccount().getUsername()
          && this.getGame().getGameID() == that.getGame().getGameID();
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.getCustomerAccount().getUsername(), this.getGame().getGameID());
    }
  }
}