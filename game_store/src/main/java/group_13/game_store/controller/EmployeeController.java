package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.dto.EmployeeListResponseDto;
import group_13.game_store.dto.EmployeeResponseDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.model.Employee;
import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;

@RestController
public class EmployeeController {

    @Autowired
    private GameStoreManagementService gameStoreService;

    @Autowired
    private AccountService accountService;

    /**
     * Retrieves all employees.
     *
     * @param loggedInUsername The username of the logged-in user.
     * @return A list of all employees.
     */
    @GetMapping("/employees")
    public EmployeeListResponseDto getAllEmployee(@RequestParam String loggedInUsername) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view employees.");
        }

        Iterable<Employee> employees = gameStoreService.getAllEmployees();

        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);
            employeeResponseDtos.add(employeeResponseDto);
        }

        return new EmployeeListResponseDto(employeeResponseDtos);
    }

    /**
     * Creates or updates an employee.
     *
     * @param userAccountRequestDto The employee data.
     * @param username              The username of the employee.
     * @param loggedInUsername      The username of the logged-in user.
     * @param isUpdate              True if updating an existing employee; false if creating a new one.
     * @return The created or updated employee.
     */
    @PutMapping("/employees/{username}")
    public EmployeeResponseDto createEmployee(@RequestBody UserAccountRequestDto userAccountRequestDto,
                                              @PathVariable String username,
                                              @RequestParam String loggedInUsername,
                                              @RequestParam boolean isUpdate) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update employees.");
        }

        if (isUpdate) {
            gameStoreService.updateEmployee(userAccountRequestDto.getName(), username,
                    userAccountRequestDto.getEmail(), userAccountRequestDto.getPassword(),
                    userAccountRequestDto.getPhoneNumber(), userAccountRequestDto.getIsActive());
        } else {
            gameStoreService.addEmployee(userAccountRequestDto.getName(), username,
                    userAccountRequestDto.getEmail(), userAccountRequestDto.getPassword(),
                    userAccountRequestDto.getPhoneNumber(), userAccountRequestDto.getIsActive());
        }

        Employee employee = gameStoreService.getEmployeeByUsername(username);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);

        return employeeResponseDto;
    }

    /**
     * Retrieves an employee by username.
     *
     * @param username         The username of the employee.
     * @param loggedInUsername The username of the logged-in user.
     * @return The employee with the given username.
     */
    @GetMapping("/employees/{username}")
    public EmployeeResponseDto getEmployeeByUsername(@PathVariable String username,
                                                     @RequestParam String loggedInUsername) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view employees.");
        }

        Employee employee = gameStoreService.getEmployeeByUsername(username);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);

        return employeeResponseDto;
    }

    /**
     * Deletes (archives) an employee by username.
     *
     * @param username         The username of the employee to delete.
     * @param loggedInUsername The username of the logged-in user.
     * @return The archived employee.
     */
    @DeleteMapping("/employees/{username}")
    public EmployeeResponseDto deleteEmployeeByUsername(@PathVariable String username,
                                                        @RequestParam String loggedInUsername) {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete employees.");
        }

        gameStoreService.archiveEmployeeAccount(username);

        Employee employee = gameStoreService.getEmployeeByUsername(username);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);

        return employeeResponseDto;
    }
}