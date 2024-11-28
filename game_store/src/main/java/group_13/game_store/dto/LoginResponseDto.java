package group_13.game_store.dto;

public class LoginResponseDto {
    private String username;
    private int permissionLevel;

    public LoginResponseDto(String username, int permissionLevel) {
        this.username = username;
        this.permissionLevel = permissionLevel;
    }

    public String getUsername() {
        return username;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public void setPassword(int newPermissionLevel) {
        permissionLevel = newPermissionLevel;
    }
}
