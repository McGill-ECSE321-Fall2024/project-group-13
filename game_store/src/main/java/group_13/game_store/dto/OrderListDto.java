package group_13.game_store.dto;

import java.util.List;

public class OrderListDto {
    private List<OrderCreationResponseDto> orders;

    protected OrderListDto() {
        
    }
    public OrderListDto(List<OrderCreationResponseDto> orders) {
        this.orders = orders;
    }

    public List<OrderCreationResponseDto> getOrders() {
        return orders;
    }

    public void setCustomers(List<OrderCreationResponseDto> orders) {
        this.orders = orders;
    }
}
