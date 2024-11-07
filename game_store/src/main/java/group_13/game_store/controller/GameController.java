package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import group_13.game_store.service.AccountService;
import group_13.game_store.service.BrowsingService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.service.ReviewService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import group_13.game_store.dto.GameListResponseDto;
import group_13.game_store.model.Game;
import group_13.game_store.dto.GameResponseDto;
import java.util.ArrayList;
import java.util.List;


@RestController
public class GameController {
    @Autowired
    BrowsingService browsingService;

    @Autowired
    GameStoreManagementService gameStoreManagementService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    AccountService accountService;

    // Get method for all games depending on usertype
    @GetMapping("/games?loggedInUser={loggedInUsername}")
    public GameListResponseDto getAllGames(@RequestParam String loggedInUsername) {

        // Check if the user is at least an employee
        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, return all games with no filter
        if (isAtLeastEmployee) {
            Iterable<Game> games = browsingService.getAllGames();

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

        // If the user is not at least an employee, query only available games
        else {
            List<Game> games = browsingService.getAllAvailableGames();

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

    }

    // Search bar functionality get all games that start with the given title
    @GetMapping("/games?startingWith={startingWith}&loggedInUser={loggedInUsername}")
    public GameListResponseDto getGamesStartingWith(@RequestParam String startingWith, @RequestParam String loggedInUsername) {

        // Check if the user is at least an employee
        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, return all games that start with the given title unfiltered
        if (isAtLeastEmployee) {
            Iterable<Game> games = browsingService.getGamesByTitleStartingWith(startingWith);

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

        // If the user is not at least an employee, query only available games
        else {
            // Get all available games that start with the given title
            List<Game> games = browsingService.getAvailableGamesByTitleStartingWith(startingWith);

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

    }

    // Get all games by category name
    @GetMapping("/games?category={categoryName}&loggedInUser={loggedInUsername}")
    public GameListResponseDto getGamesByCategory(@RequestParam String categoryName, @RequestParam String loggedInUsername) {

        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, return all games by category name unfiltered
        if (isAtLeastEmployee) {
            Iterable<Game> games = browsingService.getGamesByCategoryName(categoryName);

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

        // If the user is not at least an employee, query only available games
        else {
            List<Game> games = browsingService.getAvailableGamesByCategoryName(categoryName);

            List<GameResponseDto> gameResponseDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameResponseDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gameResponseDtos.add(gameResponseDto);
            }

            return new GameListResponseDto(gameResponseDtos);
        }

    }

    // Get a game by its ID
    @GetMapping("/games/{gameID}?loggedInUser={loggedInUsername}")
    public GameResponseDto getGameById(@PathVariable int gameID, @RequestParam String loggedInUsername){

        // Check if the logged in user is at least an employee
        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, let them query unfiltered all games
        if (isAtLeastEmployee) {
            Game foundGame = browsingService.getGameById(gameID);

            // Convert the found game into a DTO
            GameResponseDto gameResponseDto = new GameResponseDto(foundGame.getGameID(), foundGame.getTitle(),
                    foundGame.getDescription(),
                    foundGame.getImg(), foundGame.getStock(), foundGame.getPrice(), foundGame.getParentalRating(), foundGame.getStatus(),
                    foundGame.getCategory().getName(), foundGame.getPromotion().getTitle());
            
            return gameResponseDto;

        }

        // If the user is a customer or Guest, only allow them to browse available games
        else {
            Game foundGame = browsingService.getAvailableGameById(gameID);

            // Convert the found game into a DTO
            GameResponseDto gameResponseDto = new GameResponseDto(foundGame.getGameID(), foundGame.getTitle(),
                    foundGame.getDescription(),
                    foundGame.getImg(), foundGame.getStock(), foundGame.getPrice(), foundGame.getParentalRating(), foundGame.getStatus(),
                    foundGame.getCategory().getName(), foundGame.getPromotion().getTitle());
            
            return gameResponseDto;
        }


    }



}
