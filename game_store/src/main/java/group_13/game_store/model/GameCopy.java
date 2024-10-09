package group_13.game_store.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 86 "model.ump"
// line 200 "model.ump"
@Entity
public class GameCopy
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCopy Associations
  private Key key;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // Hibernate needs a no-args constructor, but it can be protected
  protected GameCopy()
  {
  }

  public GameCopy(Key key)
  {
    this.key = key;
  }

  public Key getKey()
  {
    return key;
  }

  @Embeddable
    public static class Key implements Serializable {
      @ManyToOne
      private Order order;
      @ManyToOne
      private Game game;

      public Key() {
        super();
      }

      public Key(Order order, Game game) {
        this.order = order;
        this.game = game;
      }

      public Order getOrder() {
        return order;
      }

      public Game getGame() {
        return game;
      }

      @Override
      public boolean equals(Object obj) {
        if (!(obj instanceof Key)) {
          return false;
        }
        Key that = (Key) obj;
        return this.getOrder().getOrderID() == that.getOrder().getOrderID()
            && this.getGame().getGameID() == that.getGame().getGameID();
      }

      @Override
      public int hashCode() {
        return Objects.hash(this.getOrder().getOrderID(), this.getGame().getGameID());
      }
    }
  }



