package group_13.game_store.dto;

import java.util.List;

public class OrderListDto {
    private List<OrderResponseDto> orders;

    protected OrderListDto() {
        
    }
    public OrderListDto(List<OrderResponseDto> orders) {
        this.orders = orders;
    }

    public List<OrderResponseDto> getOrders() {
        return orders;
    }

    public void setCustomers(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}
