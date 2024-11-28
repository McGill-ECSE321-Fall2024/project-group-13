package group_13.game_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.LoginRequestDto;
import group_13.game_store.dto.LoginResponseDto;
import group_13.game_store.service.AccountService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {
    @Autowired
    AccountService accountService;

    /**
     * Logs a user into the system after validating their credentials (Only not logged in users a.k.a. guests)
     * 
     * @param loggedInUsername  The user name of the person currently logged in. If no one is logged in, it is equal to 'guest'.
     * @param loginRequestDto   Login credentials (username and password) to login with
     * @return                  The user name of the user that logged in, if successful
     */
    @PostMapping("/login")
    public LoginResponseDto loginToUserAccount(@RequestParam String loggedInUsername, @RequestBody LoginRequestDto loginRequestDto) {
        if (accountService.hasPermissionAtLeast(loggedInUsername, 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already logged in");
        }

        String logUsername = accountService.loginToAccount(
            loginRequestDto.getUsername(), 
            loginRequestDto.getPassword());
        
        LoginResponseDto response = new LoginResponseDto(logUsername, accountService.findPermissionLevelByUsername(logUsername));
        return response;
    }
}
