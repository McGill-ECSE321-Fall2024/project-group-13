package group_13.game_store.dto;

import group_13.game_store.model.Employee;

public class EmployeeResponseDto extends UserAccountResponseDto {
    private boolean isActive;

    protected EmployeeResponseDto(){}

    public EmployeeResponseDto(Employee aEmployeeAccount) {
        super(aEmployeeAccount);
        this.isActive = aEmployeeAccount.getIsActive();
    }

    public boolean getIsActive() {
        return isActive;
    }
}
