package group_13.game_store.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // Method to get a UserAccount by their username
    public UserAccount findUserByUsername(String username) {
		UserAccount anAccount = userAccountRepo.findByUsername(username);
		if (anAccount == null) {
			// Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
		}
		return anAccount;
	}

    // Method to get a customer by their username
    public Customer findCustomerByUsername(String username) {
		Customer anAccount = customerRepo.findByUsername(username);
		if (anAccount == null) {
			// Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
		}
		return anAccount;
	}

    // Method to get the permission level of a user based on their username
    public int findPermissionLevelByUsername(String username) {
        UserAccount user = userAccountRepo.findByUsername(username);

        if (user == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }

        return user.getPermissionLevel();
    }

    // Method to check if a user has permission to perform a certain action
    public boolean hasPermission(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) == permissionLevel);
    }

    // Method to check if a user has permission level above a certain threshold
    public boolean hasPermissionAtLeast(String username, int permissionLevel) {
        return (findPermissionLevelByUsername(username) >= permissionLevel);
    }

    // Method to allow user to change phone number
    @Transactional
    public boolean changePhoneNumber(String newPhoneNumber, String username) {
    // validate new phone number
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (newPhoneNumber == null || !newPhoneNumber.matches(regex)) {
            // Indicate that the phone number is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx");
        }

        // Now check that the user with the given username exists
        UserAccount person = userAccountRepo.findByUsername(username);
        if (person == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }

        person.setPhoneNumber(newPhoneNumber);
        person = userAccountRepo.save(person);
        return true;
    }

    // Method to allow user to change password
    @Transactional
    public void changePassword(String newPassword, String username) {
        // Check if the password is at least 8 characters long
        if (newPassword.length() < 8) {
            // Indicate that the password was not long enough
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be at least 8 characters long");
        }
        
        // Check if the password contains at least one uppercase letter
        if (!newPassword.matches(".*[A-Z].*")) {
            // Indicate that the password needs 1 uppercase letter
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 uppercase letter");
        }
        
        // Check if the password contains at least one lowercase letter
        if (!newPassword.matches(".*[a-z].*")) {
            // Indicate that the password needs 1 lowercase letter
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 lowercase letter");
        }
        
        // Check if the password contains at least one number
        if (!newPassword.matches(".*[0-9].*")) {
            // Indicate that the password needs 1 number
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 number");
        }
        
        // If all checks pass, then the password is valid
        // Now check that the user with the given username exists
        UserAccount person = userAccountRepo.findByUsername(username);
        if (person == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }

        // Now we can change their password
        String newHashedPassword = hashPassword(newPassword);
        person.setPassword(newHashedPassword);
        person = userAccountRepo.save(person);
    }

    // Method to allow guests to create an account
    @Transactional
    public Customer createCustomerAccount(String name, String username, String email, String password, String phoneNumber) {
        // Validate that the username is unique
        UserAccount usernameCheck = userAccountRepo.findByUsername(username);
        if (usernameCheck != null) {
            // Indicate that there is already an account with this username
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An account already exists with this username, please select a different username");
        }
        
        // Validate that the email is unique
        UserAccount emailCheck = userAccountRepo.findByEmail(email);
        if (emailCheck != null) {
            // Indicate that there is already an account with this email
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An account already exists with this email, please enter a different email");
        }

        // Validate the phone number
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";
        if (phoneNumber == null || !phoneNumber.matches(regex)) {
            // Indicate that the phone number is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is invalid, please enter a valid phone number: xxx-xxx-xxxx");
        }

        // Validate the email
        // Regex to check that there is text before and after the `@` symbol
        String emailRegex = "^[^@\\s]+@[^@\\s]+$";
        if (!email.matches(emailRegex)) {
            // Indicate that the email is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is invalid, please enter a valid email with an @ symbol");
        }
        

        // Validate the password (MANY STEPS)
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            // Indicate that the password was not long enough
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be at least 8 characters long");
        }
        
        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            // Indicate that the password needs 1 uppercase letter
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 uppercase letter");
        }
        
        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            // Indicate that the password needs 1 lowercase letter
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 lowercase letter");
        }
        
        // Check if the password contains at least one number
        if (!password.matches(".*[0-9].*")) {
            // Indicate that the password needs 1 number
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least 1 number");
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
    public String loginToAccount(String username, String password) {
        // Check that account with input username exists
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }

        // Check that the password matches with the encrypted account password
        String hashedPassword = hashPassword(password);
        if (hashedPassword != user.getPassword()) {
            // Indicate that the passwords do not match
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match the account with the given username");
        }

        // Make the 'currently logged in' account this one somehow... issue for the frontend :)

        return username;
    }


    // Method to encrypt a password
    @Transactional
    public String hashPassword(String password) {
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
    public boolean changePaymentInfo(String username, String cardNumber, String billingName, Date expiryDate, int cvvCode, Address billingAddress) {
        // Create a new PaymentInfo object if they don't have one yet
        UserAccount user = userAccountRepo.findByUsername(username);
        
        if (user == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }
        // Ensures that the given user is a customer
        if (!hasPermission(username, 1)) {
            // Indicate that the user is not a customer
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is not a Customer, and therefore does not have payment information");
        }

        // Typecasts to a customer and checks if they have a PaymentInfo or not.
        // If they don't then create one with the given information
        Customer customer = (Customer) user;
        PaymentInformation paymentInfo = customer.getPaymentInformation();

        // Validates input payment information
        boolean valid = validatePaymentInfo(cardNumber, expiryDate, cvvCode, billingAddress, billingName);

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
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A customer with given username not found");
        }

        PaymentInformation paymentInfo = customer.getPaymentInformation();

        if (paymentInfo == null) {
            // Indicate that there is no payment info associated with this customer
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This customer has not set any payment information");
        }

        return paymentInfo;
    }


    // Method to get paymentinfo id from customer username
    @Transactional
    public int getPaymentInfoIdByUsername(String username) {
        Customer customer = customerRepo.findByUsername(username);

        if (customer == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A customer with given username not found");
        }

        PaymentInformation paymentInfo = customer.getPaymentInformation();

        if (paymentInfo == null) {
            // Indicate that there is no payment info associated with this customer
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This customer has not set any payment information");
        }

        return paymentInfo.getPaymentInfoID();
    }

    // Method to get an address given an address id
    @Transactional
    public Address getAddressById(int addressId) {
        Address address = addressRepo.findByAddressID(addressId);

        if (address == null) {
            // Indicate that there is no such address with the given id
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no address with the given id");
        }

        return address;
    }

    // Method to validate payment into
    @Transactional
    public boolean validatePaymentInfo(String cardNumber, Date expiryDate, int cvvCode, Address billingAddress, String billingName) {
        // Validate given information
        // Check the card number length
        if (cardNumber.length() != 16)

        {
            // Indicate that the length of the card number is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The customer does not have a valid credit card number");
        }

        // Ensure that they give a 3 digit CVV code
        if (Integer.toString(cvvCode).length() != 3)
        {
            // Indicate that the length of the cvv code is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The customer does not have a valid cvv code");
        }

        // Ensure that the card is not expired
        Date currentDate = new Date(System.currentTimeMillis());
        if (expiryDate.before(currentDate))
        {
            // Indicate that the card is expired
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The credit card with the following information is expired");
        }

        // Ensure that they enter a cardholder name
        if (billingName == null) {
            // Indicate that a billing name is missing
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a cardholder name");
        }

        return true;
    }

    @Transactional
    public boolean addAddressToUser(String username, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        // Validate that the user exists
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }
        // Ensures that the given user is a customer
        if (!hasPermission(username, 1)) {
            // Indicate that the user is not a customer
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is not a Customer, so you cannot add an address");
        }
        // Validate the address fields
        if (street == null || street.trim().isEmpty()) {
            // Indicate that the street must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a street name");
        }
        String regex = "^[A-Za-z]\\d[A-Za-z] ?\\d[A-Za-z]\\d$";
        if (!postalCode.matches(regex)) {
            // Indicate that the postal code must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid postal code: A#A#A#");
        }
        if (number <= 0) {
            // Indicate that the house number must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid house number");
        }
        if (city == null || city.trim().isEmpty()) {
            // Indicate that the city must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a city");
        }
        if (stateOrProvince == null || stateOrProvince.trim().isEmpty()) {
            // Indicate that the state/province must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a state/province");
        }
        if (country == null || country.trim().isEmpty()) {
            // Indicate that country must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a country");
        }
        if (apartmentNo < 0) {
            // Indicate that the apartment number must be greater than 0 or null
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid apartment number");
        }

        // Create and save the new address
        Address newAddress = new Address(street, postalCode, number, city, stateOrProvince, country, apartmentNo != null ? apartmentNo : 0);
        
        addressRepo.save(newAddress); // Persist the address in the database

        // Associate the address with the customer
        Customer customer = (Customer) user;
        customer.setAddress(newAddress);
        customerRepo.save(customer); // Save the updated customer with the new address
        return true;
    }
    

    @Transactional
    public boolean updateAddressForUser(String username, int addressId, String street, String postalCode, int number, String city, String stateOrProvince, String country, Integer apartmentNo) {
        // Validate that the user exists
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
           // Indicate that the user does not exist
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }
        // Ensures that the given user is a customer
        if (!hasPermission(username, 1)) {
            // Indicate that the user is not a customer
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is not a Customer, and therefore does not have an address to update");
        }

        // Retrieve the address and validate it exists
        Address address = addressRepo.findByAddressID(addressId);
        if (address == null) {
            // Indicate that no such address exists
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No address exists with the given address ID");
        }

        // Validate the address fields
        if (street == null || street.trim().isEmpty()) {
            // Indicate that the street must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a street name");
        }
        String regex = "^[A-Za-z]\\d[A-Za-z] ?\\d[A-Za-z]\\d$";
        if (!postalCode.matches(regex)) {
            // Indicate that the postal code must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid postal code: A#A#A#");
        }
        if (number <= 0) {
            // Indicate that the house number must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid house number");
        }
        if (city == null || city.trim().isEmpty()) {
            // Indicate that the city must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a city");
        }
        if (stateOrProvince == null || stateOrProvince.trim().isEmpty()) {
            // Indicate that the state/province must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a state/province");
        }
        if (country == null || country.trim().isEmpty()) {
            // Indicate that country must be entered
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a country");
        }
        if (apartmentNo < 0) {
            // Indicate that the apartment number must be greater than 0 or null
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter a valid apartment number");
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
        return true;
    }



    @Transactional
    public Address getAddressForUser(String username) {
        // Retrieve the user by their username
        UserAccount user = userAccountRepo.findByUsername(username);
        if (user == null) {
            // Indicate that the user does not exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given username not found");
        }

        // Ensures that the given user is a customer
        if (!hasPermission(username, 1)) {
            // Indicate that the user is not a customer
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is not a Customer, and therefore does not have an address to get");
        }

        // Retrieve the customer's address
        Customer customer = (Customer) user;
        Address address = customer.getAddress(); //Need to add function to model
        if (address == null) {
            // Indicate that there is no such address with the given id
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The customer does not have an address on their account");
        }

        return address;
    }

}
