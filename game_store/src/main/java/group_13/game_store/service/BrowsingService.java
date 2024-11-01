package group_13.game_store.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import jakarta.transaction.Transactional;
import group_13.game_store.model.Game;
import java.util.List;

@Service
public class BrowsingService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ************************** EMPLOYEE AND OWNER BROWSING **************************

    // Gets all the games in the database
    @Transactional
    public Iterable<Game> getAllGames() {
        Iterable<Game> allGames = gameRepository.findAll();
        
        if (!allGames.iterator().hasNext()) {
            // Indicate that there are no games
        }
        return allGames;
    }

    // Gets a game by its ID
    @Transactional
    public Game getGameById(int gameID) {
        Game game = gameRepository.findByGameID(gameID);

        if (game == null) {
            // Indicate that the game does not exist
        }

        return game;
    }

    // Gets all the games in a category
    @Transactional
    public List<Game> getGamesByCategoryName(String category) {
        List<Game> games = gameRepository.findByCategory_Name(category);

        if (games.isEmpty()) {
            // Indicate that there are no games in the category
        }

        return games;
    }

    // Gets all the games with a title starting with the given string
    @Transactional
    public List<Game> getGamesByTitleStartingWith(String title) {
        List<Game> games = gameRepository.findByTitleStartingWith(title);

        if (games.isEmpty()) {
            // Indicate that there are no games with the given name
        }

        return games;
    }

    // ************************** CUSTOMER BROWSING **************************

    // Gets all the games in the database that are available
    @Transactional
    public List<Game> getAvailableGames() {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);
        List<Game> games = gameRepository.findByStockGreaterThanAndStatusIn(0, visibleStatuses);

        if (games.isEmpty()) {
            // Indicate that there are no games available
        }

        return games;
    }

    // Gets a game by its ID if available
    @Transactional
    public Game getAvailableGameById(int gameID) {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);

        Game game = gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(gameID, 0, visibleStatuses);

        if (game == null) {
            // Indicate that the game does not exist
        }

        return game;
    }

    // Gets all the games in a category that are available
    @Transactional
    public List<Game> getAvailableGamesByCategoryName(String category) {
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);
        
        List<Game> games = gameRepository.findByCategory_NameAndStockGreaterThanAndStatusIn(category, 0, visibleStatuses);

        if (games.isEmpty()){
            // Indicate that there are no available games in this category
        }

        return games;

    }

    // Gets all the games with a title starting with the given string
    @Transactional
    public List<Game> getAvailableGamesByTitleStartingWith(String title){
        List<Game.VisibilityStatus> visibleStatuses = List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive);

        List<Game> games = gameRepository.findByTitleStartingWithAndStockGreaterThanAndStatusIn(title, 0, visibleStatuses);

        if (games.isEmpty()) {
            // Indicate that there are no available games in starting with this title
        }

        return games;
    }

    

}
