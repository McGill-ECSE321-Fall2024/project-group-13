package group_13.game_store.dto;

import java.time.LocalDate;

public class ReturnOrderRequestDto {
    private LocalDate returnDate;
    private String customerUsername;
    
    protected ReturnOrderRequestDto() {

    }

    public ReturnOrderRequestDto(LocalDate returnDate, String customerUsername) {
        this.returnDate = returnDate;
        this.customerUsername = customerUsername;
        //this.orderId = orderId
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getCustomer() {
        return this.customerUsername;
    }
}
