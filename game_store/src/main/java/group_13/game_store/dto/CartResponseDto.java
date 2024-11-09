package group_13.game_store.dto;

import java.util.List;

public class CartResponseDto extends GameListResponseDto {

    private double subtotalPrice;

    public CartResponseDto(List<GameResponseDto> games, double subtotalPrice) {
        super(games);
        this.subtotalPrice = subtotalPrice;
    }

    public double getSubtotalPrice() {
        return subtotalPrice;
    }

}

