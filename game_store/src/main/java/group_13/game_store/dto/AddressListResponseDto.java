package group_13.game_store.dto;

import java.util.List;

public class AddressListResponseDto {

    private List<AddressResponseDto> addresses;

    public AddressListResponseDto(List<AddressResponseDto> addresses) {
        this.addresses = addresses;
    }

    public List<AddressResponseDto> getAddresses() {
        return addresses;
    }

}
