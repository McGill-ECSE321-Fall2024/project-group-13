package group_13.game_store.dto;

public class AddressRequestDto {

    private String street;
    private String postalCode;
    private int number;
    private String city;
    private String stateOrProvince;
    private String country;
    private int apartmentNo;

    public AddressRequestDto() {}

    public AddressRequestDto(String street, String postalCode, int number, String city, String stateOrProvince, String country, int apartmentNo) {
        this.street = street;
        this.postalCode = postalCode;
        this.number = number;
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.country = country;
        this.apartmentNo = apartmentNo;
    }

    // Getters and setters

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(int apartmentNo) {
        this.apartmentNo = apartmentNo;
    }
}