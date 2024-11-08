package group_13.game_store.dto;

public class DeliveryInformationRequestDto {
    private String deliveryName;
    private int addressId;
    
    // Empty Constructor for hibernate
    protected DeliveryInformationRequestDto() {
    }

    public DeliveryInformationRequestDto(String deliveryName, int addressId) {
        this.deliveryName = deliveryName;
        this.addressId = addressId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String newDeliveryName) {
        this.deliveryName = newDeliveryName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int newAddressId) {
        this.addressId = newAddressId;
    }
}