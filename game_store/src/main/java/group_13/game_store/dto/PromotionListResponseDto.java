package group_13.game_store.dto;

import java.util.List;

import group_13.game_store.model.Promotion;

public class PromotionListResponseDto {
    private List<Promotion> promotions;

    public PromotionListResponseDto(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    //No setters as it is a response object
    public List<Promotion> getPromotions() {
        return promotions;
    }
}