package group_13.game_store.dto;

import java.util.List;

public class CustomerListDto {
    private List<CustomerResponseDto> customers;

    protected CustomerListDto(){
        
    }
    public CustomerListDto(List<CustomerResponseDto> customers) {
        this.customers = customers;
    }

    public List<CustomerResponseDto> customers() {
        return customers;
    }

    public void setCustomers(List<CustomerResponseDto> customers) {
        this.customers = customers;
    }
}
