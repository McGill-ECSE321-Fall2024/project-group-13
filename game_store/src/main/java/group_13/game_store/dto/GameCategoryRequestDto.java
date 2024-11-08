package group_13.game_store.dto;

import group_13.game_store.model.GameCategory;

public class GameCategoryRequestDto {
    private String description;
    private String name;

    protected GameCategoryRequestDto() {
    }

    public GameCategoryRequestDto(GameCategory category)
    {
        this.description = category.getDescription();
        this.name = category.getName();
    }

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
