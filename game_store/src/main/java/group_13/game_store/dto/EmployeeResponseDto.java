package group_13.game_store.dto;

import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCategory.VisibilityStatus;

public class EmployeeResponseDto {
    private int categoryID;
    private String description;
    private VisibilityStatus status;
    private String name;

    protected EmployeeResponseDto() {
    }

    public EmployeeResponseDto(GameCategory category)
    {
        this.categoryID = category.getCategoryID();
        this.description = category.getDescription();
        this.status = category.getStatus();
        this.name = category.getName();
    }


	public int getId() {
		return categoryID;
	}

	public String getName() {
		return name;
	}

	public String getdescription() {
		return description;
	}

	public VisibilityStatus getStatus() {
		return status;
	}
}
