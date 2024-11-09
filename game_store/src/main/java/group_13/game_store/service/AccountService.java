package group_13.game_store.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import group_13.game_store.dto.CustomerResponseDto;
import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.repository.UserAccountRepository;
import jakarta.transaction.Transactional;


@Service
public class AccountService {
    @Autowired
    private UserAccountRepository userAccountRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PaymentInformationRepository paymentInfoRepo;

    @Autowired
    private AddressRepository addressRepo;


    public UserAccount findUserByUsername(String username) {
		UserAccount anAccount = userAccountRepo.findByUsername(username);
		if (anAccount == null) {
			throw new IllegalArgumentException("No user with username with " + username + " exists.");
		}
		return anAccount;
	}

    public Customer findCustomerByUsername(String username) {
		Customer anAccount = customerRepo.findByUsername(username);
		if (anAccount == null) {
			throw new IllegalArgumentException("No customer username with " + username + " exists.");
		}
		return anAccount;
	}

    // Method to get the permission level of a user based on their username
    public int findPermissionLevelByUsername(String username) {
        return userAccountRepo.findByUsername(username).getPermissionLevel();
    }

    // Method to check if a user has permission to perform a certain action
    public boolean hasPermission(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) == permissionLevel);
    }

    // Method to check if a user has permission level above a certain threshold
    public boolean hasPermissionAtLeast(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) >= permissionLevel);
    }

    // method to allow user to change phone number
    @Transactional
    public boolean changePhoneNumber(String newPhoneNumber, String username) {
       // validate new phone number
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (newPhoneNumber == null || !newPhoneNumber.matches(regex)) {
            System.out.println("Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx");
            return false;
        }

        // Now check that the user with the given username exists
        UserAccount person = userAccountRepo.findByUsername(username);
        if (person == null) {
            System.out.println("No user exists with the given username.");
            return false;
        }

        person.setPhoneNumber(newPhoneNumber);
        person = userAccountRepo.save(person);
        return true;
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
        UserAccount person = userAccountRepo.findByUsername(username);
        if (person == null) {
            System.out.println("No user exists with the given username.");
            return false;
        }

        // Now we can change their password
        String newHashedPassword = hashPassword(newPassword);
        person.setPassword(newHashedPassword);
        person = userAccountRepo.save(person);
        return true;
    }

    // Method to allow guests to create an account
    @Transactional
    public Customer createCustomerAccount(String name, String username, String email, String password, String phoneNumber) {
        // Validate that the username is unique
        UserAccount usernameCheck = userAccountRepo.findByUsername(username);
        if (usernameCheck != null) {
            System.out.println("An account already exists with this username, please select a different username");
            return null;
        }
        
        // Validate that the email is unique
        UserAccount emailCheck = userAccountRepo.findByEmail(email);
        if (emailCheck != null) {
            System.out.println("An account already exists with this email address, please select a different email");
            return null;
        }

        // Validate the phone number
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (phoneNumber == null || !phoneNumber.matches(regex)) {
            System.out.println("Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx");
            return null;
        }

        // Validate the password (MANY STEPS)
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return null;
        }
        
        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return null;
        }
        
        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return null;
        }
        
        // Check if the password contains at least one number
        if (!password.matches(".*[0-9].*")) {
            System.out.println("Password must contain at least one number.");
            return null;
        }
        
        // Hash the password before we save it
        String hashedPassword = hashPassword(password);

        // Create the customer account
        Customer newCustomerAccount = new Customer(name, username, email, hashedPassword, phoneNumber);
        newCustomerAccount.setPermissionLevel(1);
        userAccountRepo.save(newCustomerAccount);
        return newCustomerAccount;
    }


    // Method to login to an account
    @Transactional
    public boolean loginToAccount(String username, String password) {
        // Check that account with input username exists
        UserAccount user = userAccountRepo.findByUsername(username);
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
        UserAccount user = userAccountRepo.findByUsername(username);
        
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

    // Method to get payment info from a certain customer
    @Transactional
    public PaymentInformation getPaymentInformationByCustomerUsername(String username) {
        Customer customer = customerRepo.findByUsername(username);
        
        if (customer == null) {
            System.out.println("Ur mom hehehe! Also the customer is null");
        }

        PaymentInformation paymentInfo = paymentInfoRepo.findByPaymentInfoID(customer.getPaymentInformation().getPaymentInfoID());

        if (paymentInfo == null) {
            System.out.println("Payment info is null btw for this customer");
        }

        return paymentInfo;
    }


    // Method to get paymentinfo id from customer username
    @Transactional
    public int getPaymentInfoIdByUsername(String username) {
        Customer customer = customerRepo.findByUsername(username);

        if (customer == null) {
            System.out.println("This customer is null");
        }

        PaymentInformation paymentInfo = paymentInfoRepo.findByPaymentInfoID(customer.getPaymentInformation().getPaymentInfoID());

        if (paymentInfo == null) {
            System.out.println("Payment info is null for this customer");
        }

        return paymentInfo.getPaymentInfoID();
    }

    // Method to get an address given an address id
    @Transactional
    public Address getAddressById(int addressId) {
        Address address = addressRepo.findByAddressID(addressId);

        if (address == null) {
            System.out.println("The address is null");
        }

        return address;
    }


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

    @Transactional
    public boolean addAddressToUser(String username, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        // Validate that the user exists
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            System.out.println("No user exists with the given username.");
            return false;
        }
        // Ensure the user is a customer
        if (!(user instanceof Customer)) {
            System.out.println("The given user is not a Customer");
            return false;
        }

        // Validate the address fields
        if (street == null || street.trim().isEmpty()) {
            System.out.println("Street cannot be empty.");
            return false;
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            System.out.println("Postal code cannot be empty.");
            return false;
        }
        if (number <= 0) {
            System.out.println("House/building number must be a positive integer.");
            return false;
        }
        if (city == null || city.trim().isEmpty()) {
            System.out.println("City cannot be empty.");
            return false;
        }
        if (stateOrProvince == null || stateOrProvince.trim().isEmpty()) {
            System.out.println("State/Province cannot be empty.");
            return false;
        }
        if (country == null || country.trim().isEmpty()) {
            System.out.println("Country cannot be empty.");
            return false;
        }
        if (apartmentNo != null && apartmentNo <= 0) {
            System.out.println("Apartment number must be a positive integer if specified.");
            return false;
        }

        // Create and save the new address
        Address newAddress = new Address(street, postalCode, number, city, stateOrProvince, country, apartmentNo != null ? apartmentNo : 0);
        
        addressRepo.save(newAddress); // Persist the address in the database

        // Associate the address with the customer
        Customer customer = (Customer) user;
        customer.setAddress(newAddress);
        customerRepo.save(customer); // Save the updated customer with the new address

        System.out.println("Address added successfully.");
        return true;
    }
    

    @Transactional
    public boolean updateAddressForUser(String username, int addressId, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        // Validate that the user exists
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            System.out.println("No user exists with the given username.");
            return false;
        }
        // Ensure the user is a customer
        if (!(user instanceof Customer)) {
            System.out.println("The given user is not a Customer.");
            return false;
        }

        // Retrieve the address and validate it exists
        Address address = addressRepo.findByAddressID(addressId);
        if (address == null) {
            System.out.println("No address exists with the given address ID.");
            return false;
        }

        // Validate the address fields
        if (street == null || street.trim().isEmpty()) {
            System.out.println("Street cannot be empty.");
            return false;
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            System.out.println("Postal code cannot be empty.");
            return false;
        }
        if (number <= 0) {
            System.out.println("House/building number must be a positive integer.");
            return false;
        }
        if (city == null || city.trim().isEmpty()) {
            System.out.println("City cannot be empty.");
            return false;
        }
        if (stateOrProvince == null || stateOrProvince.trim().isEmpty()) {
            System.out.println("State/Province cannot be empty.");
            return false;
        }
        if (country == null || country.trim().isEmpty()) {
            System.out.println("Country cannot be empty.");
            return false;
        }
        if (apartmentNo != null && apartmentNo <= 0) {
            System.out.println("Apartment number must be a positive integer if specified.");
            return false;
        }

        // Update the address fields
        address.setStreet(street);
        address.setPostalCode(postalCode);
        address.setNumber(number);
        address.setCity(city);
        address.setStateOrProvince(stateOrProvince);
        address.setCountry(country);
        address.setApartmentNo(apartmentNo != null ? apartmentNo : 0); // Set apartment number or 0 if null

        addressRepo.save(address); // Save the updated address in the database

        System.out.println("Address updated successfully.");
        return true;
    }



    @Transactional
    public Address getAddressForUser(String username) {
        // Retrieve the user by their username
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            System.out.println("No user exists with the given username.");
            return null; // Or throw a custom exception if preferred
        }

        // Ensure the user is a customer
        if (!(user instanceof Customer)) {
            System.out.println("The given user is not a Customer.");
            return null; // Or throw a custom exception if preferred
        }

        // Retrieve the customer's address
        Customer customer = (Customer) user;
        Address address = customer.getAddress(); //Need to add function to model
        if (address == null) {
            System.out.println("No address found for the given user.");
            return null; // Or throw a custom exception if preferred
        }

        return address;
    }

}
