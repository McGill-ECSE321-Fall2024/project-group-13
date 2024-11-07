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
    public GameListResponseDto getGamesStartingWith(@RequestParam String startingWith,
            @RequestParam String loggedInUsername) {

        // Check if the user is at least an employee
        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, return all games that start with the
        // given title unfiltered
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
    public GameListResponseDto getGamesByCategory(@RequestParam String categoryName,
            @RequestParam String loggedInUsername) {

        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, return all games by category name
        // unfiltered
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
    public GameResponseDto getGameById(@PathVariable int gameID, @RequestParam String loggedInUsername) {

        // Check if the logged in user is at least an employee
        boolean isAtLeastEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);

        // If the user is at least an employee, let them query unfiltered all games
        if (isAtLeastEmployee) {
            Game foundGame = browsingService.getGameById(gameID);

            // Convert the found game into a DTO
            GameResponseDto gameResponseDto = new GameResponseDto(foundGame.getGameID(), foundGame.getTitle(),
                    foundGame.getDescription(),
                    foundGame.getImg(), foundGame.getStock(), foundGame.getPrice(), foundGame.getParentalRating(),
                    foundGame.getStatus(),
                    foundGame.getCategory().getName(), foundGame.getPromotion().getTitle());

            return gameResponseDto;

        }

        // If the user is a customer or Guest, only allow them to browse available games
        else {
            Game foundGame = browsingService.getAvailableGameById(gameID);

            // Convert the found game into a DTO
            GameResponseDto gameResponseDto = new GameResponseDto(foundGame.getGameID(), foundGame.getTitle(),
                    foundGame.getDescription(),
                    foundGame.getImg(), foundGame.getStock(), foundGame.getPrice(), foundGame.getParentalRating(),
                    foundGame.getStatus(),
                    foundGame.getCategory().getName(), foundGame.getPromotion().getTitle());

            return gameResponseDto;
        }
    }

    // Get games which are pending archived / requested to be archived by employees
    // (Owner)
    @GetMapping("/games?isPendingArchive={isPendingArchive}?loggedInUser={loggedInUsername}")
    public GameListResponseDto getGameArchiveRequests(@RequestParam boolean isPendingArchive,
            @RequestParam String loggedInUsername) {
        // Check if the user is the owner
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        // If the user is not the owner, throw permission denied exception -- come back
        // to handling this
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to view pending archive requests.");
        }

        if (!isPendingArchive) {
            // throw an exception and status code
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"); // come back to this
        } else {
            // Retreive the games which are marked to be reviewed by the owner, which
            // requested to be archived by employees
            List<Game> games = gameStoreManagementService.getGameArchiveRequests();
            ArrayList<GameResponseDto> gamesDtos = new ArrayList<GameResponseDto>();

            for (Game game : games) {
                GameResponseDto gameDto = new GameResponseDto(game.getGameID(), game.getTitle(),
                        game.getDescription(),
                        game.getImg(), game.getStock(), game.getPrice(), game.getParentalRating(), game.getStatus(),
                        game.getCategory().getName(), game.getPromotion().getTitle());
                gamesDtos.add(gameDto);
            }
            return new GameListResponseDto(gamesDtos);
        }
    }

    // will probably also eventually need to implement same method for
    // pendingVisible ?? if employees can suggest games to be visible

    // Add a game to the store
    @PostMapping("/games?loggedInUser={loggedInUsername}")
    public GameResponseDto addGame(@RequestBody GameRequestDto gameRequestDto, @RequestParam String loggedInUsername) {
        // Check if the user is the owner
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add games.");
        }

        // Use the service to add the game and retrieve the created Game entity
        Game createdGame = gameStoreManagementService.addGame(
                loggedInUsername,
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getImg(),
                gameRequestDto.getStock(),
                gameRequestDto.getPrice(),
                gameRequestDto.getParentalRating(),
                gameRequestDto.getStatus(),
                gameRequestDto.getCategoryId());

        // Construct the response DTO from the created Game entity -- Kinda sketch need to revisit
        GameResponseDto gameResponseDto = new GameResponseDto(
                createdGame.getGameID(),
                createdGame.getTitle(),
                createdGame.getDescription(),
                createdGame.getImg(),
                createdGame.getStock(),
                createdGame.getPrice(),
                createdGame.getParentalRating(),
                createdGame.getStatus(),
                createdGame.getCategory().getName(),
                createdGame.getPromotion() != null ? createdGame.getPromotion().getTitle() : null);

        // Return the response DTO
        return gameResponseDto;
    }

    // Archive a game from the store if owner or request to archive a game if employee
    @DeleteMapping("/games/{gameID}?loggedInUser={loggedInUsername}")
    public void archiveGame(@RequestParam int gameID, @RequestParam String loggedInUsername) {
        // Check if the user is the owner
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        // if user is the owner, archive the game
        if (isOwner) {
            gameStoreManagementService.archiveGame(gameID, loggedInUsername);
        }

        // if user is an employee, request to archive the game
        boolean isEmployee = accountService.hasPermission(loggedInUsername, 2);
        if (isEmployee) {
            gameStoreManagementService.archiveGameRequest(gameID, loggedInUsername);
        }

        // if user is not an employee or owner, throw a permission denied exception
        if (!isOwner && !isEmployee) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to archive games.");
        }
    }

    // Update a game in the store
    @PutMapping("/games/{gameID}?loggedInUser={loggedInUsername}")
    public GameResponseDto updateGame(@PathVariable int gameID, @RequestBody GameRequestDto gameRequestDto,
            @RequestParam String loggedInUsername) {
        // Check if the user is the owner
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update games.");
        }

        // Use the service to update the game and retrieve the updated Game
        Game updatedGame = gameStoreManagementService.updateGame(
                gameID,
                gameRequestDto.getTitle(),
                gameRequestDto.getDescription(),
                gameRequestDto.getImg(),
                gameRequestDto.getStock(),
                gameRequestDto.getPrice(),
                gameRequestDto.getParentalRating(),
                gameRequestDto.getStatus(),
                gameRequestDto.getCategoryId(),
                loggedInUsername);
        
        // Construct the response DTO from the updated Game entity 
        GameResponseDto gameResponseDto = new GameResponseDto(
                updatedGame.getGameID(),
                updatedGame.getTitle(),
                updatedGame.getDescription(),
                updatedGame.getImg(),
                updatedGame.getStock(),
                updatedGame.getPrice(),
                updatedGame.getParentalRating(),
                updatedGame.getStatus(),
                updatedGame.getCategory().getName(),
                updatedGame.getPromotion() != null ? updatedGame.getPromotion().getTitle() : null);
        
        return gameResponseDto;

    }
    

}