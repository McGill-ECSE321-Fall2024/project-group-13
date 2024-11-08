package group_13.game_store.dto;

import java.util.List;

public class EmployeeListResponseDto {

    private List<EmployeeResponseDto> employees;

    public EmployeeListResponseDto(List<EmployeeResponseDto> employees)
    {
        this.employees = employees;
    }
    public List<EmployeeResponseDto> getGames()
    {
        return employees;
    }
    
}
