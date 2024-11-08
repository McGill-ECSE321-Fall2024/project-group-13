package group_13.game_store.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import group_13.game_store.model.GameCategory;
import group_13.game_store.dto.GameCategoryRequestDto;
import group_13.game_store.dto.GameCategoryResponseDto;
import group_13.game_store.dto.GameCategoryListResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;

import java.util.List;


@RestController
public class GameCategoryController 
{
    @Autowired
    private GameStoreManagementService gameStoreService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/categories?loggedInUser={loggedInUsername}")
    public GameCategoryResponseDto createGameCategory(@RequestBody GameCategoryRequestDto categoryToCreate, @RequestParam String loggedInUsername)
    {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add game categories.");
        }

        GameCategory createdCategory = gameStoreService.addCategory(loggedInUsername, 
                                        categoryToCreate.getName(),
                                        categoryToCreate.getDescription());

        GameCategoryResponseDto response = new GameCategoryResponseDto(createdCategory);

        return response;
    }

    @GetMapping("/categories?loggedInUser={loggedInUsername}")
    public GameCategoryListResponseDto getAllGameCategory(@RequestParam String loggedInUsername)
    {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view game categories.");
        }

        Iterable<GameCategory> gameCategories = gameStoreService.getAllCategories();

        List<GameCategoryResponseDto> gameCategoryResponseDtos = new ArrayList<GameCategoryResponseDto>();

        for (GameCategory category : gameCategories)
        {
            GameCategoryResponseDto gameCategoryResponseDto = new GameCategoryResponseDto(category);
            gameCategoryResponseDtos.add(gameCategoryResponseDto);
        }

        return new GameCategoryListResponseDto(gameCategoryResponseDtos);
    }

    @GetMapping("/categories/{categoryID}")
    public GameCategoryResponseDto getGameCategoryById(@PathVariable int gameCategoryID)
    {
        GameCategory category= gameStoreService.getCategoryById(gameCategoryID);

        return new GameCategoryResponseDto(category);
    }

    @GetMapping("/categories/{categoryID}?loggedInUser={loggedInUsername}")
    public void deleteGameCategoryById(@PathVariable int gameCategoryID, @RequestParam String loggedInUsername)
    {
        // Check if the user is atleast employee
        boolean isEmployee = accountService.hasPermissionAtLeast(loggedInUsername, 2);


        // If the user is not the owner, throw a permission denied exception
        if (!isEmployee) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to archive game categories.");
        }

        // Archive game if user is owner, request archive if employee
        gameStoreService.archiveCategory(gameCategoryID, loggedInUsername);
    }

    @GetMapping("/categories?isPendingArchive={isPendingArchive}?loggedInUser={loggedInUsername}")
    public GameCategoryListResponseDto getGameCategoryArchiveRequests(@RequestParam boolean isPendingArchive,
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
            // Retreive the game categories which are marked to be reviewed by the owner, which
            // requested to be archived by employees
            List<GameCategory> gameCategories = gameStoreService.getAllPendingArchiveCategories();
            ArrayList<GameCategoryResponseDto> gameCategoriesDtos = new ArrayList<GameCategoryResponseDto>();

            for (GameCategory gameCategory : gameCategories) {
                GameCategoryResponseDto gameCategoryDto = new GameCategoryResponseDto(gameCategory);
                gameCategoriesDtos.add(gameCategoryDto);
            }
            return new GameCategoryListResponseDto(gameCategoriesDtos);
        }
    }
}
