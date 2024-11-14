package group_13.game_store.dto;
import java.util.List;

public class GameListResponseDto { 

    private List<GameResponseDto> games;

    public GameListResponseDto(List<GameResponseDto> games) {
        this.games = games;
    }

    // defaul constructor
    public GameListResponseDto() {
    }

    public List<GameResponseDto> getGames() {
        return games;
    }

}
