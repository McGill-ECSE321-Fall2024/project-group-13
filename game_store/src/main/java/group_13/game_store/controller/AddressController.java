package group_13.game_store.controller;

import group_13.game_store.dto.AddressRequestDto;
import group_13.game_store.dto.AddressResponseDto;
import group_13.game_store.service.AccountService;
import group_13.game_store.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/customers/{customerId}")
public class AddressController {

    @Autowired
    AccountService accountService;

    /**
     * Retrieves the address for a given customer.
     *
     * @param customerId The ID of the customer.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @return The address details for the customer.
     */
    @GetMapping("/address")
    public AddressResponseDto getAddress(
        @PathVariable("customerId") String customerId,
        @RequestParam String loggedInUsername
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Address address = accountService.getAddressForUser(customerId);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found for the user.");
        }

        return new AddressResponseDto(
            address.getAddressID(),
            address.getStreet(),
            address.getPostalCode(),
            address.getNumber(),
            address.getCity(),
            address.getStateOrProvince(),
            address.getCountry(),
            address.getApartmentNo()
        );
    }

    /**
     * Adds a new address for a given customer.
     *
     * @param customerId The ID of the customer.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @param request The address request details.
     * @return The added address details.
     */
    @PostMapping("/address")
    public AddressResponseDto addAddress(
        @PathVariable("customerId") String customerId,
        @RequestParam String loggedInUsername,
        @RequestBody AddressRequestDto request
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        boolean success = accountService.addAddressToUser(
            customerId,
            request.getStreet(),
            request.getPostalCode(),
            request.getNumber(),
            request.getCity(),
            request.getStateOrProvince(),
            request.getCountry(),
            request.getApartmentNo()
        );
        
        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add address.");
        }

        Address address = accountService.getAddressForUser(customerId);
        return new AddressResponseDto(
            address.getAddressID(),
            address.getStreet(),
            address.getPostalCode(),
            address.getNumber(),
            address.getCity(),
            address.getStateOrProvince(),
            address.getCountry(),
            address.getApartmentNo()
        );
    }

    /**
     * Updates an existing address for a given customer.
     *
     * @param customerId The ID of the customer.
     * @param addressID The ID of the address to update.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @param request The address request details.
     * @return The updated address details.
     */
    @PutMapping("/address/{addressID}")
    public AddressResponseDto updateAddress(
        @PathVariable("customerId") String customerId,
        @PathVariable int addressID,
        @RequestParam String loggedInUsername,
        @RequestBody AddressRequestDto request
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        boolean success = accountService.updateAddressForUser(
            customerId,
            addressID,
            request.getStreet(),
            request.getPostalCode(),
            request.getNumber(),
            request.getCity(),
            request.getStateOrProvince(),
            request.getCountry(),
            request.getApartmentNo()
        );
        
        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update address.");
        }

        Address updatedAddress = accountService.getAddressForUser(customerId);
        return new AddressResponseDto(
            updatedAddress.getAddressID(),
            updatedAddress.getStreet(),
            updatedAddress.getPostalCode(),
            updatedAddress.getNumber(),
            updatedAddress.getCity(),
            updatedAddress.getStateOrProvince(),
            updatedAddress.getCountry(),
            updatedAddress.getApartmentNo()
        );
    }

    /**
     * Retrieves the billing address for a customer's payment information.
     *
     * @param customerId The ID of the customer.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @return The billing address details for the customer.
     */
    @GetMapping("/paymentinfo/address")
    public AddressResponseDto getBillingAddress(
        @PathVariable("customerId") String customerId,
        @RequestParam String loggedInUsername
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Address address = accountService.getAddressForUser(customerId);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Billing address not found for the user.");
        }

        return new AddressResponseDto(
            address.getAddressID(),
            address.getStreet(),
            address.getPostalCode(),
            address.getNumber(),
            address.getCity(),
            address.getStateOrProvince(),
            address.getCountry(),
            address.getApartmentNo()
        );
    }

    /**
     * Adds a new billing address for a customer's payment information.
     *
     * @param customerId The ID of the customer.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @param request The address request details.
     * @return The added billing address details.
     */
    @PostMapping("/paymentinfo/address")
    public AddressResponseDto addBillingAddress(
        @PathVariable("customerId") String customerId,
        @RequestParam String loggedInUsername,
        @RequestBody AddressRequestDto request
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        boolean success = accountService.addAddressToUser(
            customerId,
            request.getStreet(),
            request.getPostalCode(),
            request.getNumber(),
            request.getCity(),
            request.getStateOrProvince(),
            request.getCountry(),
            request.getApartmentNo()
        );
        
        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add billing address.");
        }

        Address address = accountService.getAddressForUser(customerId);
        return new AddressResponseDto(
            address.getAddressID(),
            address.getStreet(),
            address.getPostalCode(),
            address.getNumber(),
            address.getCity(),
            address.getStateOrProvince(),
            address.getCountry(),
            address.getApartmentNo()
        );
    }

    /**
     * Updates an existing billing address for a customer's payment information.
     *
     * @param customerId The ID of the customer.
     * @param addressID The ID of the billing address to update.
     * @param loggedInUsername The username of the logged-in user, used for security verification.
     * @param request The address request details.
     * @return The updated billing address details.
     */
    @PutMapping("/paymentinfo/address/{addressID}")
    public AddressResponseDto updateBillingAddress(
        @PathVariable("customerId") String customerId,
        @PathVariable int addressID,
        @RequestParam String loggedInUsername,
        @RequestBody AddressRequestDto request
    ) {
        // Security check to verify logged-in user
        if (!loggedInUsername.equals(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        boolean success = accountService.updateAddressForUser(
            customerId,
            addressID,
            request.getStreet(),
            request.getPostalCode(),
            request.getNumber(),
            request.getCity(),
            request.getStateOrProvince(),
            request.getCountry(),
            request.getApartmentNo()
        );
        
        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update billing address.");
        }

        Address updatedAddress = accountService.getAddressForUser(customerId);
        return new AddressResponseDto(
            updatedAddress.getAddressID(),
            updatedAddress.getStreet(),
            updatedAddress.getPostalCode(),
            updatedAddress.getNumber(),
            updatedAddress.getCity(),
            updatedAddress.getStateOrProvince(),
            updatedAddress.getCountry(),
            updatedAddress.getApartmentNo()
        );
    }

}
