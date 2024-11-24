package group_13.game_store.dto;

import java.sql.Date;

public class ReturnOrderRequestDto {
    private Date returnDate;
    private String customerUsername;
    
    protected ReturnOrderRequestDto() {

    }

    public ReturnOrderRequestDto(Date returnDate, String customerUsername) {
        this.returnDate = returnDate;
        this.customerUsername = customerUsername;
        //this.orderId = orderId
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getCustomerUsername() {
        return this.customerUsername;
    }
}
