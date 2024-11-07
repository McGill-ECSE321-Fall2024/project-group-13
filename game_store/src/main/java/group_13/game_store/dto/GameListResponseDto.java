package group_13.game_store.dto;
import java.util.List;

public class GameListResponseDto { 

    private List<GameResponseDto> games;

    public GameListResponseDto(List<GameResponseDto> games) {
        this.games = games;
    }

    public List<GameResponseDto> getGames() {
        return games;
    }

    public void setGames(List<GameResponseDto> games) {
        this.games = games;
    }
}
