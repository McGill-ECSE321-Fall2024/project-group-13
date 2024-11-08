package group_13.game_store.dto;

import java.sql.Date;

import group_13.game_store.model.Customer;

public class OrderRequestDto {
    private Date purchaseDate;
    private int totalPrice;
    private Date returnDate;
    private boolean isReturned;
    private Customer customer;
    
    public OrderRequestDto(Date purchaseDate, Date returnDate, Customer customer) {
        this.purchaseDate = purchaseDate;
        this.customer = customer;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Date returnDate() {
        return returnDate;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setPurchaseDate(Date aPurchaseDate) {
        this.purchaseDate = aPurchaseDate;
    }

    public void setIsReturned(boolean newStatus) {
        this.isReturned = newStatus;
    }

    public void setTotalPrice(int aTotalPrice) {
        this.totalPrice = aTotalPrice;
    }

    public void setReturnDate(Date aReturnDate) {
        this.returnDate = aReturnDate;
    }
}
