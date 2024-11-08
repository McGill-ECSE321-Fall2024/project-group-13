package group_13.game_store.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.DeliveryInformation;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.DeliveryInformationRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;
import jakarta.transaction.Transactional;


@Service
public class AccountService {
    @Autowired
    private UserAccountRepository repo;

    @Autowired
    private PaymentInformationRepository paymentInfoRepo;

    @Autowired
    private DeliveryInformationRepository deliveryInfoRepo;


    
    public UserAccount findCustomerByUsername(String username) {
		UserAccount anAccount = repo.findByUsername(username);
		if (anAccount == null) {
			throw new IllegalArgumentException("No customer username with " + username + " exists.");
		}
		return anAccount;
	}

    // Method to get the permission level of a user based on their username
    public int findPermissionLevelByUsername(String username) {
        return repo.findByUsername(username).getPermissionLevel();
    }

    // Method to check if a user has permission to perform a certain action
    public boolean hasPermission(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) == permissionLevel);
    }

    // Method to check if a user has permission level above a certain threshold
    public boolean hasPermissionAtLeast(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) >= permissionLevel);
    }

    // Method to allow user to change password
    @Transactional
    public boolean changePassword(String newPassword, String username) {
        // Check if the password is at least 8 characters long
        if (newPassword.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        
        // Check if the password contains at least one uppercase letter
        if (!newPassword.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        
        // Check if the password contains at least one lowercase letter
        if (!newPassword.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        
        // Check if the password contains at least one number
        if (!newPassword.matches(".*[0-9].*")) {
            System.out.println("Password must contain at least one number.");
            return false;
        }
        
        // If all checks pass, then the password is valid
        // Now check that the user with the given username exists
        UserAccount person = repo.findByUsername(username);
        if (person == null) {
            System.out.println("No user exists with the given username.");
            return false;
        }

        // Now we can change their password
        person.setPassword(newPassword);
        return true;
    }

    // Method to allow guests to create an account
    @Transactional
    public boolean createCustomerAccount(String name, String username, String email, String password, String phoneNumber) {
        // Validate that the username is unique
        UserAccount usernameCheck = repo.findByUsername(username);
        if (usernameCheck != null) {
            System.out.println("An account already exists with this username, please select a different username");
            return false;
        }
        
        // Validate that the email is unique
        UserAccount emailCheck = repo.findByEmail(email);
        if (emailCheck != null) {
            System.out.println("An account already exists with this email address, please select a different email");
            return false;
        }

        // Validate the phone number
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (phoneNumber == null || !phoneNumber.matches(regex)) {
            System.out.println("Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx");
            return false;
        }

        // Validate the password (MANY STEPS)
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        
        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        
        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        
        // Check if the password contains at least one number
        if (!password.matches(".*[0-9].*")) {
            System.out.println("Password must contain at least one number.");
            return false;
        }
        
        // Hash the password before we save it
        String hashedPassword = hashPassword(password);

        // Create the customer account
        Customer newCustomerAccount = new Customer(name, username, email, hashedPassword, phoneNumber);
        newCustomerAccount.setPermissionLevel(1);
        repo.save(newCustomerAccount);
        return true;
    }


    // Method to login to an account
    @Transactional
    public boolean loginToAccount(String username, String password) {
        // Check that account with input username exists
        UserAccount user = repo.findByUsername(username);
        if (user == null) {
            System.out.println("There are no users with the given username.");
            return false;
        }

        // Check that the password matches with the encrypted account password
        String hashedPassword = hashPassword(password);
        if (hashedPassword != user.getPassword()) {
            System.out.println("Password does not match the account with the given username.");
            return false;
        }

        // Make the 'currently logged in' account this one somehow...
        // TODO AFTER DISCUSSION

        return true;
    }


    // Method to encrypt a password
    @Transactional
    public static String hashPassword(String password) {
        // Create a MessageDigest instance to use SHA-256 hashing
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // Hash the password
            byte[] hashedBytes = messageDigest.digest(password.getBytes());

            // Convert into hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString(); // Return the hashed password

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found.", e);
        }

    }

    // Method to create/update payment info
    @Transactional
    public boolean changePaymentInfo(String username, long cardNumber, String billingName, Date expiryDate, int cvvCode, Address billingAddress) {
        // Create a new PaymentInfo object if they don't have one yet
        UserAccount user = repo.findByUsername(username);
        
        if (user == null) {
            System.out.println("No such user with the given username exists");
            return false;
        }
        // Ensures that the given user is a customer
        if (!(user instanceof Customer)) {
            System.out.println("The given user is not a Customer");
            return false;
        }

        // Typecasts to a customer and checks if they have a PaymentInfo or not.
        // If they don't then create one with the given information
        Customer customer = (Customer) user;
        PaymentInformation paymentInfo = customer.getPaymentInformation();

        // Validates input payment information
        boolean valid = validatePaymentInfo(cardNumber, expiryDate, cvvCode, billingAddress, billingName);
        if (!valid) {
            // Calling the validate method will print something if there is a problem
            return false;
        }

        if (paymentInfo == null) {
            PaymentInformation newPaymentInfo = new PaymentInformation(cardNumber, billingName, expiryDate, cvvCode, billingAddress);
            paymentInfoRepo.save(newPaymentInfo); // save the new PaymentInfo into the database
            customer.setPaymentInformation(newPaymentInfo);
            return true;
        }

        // Update the PaymentInfo object if they already have one using setters
        paymentInfo.setBillingAddress(billingAddress);
        paymentInfo.setBillingName(billingName);
        paymentInfo.setCardNumber(cardNumber);
        paymentInfo.setCvvCode(cvvCode);
        paymentInfo.setExpiryDate(expiryDate);
        return true;
    }

    // Method to create/update delivery info
    @Transactional
    public boolean changeDeliveryInfo(String username, String deliveryName, Address deliveryAddress) {
         // Create a new PaymentInfo object if they don't have one yet
         UserAccount user = repo.findByUsername(username);
        
         if (user == null) {
             System.out.println("No such user with the given username exists");
             return false;
         }
         // Ensures that the given user is a customer
         if (!(user instanceof Customer)) {
            System.out.println("The given user is not a Customer");
             return false;
         }
 
         // Typecasts to a customer and checks if they have a DeliveryInformation or not.
         // If they don't then create one with the given information
         Customer customer = (Customer) user;
         DeliveryInformation deliveryInfo = customer.getDeliveryInformation();
 
         // Validates input delivery information
         boolean valid = validateDeliveryInfo(deliveryName, deliveryAddress);
         if (!valid) {
            // Calling the validate method will print something if there is a problem
             return false;
         }
 
         if (deliveryInfo == null) {
             DeliveryInformation newDeliveryInfo = new DeliveryInformation(deliveryName, deliveryAddress);
             deliveryInfoRepo.save(newDeliveryInfo); // save the new DeliveryInformation into the database
             customer.setDeliveryInformation(newDeliveryInfo);
             return true;
         }
 
         // Update the DeliveryInformation object if they already have one using setters
         deliveryInfo.setDeliveryAddress(deliveryAddress);
         deliveryInfo.setDeliveryName(deliveryName);
         return true;
    }

    // Method to validate payment info
    public boolean validatePaymentInfo(long cardNumber, Date expiryDate, int cvvCode, Address billingAddress, String billingName) {
        // Validate given information
        // Check the card number length
        if (Long.toString(cardNumber).length() != 16)
        {
            System.out.print("Customer does not have valid credit card number");
            return false;
        }

        // Ensure that they give a 3 digit CVV code
        if (Integer.toString(cvvCode).length() != 3)
        {
            System.out.print("Customer does not have valid CVV code");
            return false;
        }

        // Ensure that the card is not expired
        Date currentDate = new Date(System.currentTimeMillis());
        if (expiryDate.before(currentDate))
        {
            System.out.print("Customer credit card is expired");
            return false;
        }

        // Ensure that they enter a cardholder name
        if (billingName == null) {
            System.out.print("Please enter the cardholder name");
            return false;
        }

        return true;
    }

    // Method to validate delivery info
    public boolean validateDeliveryInfo(String deliveryName, Address deliveryAddress) {
        // Ensure that they enter a delivery name
        if (deliveryName == null) {
            System.out.println("Please enter a name for this delivery information");
            return false;
        }
        return true;
    }
}
