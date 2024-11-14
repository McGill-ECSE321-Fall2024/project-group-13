package group_13.game_store.dto;

import group_13.game_store.model.PaymentInformation;

public class UserAccountRequestDto {
    // for every user account
    private String username;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private int permissionLevel;
    // for employee account
    private boolean isActive;
    // for customer account
    private PaymentInformation paymentInformation;

    protected UserAccountRequestDto() {
        
    }

    public UserAccountRequestDto(String username, String name, String email, String phoneNumber, String password) {
        this.username = username;
		this.name = name;
		this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

}