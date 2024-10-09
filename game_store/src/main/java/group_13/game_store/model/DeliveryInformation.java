package group_13.game_store.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DeliveryInformation
{
  // foreign keys
  @EmbeddedId
	private Key key;

  //------------------------
  // MEMBER VARIABLES
  //------------------------
  
  //DeliveryInformation Attributes
  @Id
  @GeneratedValue
  private int deliveryInfoID;
  private String deliveryName;

  //DeliveryInformation Associations
  private Address deliveryAddress;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DeliveryInformation(int aDeliveryInfoID, String aDeliveryName, Address aDeliveryAddress)
  {
    deliveryInfoID = aDeliveryInfoID;
    deliveryName = aDeliveryName;
    if (!setDeliveryAddress(aDeliveryAddress))
    {
      throw new RuntimeException("Unable to create DeliveryInformation due to aDeliveryAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  @Embeddable
  public static class Key implements Serializable {
    @ManyToOne
    private Address addressLocation;

    public Key() {
      // does super here have any args?
      super();
    }

    public Address getAddress() {
			return addressLocation;
		}

    @Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Key)) {
				return false;
			}

      // downcasting object
			Key thatDeliveryInformation = (Key) obj;
			return this.getAddress().getAddressID() == thatDeliveryInformation.getAddress().getAddressID();
		}

    @Override
		public int hashCode() {
			return Objects.hash(this.getAddress().getAddressID());
		}


  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDeliveryInfoID(int aDeliveryInfoID)
  {
    boolean wasSet = false;
    deliveryInfoID = aDeliveryInfoID;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeliveryName(String aDeliveryName)
  {
    boolean wasSet = false;
    deliveryName = aDeliveryName;
    wasSet = true;
    return wasSet;
  }

  public int getDeliveryInfoID()
  {
    return deliveryInfoID;
  }

  public String getDeliveryName()
  {
    return deliveryName;
  }
  /* Code from template association_GetOne */
  public Address getDeliveryAddress()
  {
    return deliveryAddress;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setDeliveryAddress(Address aNewDeliveryAddress)
  {
    boolean wasSet = false;
    if (aNewDeliveryAddress != null)
    {
      deliveryAddress = aNewDeliveryAddress;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    deliveryAddress = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "deliveryInfoID" + ":" + getDeliveryInfoID()+ "," +
            "deliveryName" + ":" + getDeliveryName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryAddress = "+(getDeliveryAddress()!=null?Integer.toHexString(System.identityHashCode(getDeliveryAddress())):"null");
  }
}



