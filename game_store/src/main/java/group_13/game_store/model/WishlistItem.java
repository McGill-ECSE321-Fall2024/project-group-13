package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EmbeddedId;

// line 48 "model.ump"
// line 178 "model.ump"
@Entity
public class WishlistItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  @EmbeddedId
  private Key key;
  

  //------------------------
  // CONSTRUCTOR
  //------------------------

  protected WishlistItem(){
  }

  public WishlistItem(Key key){
    this.key = key;
  }

  public Key getKey()
  {
    return key;
  }

@Embeddable
class WishlistItemId implements Serializable {
  @ManyToOne
  private UserAccount userAccount;

  @ManyToOne
  private Game game;

  public Key() {
    super();
  }

  public key(UserAccount userAccount, Game game) {
    this.userAccount = userAccount;
    this.game = game;
  }

  public UserAccount getUserAccount(){
    return userAccount;
  }

  public Game getGame(){
    return game;
  }
  

  // Equals and hashCode
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Key)) {
       return false;
    }
    Key that = (Key) obj;
    return this.getUserAccount().getUserAccountID() == that.getUserAccount().getUserAccountID()
            && this.getGame().getGameID() == that.getGame().getGameID();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getUserAccount().getUserAccountID(), this.getGame().getGameID());
  }
}
