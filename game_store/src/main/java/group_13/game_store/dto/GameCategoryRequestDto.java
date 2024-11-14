package group_13.game_store.dto;

public class GameCategoryRequestDto {
    private String description;
    private String name;

    protected GameCategoryRequestDto() {
    }

    public GameCategoryRequestDto(String description, String name)
    {
        this.description = description;
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
