package group_13.game_store.dto;

import group_13.game_store.model.UserAccount;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Customer;

public class UserAccountResponseDto {
    
    private String username;
    private String name;
    private String email;
    // including password compromises security
    private String phoneNumber;
    private int permissionLevel;
    // includinng date of account creation is useful
    // private LocalDate creationDate;

    public UserAccountResponseDto(UserAccount aUserAccount) {
		this.username = aUserAccount.getUsername();
		this.name = aUserAccount.getName();
		this.email = aUserAccount.getEmail();
        this.phoneNumber = aUserAccount.getPhoneNumber();
        // need this, because otherwise getPermissionLevel will not return permission level any other way
        this.permissionLevel = aUserAccount.getPermissionLevel();
	}
    
    public static UserAccountResponseDto create(UserAccount aUserAccount) {

        if (aUserAccount instanceof Customer) {
            return new CustomerResponseDto((Customer) aUserAccount);
        } else if (aUserAccount instanceof Employee) {
            return new EmployeeResponseDto((Employee) aUserAccount);
        } else {
            throw new IllegalArgumentException("Unknown account type.");
        }
    }

     public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }
}
