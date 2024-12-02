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
                <input type="text" name="street" id="street" placeholder="" required v-model="deliveryInfo.address.street">
            </div>
            <div class="input">
                <label for="number">Street No.</label>
                <input type="number" name="number" id="number" placeholder="" step="1" required v-model="deliveryInfo.address.number">
            </div>
            <div class="input">
                <label for="apartment-number">Apartment No.</label>
                <input type="number" name="apartment-number" id="apartment-number" step="1" placeholder="" v-model="deliveryInfo.address.apartmentNo">
            </div>
            <div class="input">
                <label for="city">City</label>
                <input type="text" name="city" id="city" placeholder="" required v-model="deliveryInfo.address.city">
            </div>
            <div class="input">
                <label for="state-province">State/Province</label>
                <input type="text" name="state-province" id="state-province" placeholder="" required v-model="deliveryInfo.address.stateOrProvince">
            </div>
            <div class="input">
                <label for="country">Country</label>
                <input type="text" name="country" id="country" placeholder="" required v-model="deliveryInfo.address.country">
            </div>
            <div class="input">
                <label for="postal">Postal Code</label>
                <input type="text" name="postal" id="postal" placeholder="A1A1A1" required v-model="deliveryInfo.address.postalCode">
            </div>
            <button class="button" @click="saveDeliveryInfo" v-bind:disabled="!isAddressInputValid()">Update Address</button>
        </form>
        </div>
        <div class="payment-info">
        <p class="title">Payment Information</p>
        <form class="form">
            <div class="input">
                <label for="name">Cardholder Name</label>
                <input type="text" name="name" id="name" placeholder="" required v-model="paymentInfo.billingName">
            </div>
            <div class="input">
                <label for="card-number">Card Number</label>
                <input type="text" name="card-number" id="card-number" placeholder="XXXXXXXXXXXXXXXX" required v-model="paymentInfo.cardNumber">
            </div>
            <div class="input">
                <label for="expiry-date">Expiry Date</label>
                <input type="date" name="expiry-date" id="expiry-date" placeholder="" required v-model="paymentInfo.expiryDate">
            </div>

            <div class="input">
                <label for="cvv">CVV Code</label>
                <input type="number" name="cvv" id="cvv" placeholder="XXX" required v-model="paymentInfo.cvvCode">
            </div>
            <button class="button" @click="savePaymentInfo" v-bind:disabled="!isPaymentInputValid()">Update Payment Information</button>
        </form>
        </div>
    </div>

    <div v-if="activeTab === 'orders'" class="active" id="orders">
        <h1 class="title">My Order History</h1>

        <!-- Check if the cart is empty -->
        <div v-if="orders.length > 0">
        <!-- Existing cart items -->
        <div class="cart-items">
            <div v-for="(order, index) in orders" :key="index" class="order-hist-item">
            <div class="order-details">
                <span class="order-date">Order Placed On: {{ order.purchaseDate }}</span>
                <span class="order-price">Order Price: ${{ order.totalPrice.toFixed(2) }}</span>
            </div>
            <div class="game-copies">
                    <div v-for="(gameCopy, gameIndex) in order.gameCopies" :key="gameIndex" class="game-copy">
                    <span class="game-title">{{ gameCopy.game.title }}</span>
                    <span class="game-price"> ---- ${{ gameCopy.game.price.toFixed(2) }}</span>
                    </div>
            </div>
            <div class="order-return">
                <button class="return-button" :disabled="order.isReturned" @click="returnOrder(order.orderId)">
                    {{ order.isReturned ? "Order Returned" : "Return Order" }}
                </button>
            </div>
            </div>
            </div>
        </div>

        <!-- Display this when the cart is empty -->
        <div v-else class="empty-orders">
        <p>
            You currently have no orders.
            <router-link to="/browse">Browse games</router-link>
            to fill your cart.
        </p>
        </div>
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
      orders: [],
      username: null,
      name: null,
      email: null,
      phoneNumber: null,
      paymentInfoId: null,
      deliveryInfoExists: false,
      paymentInfoExists: false,
      paymentInfo: {
        billingName: null,
        cardNumber: null,
        expiryDate: null,
        cvvCode: null,
        addressId: null,
        billingAddress: {
          street: null,
          number: null,
          apartmentNo: null,
          city: null,
          stateOrProvince: null,
          country: null,
          postalCode: null,
        },
      },
      deliveryInfo: {
          address: {
          street: null,
          number: null,
          apartmentNo: null,
          city: null,
          stateOrProvince: null,
          country: null,
          postalCode: null,
        },
      },

    };
  },
  computed: {
          LOGGEDINUSERNAME() {
          return sessionStorage.getItem('loggedInUsername');
          },
          PERMISSIONLEVEL() {
          return sessionStorage.getItem('permissionLevel');
          }
        },
  methods: {
    async fetchProfileData() {
        
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

    async fetchPaymentInfo() {
      try {
        const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/paymentInfo`);
        const paymentData = response.data;

        // Store the paymentInfoId
        this.paymentInfoId = paymentData.paymentInfoID;
        // Map the backend data to paymentInfo object
        this.paymentInfo = {
          billingName: paymentData.billingName,
          cardNumber: paymentData.cardNumber,
          expiryDate: paymentData.expiryDate,
          cvvCode: paymentData.cvvCode,
          addressId: paymentData.billingAddress.addressID,
          billingAddress: {
            street: paymentData.billingAddress.street,
            number: paymentData.billingAddress.number,
            apartmentNo: paymentData.billingAddress.apartmentNo,
            city: paymentData.billingAddress.city,
            stateOrProvince: paymentData.billingAddress.stateOrProvince,
            country: paymentData.billingAddress.country,
            postalCode: paymentData.billingAddress.postalCode,
          },
        };

        this.paymentInfoExists = true;
      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.paymentInfoExists = false;
          console.log('No existing payment information.');
        } else {
          console.error('Error fetching payment information:', error);
          // Handle other errors appropriately
        }
      }
    },

    async fetchDeliveryInfo() {
      try {
        const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/address?loggedInUsername=${this.LOGGEDINUSERNAME}`);
        const deliveryData = response.data;
        // Store the deliveryInfoId
        this.deliveryInfoId = deliveryData.addressID;
        // Map the backend data to paymentInfo object
        this.deliveryInfo.address = {
            street: deliveryData.street,
            number: deliveryData.number,
            apartmentNo: deliveryData.apartmentNo,
            city: deliveryData.city,
            stateOrProvince: deliveryData.stateOrProvince,
            country: deliveryData.country,
            postalCode: deliveryData.postalCode,
        };

        this.deliveryInfoExists = true;
      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.deliveryInfoExists = false;
          console.log('No existing delivery information.');
        } else {
          console.error('Error fetching delivery information:', error);
          // Handle other errors appropriately
        }
      }
    },

    async fetchOrderData() {
        try {
          const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/orders?loggedInUsername=${this.LOGGEDINUSERNAME}`);
          const orderData = response.data.orders;
          console.log(response.data);
          console.log(orderData);
          console.log(orderData.length);
          this.orders = orderData;

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

    async saveDeliveryInfo() {
        event.preventDefault();  // Prevent the form from submitting and updating the URL
        try {
          const urlBase = `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/address`;
          if (this.deliveryInfoExists) {
            console.log("putting")
            // Update existing delivery information
            const response = await axios.put(`${urlBase}/${this.deliveryInfoId}?loggedInUsername=${this.LOGGEDINUSERNAME}`, this.deliveryInfo.address);
            this.paymentInfo.addressId = response.data.addressID;

            console.log(response.data.addressID);
            console.log(this.paymentInfo.addressId);
            this.$swal({
                title: 'Success',
                text: 'Address updated successfully.',
                icon: 'success',
              });
          } else {
            console.log("posting")
            // Add new delivery information
            const response = await axios.post(`${urlBase}?loggedInUsername=${this.LOGGEDINUSERNAME}`, this.deliveryInfo.address);

            // Set deliveryInfoExists to true and store the new deliveryInfoId
            this.deliveryInfoExists = true;
            this.deliveryInfoId = response.data.addressID;
            this.paymentInfo.addressId = response.data.addressID;
            console.log("address id: " + this.paymentInfo.addressId)
            this.$swal({
                title: 'Success',
                text: 'Address saved successfully.',
                icon: 'success',
              });
          }
        } catch (error) {
            console.log("error")
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
        event.preventDefault();  // Prevent the form from submitting and updating the URL

        try {
            console.log(this.paymentInfo.addressId);
            const urlBase = `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/paymentInfo`;
            const paymentInfoToSend = { ...this.paymentInfo };
            if (this.paymentInfoExists) {
                console.log("putting")
                // Update existing payment information
                const response = await axios.put(`${urlBase}/${this.paymentInfoId}`, paymentInfoToSend);
                // Set paymentInfoExists to true and store the new paymentInfoId
                this.paymentInfoId = response.data.id;
                this.$swal({
                title: 'Success',
                text: 'Payment information updated successfully.',
                icon: 'success',
              });
            } else {
                console.log("posting")
                // Add new payment information
                const response = await axios.post(urlBase, paymentInfoToSend);
                // Set paymentInfoExists to true and store the new paymentInfoId
                this.paymentInfoExists = true;
                this.paymentInfoId = response.data.id;
                this.$swal({
                title: 'Success',
                text: 'Payment information saved successfully.',
                icon: 'success',
              });
            }


            }
           catch (error) {
            console.log("error")
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

        async returnOrder(orderId) {
            const urlBase = `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/orders/${orderId}`;
            const today = new Date();
            const request = {
                returnDate: today,
                customerUsername: this.LOGGEDINUSERNAME
            }
            try {
                const response = await axiosClient.put(urlBase, request, {
                    params: { loggedInUsername: sessionStorage.getItem("loggedInUsername") }   // Add the query parameter
                });
                
                const order = this.orders.find(o => o.orderId === orderId);
                if (order) {
                    order.isReturned = true;
                }
                this.$swal({
                title: 'Success',
                text: 'Order returned successfully. You will receive a refund in 2-5 business days.',
                icon: 'success',
                });
                
                console.log("returned order yay");
            } catch {
                console.log("error")
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

            isPaymentInputValid() {
            return this.paymentInfo.addressId !== 0   
            && this.paymentInfo.cardNumber
            && this.paymentInfo.cvvCode
            && this.paymentInfo.expiryDate
            && this.paymentInfo.billingName
            },

            isAddressInputValid() {
            return this.deliveryInfo.address.apartmentNo   
            && this.deliveryInfo.address.city
            && this.deliveryInfo.address.country
            && this.deliveryInfo.address.number
            && this.deliveryInfo.address.postalCode
            && this.deliveryInfo.address.stateOrProvince
            && this.deliveryInfo.address.street
            }

  },

  mounted() {
    this.fetchProfileData(); // Populate the profile page
    this.fetchDeliveryInfo();
    this.fetchPaymentInfo();
    this.fetchOrderData();
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
  border-radius: 10px;
}

.return-button {
  margin-top: 10px;
  display: block;
  width: 20%;
  background-color: rgba(167, 139, 250, 1);
  padding: 0.75rem;
  text-align: center;
  color: rgba(17, 24, 39, 1);
  border: none;
  font-weight: 600;
  cursor: pointer;
  justify-self: end;
  border-radius: 10px;
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

.game-copies {
    text-align: right;
    margin-right: 275px;
    font-size: 1.0rem;
}

.game-title {
    color: rgba(167, 139, 250, 1);
}

.game-price {
    color: rgba(78, 191, 109, 1);
}

.profile-field {
    flex: 1;
    text-align: left;
    font-size: 1.25rem;
    color: rgba(78, 191, 109, 1);
}

.empty-orders {
    text-align: center;
}

.order-hist-item {
    margin-top: 15px;
    margin-left: 400px;
    margin-right: 400px;
    padding: 10px;
    border: 1px solid #ccc;
    padding-bottom: 20px;
    border-radius: 10px;
}

.order-details {
    display: flex;
    justify-content: space-between;
}

.order-date {
    margin-left: 20px;
    font-size: 1.25rem;
    color: white;
    text-align: left;
}

.order-price {
    margin-right: 20px;
    font-size: 1.25rem;
    color: white;
    text-align: right;
}

.button:disabled {
    background-color: rgba(75, 85, 99, 1);
    color: rgba(243, 244, 246, 0.5);
    cursor: not-allowed;
}

.return-button:disabled {
    background-color: rgba(75, 85, 99, 1);
    color: rgba(243, 244, 246, 0.5);
    cursor: not-allowed;
}
</style>