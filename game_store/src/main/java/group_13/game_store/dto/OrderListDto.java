package group_13.game_store.dto;

import java.util.List;

public class OrderListDto {
    private List<OrderResponseDto> orders;

    public OrderListDto(List<OrderResponseDto> orders) {
        this.orders = orders;
    }

    public List<OrderResponseDto> orders() {
        return orders;
    }

    public void setCustomers(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}
