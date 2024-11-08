package group_13.game_store.dto;

import group_13.game_store.model.Address;

public class DeliveryInformationResponseDto {
    private int deliveryInfoID;
    private String deliveryName;
    private Address deliveryAddress;

    // Empty constructor for hibernate
    protected DeliveryInformationResponseDto() {
    }

    public DeliveryInformationResponseDto(int deliveryInfoID, String deliveryName, Address deliveryAddress) {
        this.deliveryInfoID = deliveryInfoID;
        this.deliveryName = deliveryName;
        if (!setDeliveryAddress(deliveryAddress))
            {
            throw new RuntimeException("Unable to create DeliveryInformation due to aDeliveryAddress. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
            }
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public boolean setDeliveryAddress(Address newDeliveryAddress) {
        boolean wasSet = false;
        if (newDeliveryAddress != null)
        {
          deliveryAddress = newDeliveryAddress;
          wasSet = true;
        }
        return wasSet;
    }

    public int getDeliveryInfoID() {
        return deliveryInfoID;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String newDeliveryName) {
        this.deliveryName = newDeliveryName;
    }
}
