package group_13.game_store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.service.AccountService;
import group_13.game_store.service.GameStoreManagementService;
import group_13.game_store.dto.EmployeeListResponseDto;
import group_13.game_store.dto.EmployeeResponseDto;
import group_13.game_store.dto.UserAccountRequestDto;
import group_13.game_store.model.Employee;

public class EmployeeController {

    @Autowired
    private GameStoreManagementService gameStoreService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/employees")
    public EmployeeListResponseDto getAllEmployee(@RequestParam String loggedInUsername)
    {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view employees.");
        }

        Iterable<Employee> employees = gameStoreService.getAllEmployees();

        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<EmployeeResponseDto>();

        for (Employee employee : employees)
        {
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);
            employeeResponseDtos.add(employeeResponseDto);
        }

        return new EmployeeListResponseDto(employeeResponseDtos);
    }


    @PutMapping("/employees/{username}")
    public EmployeeResponseDto createEmployee(@RequestBody UserAccountRequestDto userAccountRequestDto, @PathVariable String username, @RequestParam String loggedInUsername, @RequestParam boolean isUpdate)
    {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update employees.");
        }

        if (isUpdate)
        {
            gameStoreService.updateEmployee(userAccountRequestDto.getName(), username,
                                            userAccountRequestDto.getEmail(), userAccountRequestDto.getPassword(),
                                            userAccountRequestDto.getPhoneNumber(), userAccountRequestDto.getIsActive());
        }

        else
        {
            gameStoreService.addEmployee(userAccountRequestDto.getName(), username, 
                                    userAccountRequestDto.getEmail(), userAccountRequestDto.getPassword(), 
                                    userAccountRequestDto.getPhoneNumber(), userAccountRequestDto.getIsActive());
        }
        
        Employee employee = gameStoreService.getEmployeeByUsername(username);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);

        return employeeResponseDto;
    }

    @GetMapping("/employees/{username}")
    public EmployeeResponseDto getEmployeeByUsername(@PathVariable String username, @RequestParam String loggedInUsername)
    {
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

    @DeleteMapping("/employees/{username}")
    public EmployeeResponseDto deleteEmployeeByUsername(@PathVariable String username, @RequestParam String loggedInUsername)
    {
        // Check if the user is at least an owner
        boolean isOwner = accountService.hasPermissionAtLeast(loggedInUsername, 3);

        // If the user is not the owner, throw a permission denied exception
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete employees.");
        }
        
        gameStoreService.archiveEmployeeAccount(loggedInUsername);

        Employee employee = gameStoreService.getEmployeeByUsername(username);

        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(employee);

        return employeeResponseDto;
    }
}
