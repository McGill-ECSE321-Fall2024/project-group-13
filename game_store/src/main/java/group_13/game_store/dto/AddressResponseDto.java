package group_13.game_store.dto;

public class AddressResponseDto {

    private int addressID;
    private String street;
    private String postalCode;
    private int number;
    private String city;
    private String stateOrProvince;
    private String country;
    private int apartmentNo;

    public AddressResponseDto() {
    }

    public AddressResponseDto(int addressID, String street, String postalCode, int number, String city, String stateOrProvince, String country, int apartmentNo) {
        this.addressID = addressID;
        this.street = street;
        this.postalCode = postalCode;
        this.number = number;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.country = country;
        this.apartmentNo = apartmentNo;
    }

    public int getAddressID() {
        return addressID;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public int getApartmentNo() {
        return apartmentNo;
    }
}