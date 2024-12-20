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
import group_13.game_store.dto.GameRequestDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    /** 
     * Gets all games with optional filtering by title prefix or category, customers can only see available games but employees can see all games
     * 
     * @param title the title prefix to filter by (for the search bar)
     * @param category the category name to filter by
     * @param loggedInUsername the username of the logged in user 
     * 
     * @return a list of games that match the filters
     */
    @GetMapping("/games")
    public GameListResponseDto getGames(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam String loggedInUsername) {

        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        List<Game> games;

        if (title != null) {
            // Search games by title starting with
            if (isAtLeastEmployee) {
                games = browsingService.getGamesByTitleStartingWith(title);
            } else {
                games = browsingService.getAvailableGamesByTitleStartingWith(title);
            }
        } else if (category != null) {
            // Get games by category
            if (isAtLeastEmployee) {
                games = browsingService.getGamesByCategoryName(category);
            } else {
                games = browsingService.getAvailableGamesByCategoryName(category);
            }
        } else {
            // Get all games
            if (isAtLeastEmployee) {
                games = (List<Game>) browsingService.getAllGames();
            } else {
                games = browsingService.getAllAvailableGames();
            }
        }

        List<GameResponseDto> gameResponseDtos = new ArrayList<>();

        for (Game game : games) {
            GameResponseDto gameResponseDto = new GameResponseDto(
                    game.getGameID(),
                    game.getTitle(),
                    game.getDescription(),
                    game.getImg(),
                    game.getStock(),
                    game.getPrice(),
                    game.getParentalRating(),
                    game.getStatus().toString(),
                    game.getCategory().getCategoryID(),
                    game.getPromotion() != null ? game.getPromotion().getTitle() : null, game.getCategory().getName(), game.getPromotion() != null ? game.getPromotion().getPercentage() : 0, reviewService.getGameRating(game.getGameID()));
            gameResponseDtos.add(gameResponseDto);
        }

        return new GameListResponseDto(gameResponseDtos);
    }

    /**
     * Get a game by its ID, customers can only see available games but employees can see all games
     * @param gameID the ID of the game
     * @param loggedInUsername the username of the logged in user
     * @return the game with the given ID
     */

    @GetMapping("/games/{gameID}")
    public GameResponseDto getGameById(@PathVariable int gameID, @RequestParam String loggedInUsername) {

        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        Game foundGame;

        if (isAtLeastEmployee) {
            foundGame = browsingService.getGameById(gameID);
        } else {
            foundGame = browsingService.getAvailableGameById(gameID);
        }

        if (foundGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        GameResponseDto gameResponseDto = new GameResponseDto(
                foundGame.getGameID(),
                foundGame.getTitle(),
                foundGame.getDescription(),
                foundGame.getImg(),
                foundGame.getStock(),
                foundGame.getPrice(),
                foundGame.getParentalRating(),
                foundGame.getStatus().toString(),
                foundGame.getCategory().getCategoryID(),
                foundGame.getPromotion() != null ? foundGame.getPromotion().getTitle() : null, 
                foundGame.getCategory().getName(), foundGame.getPromotion() != null ? foundGame.getPromotion().getPercentage() : 0, reviewService.getGameRating(foundGame.getGameID()));

        return gameResponseDto;
    }

    /**
     * Add a game to the store (Owner only)
     * @param gameRequestDto the game to add
     * @param loggedInUsername the username of the logged in user
     * @return the game that was added
     */
    @PostMapping("/games")
    public GameResponseDto addGame(@RequestBody GameRequestDto gameRequestDto,
            @RequestParam String loggedInUsername) {

        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add games.");
        }


        Game createdGame = gameStoreManagementService.addGame(
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getImg(),
                gameRequestDto.getStock(),
                gameRequestDto.getPrice(),
                gameRequestDto.getParentalRating(),
                stringToStatus(gameRequestDto.getStatus()),
                gameRequestDto.getCategoryId());

        // Create the game in the database

        GameResponseDto gameResponseDto = new GameResponseDto(
                createdGame.getGameID(),
                createdGame.getTitle(),
                createdGame.getDescription(),
                createdGame.getImg(),
                createdGame.getStock(),
                createdGame.getPrice(),
                createdGame.getParentalRating(),
                createdGame.getStatus().toString(),
                createdGame.getCategory().getCategoryID(),
                createdGame.getPromotion() != null ? createdGame.getPromotion().getTitle() : null,
                createdGame.getCategory().getName(), createdGame.getPromotion() != null ? createdGame.getPromotion().getPercentage() : 0, reviewService.getGameRating(createdGame.getGameID()));

        return gameResponseDto;
    }

    /**
     * Update a game (Owner only)
     * @param gameID the ID of the game to update
     * @param gameRequestDto the updated game
     * @param loggedInUsername the username of the logged in user
     * @return the updated game
     */
    @PutMapping("/games/{gameID}")
    public GameResponseDto updateGame(@PathVariable int gameID,
            @RequestBody GameRequestDto gameRequestDto,
            @RequestParam String loggedInUsername) {

        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update games.");
        }

        Game updatedGame = gameStoreManagementService.updateGame(
                gameID,
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getImg(),
                gameRequestDto.getStock(),
                gameRequestDto.getPrice(),
                gameRequestDto.getParentalRating(),
                stringToStatus(gameRequestDto.getStatus()),
                gameRequestDto.getCategoryId());

        GameResponseDto gameResponseDto = new GameResponseDto(
                updatedGame.getGameID(),
                updatedGame.getTitle(),
                updatedGame.getDescription(),
                updatedGame.getImg(),
                updatedGame.getStock(),
                updatedGame.getPrice(),
                updatedGame.getParentalRating(),
                updatedGame.getStatus().toString(),
                updatedGame.getCategory().getCategoryID(),
                updatedGame.getPromotion() != null ? updatedGame.getPromotion().getTitle() : null,
                updatedGame.getCategory().getName(), updatedGame.getPromotion() != null ? updatedGame.getPromotion().getPercentage() : 0, reviewService.getGameRating(updatedGame.getGameID()));

        return gameResponseDto;
    }

    /**
     * Depending on the user's permission level, either archive a game (Owner) or request to archive a game (Employee)
     * @param gameID the ID of the game to archive
     * @param loggedInUsername the username of the logged in user
     * 
     * @return void
    */
    @DeleteMapping("/games/{gameID}")
    public void archiveGame(@PathVariable int gameID, @RequestParam String loggedInUsername) {

        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        boolean isEmployee = accountService.hasPermission(loggedInUsername, 2);

        if (!isOwner && !isEmployee) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to archive games.");
        }

        //check if the title is Rounds or Rainbow Six Siege and if it is, they can't be archived 
        //Other games include Civilization VI, Destiny 2 and Alto's Collection 
        Game game = browsingService.getGameById(gameID);
        if (game.getTitle().equals("Rounds") || 
            game.getTitle().equals("Rainbow Six Siege") || 
            game.getTitle().equals("Civilization VI") || 
            game.getTitle().equals("Destiny 2") || 
            game.getTitle().equals("Alto's Collection")
        ) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't archive this game.");
        }

        if (isOwner) {
            gameStoreManagementService.archiveGame(gameID);
        } else {
            gameStoreManagementService.archiveGameRequest(gameID);
        }

    }

    /**
     * Get all games that have been requested to be archived by employees (Owner only)
     * @param loggedInUsername the username of the logged in user
     * 
     * @return a list of games that have been requested to be archived by employees
     */
    @GetMapping("/games/archive-requests")
    public GameListResponseDto getGameArchiveRequests(@RequestParam String loggedInUsername) {

        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to view pending archive requests.");
        }

        List<Game> games = gameStoreManagementService.getGameArchiveRequests();

        List<GameResponseDto> gameResponseDtos = new ArrayList<>();

        for (Game game : games) {
            GameResponseDto gameDto = new GameResponseDto(
                    game.getGameID(),
                    game.getTitle(),
                    game.getDescription(),
                    game.getImg(),
                    game.getStock(),
                    game.getPrice(),
                    game.getParentalRating(),
                    game.getStatus().toString(),
                    game.getCategory().getCategoryID(),
                    game.getPromotion() != null ? game.getPromotion().getTitle() : null,
                    game.getCategory().getName(), game.getPromotion() != null ? game.getPromotion().getPercentage() : 0, reviewService.getGameRating(game.getGameID()));
            gameResponseDtos.add(gameDto);
        }

        return new GameListResponseDto(gameResponseDtos);
    }


    // Helper method to convert a string to a Game.VisibilityStatus
    private static Game.VisibilityStatus stringToStatus(String status) {
        if (status.equals("Visible")) {
            return Game.VisibilityStatus.Visible;
        } else if (status.equals("Archived")) {
            return Game.VisibilityStatus.Archived;
        } else if (status.equals("PendingVisible")) {
            return Game.VisibilityStatus.PendingVisible;
        } else {
            // default status if not one of the above
            return Game.VisibilityStatus.Archived;
        }
    }
}
