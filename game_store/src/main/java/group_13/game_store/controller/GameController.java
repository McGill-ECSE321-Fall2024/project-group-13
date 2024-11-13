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

    // Get all games with optional filters
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
                    game.getPromotion() != null ? game.getPromotion().getTitle() : null);
            gameResponseDtos.add(gameResponseDto);
        }

        return new GameListResponseDto(gameResponseDtos);
    }

    // Get a game by its ID
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
                foundGame.getPromotion() != null ? foundGame.getPromotion().getTitle() : null);

        return gameResponseDto;
    }

    // Add a game to the store (Owner only)
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
                createdGame.getPromotion() != null ? createdGame.getPromotion().getTitle() : null);

        return gameResponseDto;
    }

    // Update a game in the store (Owner only)
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
                updatedGame.getPromotion() != null ? updatedGame.getPromotion().getTitle() : null);

        return gameResponseDto;
    }

    // Archive a game (Owner only)
    @DeleteMapping("/games/{gameID}")
    public void archiveGame(@PathVariable int gameID, @RequestParam String loggedInUsername) {

        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        boolean isEmployee = accountService.hasPermission(loggedInUsername, 2);

        if (!isOwner && !isEmployee) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to archive games.");
        }

        if (isOwner) {
            gameStoreManagementService.archiveGame(gameID);
        } else {
            gameStoreManagementService.archiveGameRequest(gameID);
        }

    }

    // Get pending game archive requests (Owner only)
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
                    game.getPromotion() != null ? game.getPromotion().getTitle() : null);
            gameResponseDtos.add(gameDto);
        }

        return new GameListResponseDto(gameResponseDtos);
    }

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
