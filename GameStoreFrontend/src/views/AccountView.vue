<template>
    
    <div class="tabs">
        <button 
            class="tab" 
            :class="{ active: activeTab === 'profile' }"
            @click="activeTab = 'profile'">
            User Profile</button>
        <button 
            class="tab"
            :class="{ active: activeTab === 'update' }"
            @click="activeTab = 'update'"
        >
        Update Information </button>
        <button
            class="tab"
            :class="{ active: activeTab === 'orders' }"
            @click="activeTab = 'orders'"
        >
        Order History </button>
    </div>

    <div v-if="activeTab === 'profile'" class="active" id="profile">
        <div class="profile-info">
        <p class="title profile-title">User Profile</p>
            <div class="info">
                <label for="name">Name:</label>
                <span class="profile-field">{{ name }}</span>
            </div>
            <div class="info">
                <label for="card-number">Username:</label>
                <span class="profile-field">{{ username }}</span>
            </div>
            <div class="info">
                <label for="expiry-date">Email:</label>
                <span class="profile-field">{{ email }}</span>
            </div>
            <div class="info">
                <label for="cvv">Phone Number:</label>
                <span class="profile-field">{{ phoneNumber }}</span>
            </div>
        </div>
    </div>

    <div v-if="activeTab === 'update'" class="active" id="update">
        <div class="address">
        <p class="title">Address</p>
        <form class="form">
            <div class="input">
                <label for="street">Street</label>
                <input type="text" name="street" id="street" placeholder="" required v-model="street">
            </div>
            <div class="input">
                <label for="number">Street No.</label>
                <input type="number" name="number" id="number" placeholder="" step="1" required v-model="addressNumber">
            </div>
            <div class="input">
                <label for="apartment-number">Apartment No.</label>
                <input type="number" name="apartment-number" id="apartment-number" step="1" placeholder="" v-model="apartmentNumber">
            </div>
            <div class="input">
                <label for="city">City</label>
                <input type="text" name="city" id="city" placeholder="" required v-model="city">
            </div>
            <div class="input">
                <label for="state-province">State/Province</label>
                <input type="text" name="state-province" id="state-province" placeholder="" required v-model="stateProvince">
            </div>
            <div class="input">
                <label for="country">Country</label>
                <input type="text" name="country" id="country" placeholder="" required v-model="country">
            </div>
            <div class="input">
                <label for="postal">Postal Code</label>
                <input type="text" name="postal" id="postal" placeholder="" required v-model="postalCode">
            </div>
            <button class="button">Update Address</button>
        </form>
        </div>
        <div class="payment-info">
        <p class="title">Payment Information</p>
        <form class="form">
            <div class="input">
                <label for="name">Cardholder Name</label>
                <input type="text" name="name" id="name" placeholder="" required v-model="billingName">
            </div>
            <div class="input">
                <label for="card-number">Card Number</label>
                <input type="text" name="card-number" id="card-number" placeholder="" required v-model="creditCardNumber">
            </div>
            <div class="input">
                <label for="expiry-date">Expiry Date</label>
                <input type="date" name="expiry-date" id="expiry-date" placeholder="" required v-model="expiryDate">
            </div>

            <div class="input">
                <label for="cvv">CVV Code</label>
                <input type="number" name="cvv" id="cvv" placeholder="" required v-model="cvv">
            </div>
            <button class="button">Update Payment Information</button>
        </form>
        </div>
    </div>

    <div v-if="activeTab === 'orders'" class="active" id="orders">
        <h2>Order History</h2>
        <p>Here is the order history content.</p>
    </div>
</template>


<script>
import axios from "axios";

const axiosClient = axios.create({
	// NOTE: it's baseURL, not baseUrl
	baseURL: "http://localhost:8080"
});

  export default {
  name: "AccountView",
  data() {
    return {
      activeTab: "profile", // Default tab
      username: null,
      name: null,
      email: null,
      phoneNumber: null,
      addressId: null,
      paymentInfoId: null,
      paymentInfo: {
        billingName: null,
        creditCardNumber: null,
        expiryDate: null,
        cvv: null,
        billingAddress: null,
      },
      address: {
        street: null,
        addressNumber: null,
        apartmentNumber: null,
        city: null,
        provinceState: null,
        country: null,
        postalCode: null,
      }

    };
  },

  methods: {
    async fetchData() {
        
        try {
            const loggedInUsername = sessionStorage.getItem("loggedInUsername");
            const endpoint = ("/customers/" + loggedInUsername)
            const response = await axiosClient.get(endpoint, {
                params: { loggedInUsername: sessionStorage.getItem("loggedInUsername") }   // Add the query parameter
            });

            this.username = response.data.username;
            this.name = response.data.name;
            this.email = response.data.email;
            this.phoneNumber = response.data.phoneNumber;
        }
        catch (error) {
            console.log("hey")
            // Check if the error is a server response with a status code
            if (error.response) {
                const status = error.response.status;
                const message = error.response.data?.message || "An error occurred.";
                
                // Display user-friendly messages based on status codes or backend message
                if (status === 400 || status === 404) {
                    this.errorMessage = message; // Example: Invalid credentials
                    console.log(message);
                } else if (status === 403) {
                    this.errorMessage = "Access denied. Please contact support.";
                } else {
                    this.errorMessage = "An unexpected error occurred.";
                }
            } else {
                // Network or unexpected error
                console.error(error);
                this.errorMessage = "Unable to connect to the server.";
            }
        }
    },

    async saveDeliveryInfo() {
        try {
            const loggedInUsername = sessionStorage.getItem("loggedInUsername");
            const endpoint = "/customers/" + loggedInUsername + "/address";
            // Update existing delivery information
            const response = await axiosClient.put(endpoint, this.paymentInfo, {
                params: { loggedInUsername: sessionStorage.getItem("loggedInUsername") }   // Add the query parameter
            });
            this.paymentInfo.addressId = response.data.addressID;

            console.log(response.data.addressID);
            this.paymentInfo.billingAddress = this.address
            console.log(this.paymentInfo.addressId);
        } catch (error) {
            console.log("hey")
            // Check if the error is a server response with a status code
            if (error.response) {
                const status = error.response.status;
                const message = error.response.data?.message || "An error occurred.";
                
                // Display user-friendly messages based on status codes or backend message
                if (status === 400 || status === 404) {
                    this.errorMessage = message; // Example: Invalid credentials
                    console.log(message);
                } else if (status === 403) {
                    this.errorMessage = "Access denied. Please contact support.";
                } else {
                    this.errorMessage = "An unexpected error occurred.";
                }
            } else {
                // Network or unexpected error
                console.error(error);
                this.errorMessage = "Unable to connect to the server.";
            }
        }
    },

    async savePaymentInfo() {
        if (!this.paymentInfo.addressId) {
          this.$swal({
            title: 'Error',
            text: 'Please set address before adding payment information.',
            icon: 'error',
          });
          return;
        }
        if (this.validatePaymentInfo()) {
          try {
            console.log(this.paymentInfo.addressId);
            const urlBase = `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/paymentInfo`;
            const paymentInfoToSend = { ...this.paymentInfo };
            paymentInfoToSend.expiryDate = this.convertExpiryDateToYYYYMMDD(this.paymentInfo.expiryDate);
            if (this.paymentInfoExists) {
              // Update existing payment information
              const response = await axios.put(`${urlBase}/${this.paymentInfoId}`, paymentInfoToSend);
              this.$swal({
                title: 'Success',
                text: 'Payment information updated successfully.',
                icon: 'success',
              });
            } else {
              // Add new payment information
              const response = await axios.post(urlBase, paymentInfoToSend);
              this.$swal({
                title: 'Success',
                text: 'Payment information saved successfully.',
                icon: 'success',
              });

              // Set paymentInfoExists to true and store the new paymentInfoId
              this.paymentInfoExists = true;
              this.paymentInfoId = response.data.id;
            }
          } catch (error) {
            console.error('Error saving payment information:', error);
            this.$swal({
              title: 'Error',
              text: 'Error saving new payment information.',
              icon: 'error',
            });
          }
        }
    },
  },

  mounted() {
    this.fetchData(); // Populate the profile page
  },
};


</script>

<style scoped>
.payment-info, .address, .profile-info{
  width: 500px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 0.75rem;
  background-color: #1e1e1e;
  padding: 2rem;
  color: rgba(243, 244, 246, 1);
}

.address {
    margin-top: 20px;
}

.payment-info {
    margin-top: 20px;
}

.title {
  text-align: center;
  font-size: 1.5rem;
  line-height: 2rem;
  font-weight: 700;
  color: rgba(243, 244, 246, 1);
}

.profile-title {
    padding-bottom: 20px;
    font-size: 1.5rem;
}

.form {
  margin-top: 1.5rem;
}

.input {
  margin-top: 0.25rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.input label {
  display: block;
  color: rgba(156, 163, 175, 1);
  margin-bottom: 4px;
  margin-top: 20px;
}

.input input {
  width: 100%;
  border-radius: 0.375rem;
  border: 1px solid rgba(55, 65, 81, 1);
  outline: 0;
  background-color: #1e1e1e;
  padding: 0.75rem 1rem;
  color: rgba(243, 244, 246, 1);
}

.input input:focus {
  border-color: rgba(167, 139, 250);
}

.signup a {
  color: rgba(243, 244, 246, 1);
  text-decoration: none;
  font-size: 14px;
}

.signup a:hover {
  text-decoration: underline rgba(167, 139, 250, 1);
}

.button {
  margin-top: 10px;
  display: block;
  width: 100%;
  background-color: rgba(167, 139, 250, 1);
  padding: 0.75rem;
  text-align: center;
  color: rgba(17, 24, 39, 1);
  border: none;
  font-weight: 600;
  cursor: pointer;
}

.sign-in:hover {
  text-decoration: underline rgba(55, 65, 81, 1);
}

body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #1e1e1e;
}

.tabs {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
    margin-top: 100px;
    margin-left: auto;
    margin-right: auto;
    width: 500px;
    background-color: #1e1e1e;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.tab {
    margin-left: auto;
    margin-right: auto;
    padding: 1rem 2rem;
    cursor: pointer;
    border: none;
    border-radius: 10px;
    background-color: #1e1e1e;
    color: rgba(243, 244, 246, 1);
    transition: background-color 0.3s;
    flex: 1;
    text-align: center;
}

.tab:last-child {
    border-right: none;
}

.tab:hover {
    background: rgba(55, 65, 81, 1);
}

.tab.active {
    background: rgba(55, 65, 81, 1);
    font-weight: bold;
}

.tab-content {
    display: none;
    width: 500px;
    text-align: center;
    margin-top: 1rem;
    background-color: #1e1e1e;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.tab-content.active {
    display: block;
}

.info {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.info label {
    flex: 0 0 150px;
    text-align: right;
    margin-right: 25px;
    font-size: 1.25rem;
    color: rgba(167, 139, 250, 1);
}

.profile-field {
    flex: 1;
    text-align: left;
    font-size: 1.25rem;
    color: rgb(78, 191, 109, 1);
}
</style>