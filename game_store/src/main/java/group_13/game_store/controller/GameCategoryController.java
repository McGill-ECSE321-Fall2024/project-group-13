package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.GameCategoryListResponseDto;
import group_13.game_store.dto.GameCategoryRequestDto;
import group_13.game_store.dto.GameCategoryResponseDto;
import group_13.game_store.model.GameCategory;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;

@RestController
public class GameCategoryController {

    @Autowired
    private GameStoreManagementService gameStoreService;

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new game category.
     *
     * @param categoryToCreate  The category to create.
     * @param loggedInUsername  The username of the logged-in user.
     * @return The created game category.
     */
    @PostMapping("/categories")
    public GameCategoryResponseDto createGameCategory(@RequestBody GameCategoryRequestDto categoryToCreate,
                                                      @RequestParam String loggedInUsername) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add game categories.");
        }

        GameCategory createdCategory = gameStoreService.addCategory(
                categoryToCreate.getName(),
                categoryToCreate.getDescription());

        GameCategoryResponseDto response = new GameCategoryResponseDto(createdCategory);

        return response;
    }

    /**
     * Retrieves all game categories.
     *
     * @param loggedInUsername  The username of the logged-in user.
     * @return A list of all game categories.
     */
    @GetMapping("/categories")
    public GameCategoryListResponseDto getAllGameCategory(@RequestParam String loggedInUsername) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);
        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view game categories.");
        } else {
            Iterable<GameCategory> gameCategories = gameStoreService.getAllCategories();

            List<GameCategoryResponseDto> gameCategoryResponseDtos = new ArrayList<>();

            for (GameCategory category : gameCategories) {
                GameCategoryResponseDto gameCategoryResponseDto = new GameCategoryResponseDto(category);
                gameCategoryResponseDtos.add(gameCategoryResponseDto);
            }

            return new GameCategoryListResponseDto(gameCategoryResponseDtos);
        }
    }

    /**
     * Retrieves a game category by its ID.
     *
     * @param categoryID The ID of the category to retrieve.
     * @return The game category with the given ID.
     */
    @GetMapping("/categories/{categoryID}")
    public GameCategoryResponseDto getGameCategoryById(@PathVariable int categoryID) {
        GameCategory category = gameStoreService.getCategoryById(categoryID);

        return new GameCategoryResponseDto(category);
    }

    /**
     * Deletes (archives) a game category by its ID.
     *
     * @param categoryID        The ID of the category to delete.
     * @param loggedInUsername  The username of the logged-in user.
     */
    @DeleteMapping("/categories/{categoryID}")
    public void deleteGameCategoryById(@PathVariable int categoryID, @RequestParam String loggedInUsername) {
        // Check if the user is at least employee
        boolean isStaff = accountService.hasPermissionAtLeast(loggedInUsername, 2);
        // If the user is not the owner, throw a permission denied exception
        if (!isStaff) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to archive game categories.");
        } else {
            gameStoreService.archiveCategory(categoryID, loggedInUsername);
        }
    }

    /**
     * Retrieves all game categories pending archive requests.
     *
     * @param loggedInUsername  The username of the logged-in user.
     * @return A list of game categories pending archive approval.
     */
    @GetMapping("/categories/archive-requests")
    public GameCategoryListResponseDto getGameCategoryArchiveRequests(@RequestParam String loggedInUsername) {
        // Check if the user is the owner
        boolean isOwner = accountService.hasPermission(loggedInUsername, 3);

        // If the user is not the owner, throw permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to view pending archive requests.");
        }

        // Retrieve the game categories which are marked to be reviewed by the owner,
        // which requested to be archived by employees
        List<GameCategory> gameCategories = gameStoreService.getAllPendingArchiveCategories();
        ArrayList<GameCategoryResponseDto> gameCategoriesDtos = new ArrayList<>();

        for (GameCategory gameCategory : gameCategories) {
            GameCategoryResponseDto gameCategoryDto = new GameCategoryResponseDto(gameCategory);
            gameCategoriesDtos.add(gameCategoryDto);
        }
        return new GameCategoryListResponseDto(gameCategoriesDtos);
    }
}