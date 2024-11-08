package group_13.game_store.dto;

import java.util.List;

public class GameCategoryListResponseDto {

    private List<GameCategoryResponseDto> gameCategories;

    public GameCategoryListResponseDto(List<GameCategoryResponseDto> gameCategories)
    {
        this.gameCategories = gameCategories;
    }
    public List<GameCategoryResponseDto> getGames()
    {
        return gameCategories;
    }
    
}
