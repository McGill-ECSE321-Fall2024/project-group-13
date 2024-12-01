package group_13.game_store.dto;

import java.sql.Date;
import java.util.List;

import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Order;

public class OrderCreationResponseDto {
    private Date purchaseDate;
    private double totalPrice;
    private boolean isReturned;
    private String customerUsername;
    private int orderId;
    private List<GameCopy> gameCopies;
    protected OrderCreationResponseDto() {

    }
    public OrderCreationResponseDto(Order order, List<GameCopy> gameCopies) {
        this.purchaseDate = Date.valueOf(order.getPurchaseDate().toLocalDate().plusDays(1)); 
        this.totalPrice = order.getTotalPrice();
        this.isReturned = order.getIsReturned();
        this.customerUsername = order.getCustomer().getUsername();
        this.orderId = order.getOrderID();
        this.gameCopies = gameCopies;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public List<GameCopy> getGameCopies() {
        return gameCopies;
    }
}

