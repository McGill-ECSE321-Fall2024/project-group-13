<template>
  <div class="checkout">
    <h1 class="title">Checkout</h1>
    <div class="order-items">
      <div v-for="(game, index) in games" :key="index" class="order-item">
        <img :src="resolveImagePath(game.img)" alt="Game Image" class="game-image" />
        <div class="game-details">
          <h2 class="game-title">{{ game.title }}</h2>
          <p class="game-description">{{ game.description }}</p>
          <p class="game-stock" :class="{ 'in-stock': game.stock > 0, 'out-of-stock': game.stock === 0 }">
            {{ game.stock > 0 ? 'In Stock' : 'Out of Stock' }}
          </p>
          <p class="game-description">{{ game.price.toFixed(2) }}</p>
          <div class="game-quantity">
            <span class="quantity-number">Quantity: {{ game.quantity }}</span>
          </div>
        </div>
    </div>

    <!-- Delivery Information -->
    <div class="delivery-info">
      <h2>Address</h2>
      <form>
        <!-- Address Number -->
        <div class="form-group">
          <label for="deliveryAddressNumber">Address Number</label>
          <input
            type="text"
            id="deliveryAddressNumber"
            v-model="deliveryInfo.number"
            required
          />
          <span v-if="deliveryErrors.number" class="error-message">{{ deliveryErrors.number }}</span>
        </div>

        <!-- Street -->
        <div class="form-group">
          <label for="deliveryStreet">Street</label>
          <input
            type="text"
            id="deliveryStreet"
            v-model="deliveryInfo.street"
            required
          />
          <span v-if="deliveryErrors.street" class="error-message">{{ deliveryErrors.street }}</span>
        </div>

        <div class="form-group">
          <label for="deliveryApartmentNumber">
            Apartment Number (Optional)
          </label>
          <input
            type="text"
            id="deliveryApartmentNumber"
            v-model="deliveryInfo.apartmentNo"
          />
        </div>
        <!-- City -->
        <div class="form-group">
          <label for="deliveryCity">City</label>
          <input
            type="text"
            id="deliveryCity"
            v-model="deliveryInfo.city"
            required
          />
          <span v-if="deliveryErrors.city" class="error-message">{{ deliveryErrors.city }}</span>
        </div>

        <!-- State/Province -->
        <div class="form-group">
          <label for="deliveryProvinceState">Province/State</label>
          <input
            type="text"
            id="deliveryProvinceState"
            v-model="deliveryInfo.stateOrProvince"
            required
          />
          <span v-if="deliveryErrors.stateOrProvince" class="error-message">{{ deliveryErrors.stateOrProvince }}</span>
        </div>

        <!-- Country -->
        <div class="form-group">
          <label for="deliveryCountry">Country</label>
          <input
            type="text"
            id="deliveryCountry"
            v-model="deliveryInfo.country"
            required
          />
          <span v-if="deliveryErrors.country" class="error-message">{{ deliveryErrors.country }}</span>
        </div>

        <!-- Postal Code -->
        <div class="form-group">
          <label for="deliveryPostalCode">Postal Code</label>
          <input
            type="text"
            id="deliveryPostalCode"
            v-model="deliveryInfo.postalCode"
            required
          />
          <span v-if="deliveryErrors.postalCode" class="error-message">{{ deliveryErrors.postalCode }}</span>
        </div>

        <!-- Save Button -->
        <div class="form-group">
          <button type="button" @click="saveDeliveryInfo">
            {{ deliveryInfoExists ? 'Update Delivery Information' : 'Save New Delivery Information' }}
          </button>
        </div>
      </form>
    </div>
     <!-- Payment Information -->
     <div class="payment-info">
      <h2>Payment Information</h2>
      <form>
        <!-- Billing Name -->
        <div class="form-group">
          <label for="billingName">Billing Name</label>
          <input
            type="text"
            id="billingName"
            v-model="paymentInfo.billingName"
            required
          />
        </div>

        <!-- Credit Card Number -->
        <div class="form-group">
          <label for="creditCardNumber">Credit Card Number</label>
          <input
            type="text"
            id="creditCardNumber"
            v-model="paymentInfo.cardNumber"
            required
          />
          <span v-if="paymentErrors.cardNumber" class="error-message">{{ paymentErrors.cardNumber }}</span>
        </div>

        <!-- Expiry Date -->
        <div class="form-group">
          <label for="expiryDate">Expiry Date</label>
          <input
            type="text"
            id="expiryDate"
            v-model="paymentInfo.expiryDate"
            placeholder="MM/YY"
            required
          />
          <span v-if="paymentErrors.expiryDate" class="error-message">{{ paymentErrors.expiryDate }}</span>
        </div>

        <!-- CVV -->
        <div class="form-group">
          <label for="cvv">CVV</label>
          <input type="text" id="cvv" v-model="paymentInfo.cvvCode" required />
          <span v-if="paymentErrors.cvvCode" class="error-message">{{ paymentErrors.cvvCode }}</span>
        </div>

        <div class="form-group">
          <button type="button" @click="savePaymentInfo">
             {{ paymentInfoExists ? 'Update Payment Information' : 'Save New Payment Information' }}
         </button>
        </div>
      </form>
    </div>

    <!-- Order Breakdown -->
    <div class="order-breakdown">
          <h2>Order Breakdown</h2>
          <div class="breakdown-item">
              <span>Subtotal: </span>
              <span>${{ subtotal.toFixed(2) }}</span>
          </div>
          <div class="breakdown-item" v-if="promotions > 0">
            <span>Promotions: </span>
            <span>-${{ promotions.toFixed(2) }}</span>
          </div>
          <div class="breakdown-item">
              <span>Delivery Fee: </span>
              <span>${{ deliveryFee.toFixed(2) }}</span>
          </div>
          <div class="breakdown-item">
              <span>QST (15%): </span>
              <span>${{ taxes.toFixed(2) }}</span>
          </div>
          <div class="breakdown-total">
              <span>Total: </span>
              <span>${{ total.toFixed(2) }}</span>
          </div>
          <div>
          <button class="place-order-button" type="button" @click="placeOrder">
             {{ 'Place Order' }}
         </button>
        </div>
          </div>
      </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "OrderView",
  data() {
    return {
      paymentErrors: {},
      deliveryErrors: {},
      deliveryInfoExists: false,
      paymentInfoExists: false,
      paymentInfoId: null,
      games: [],
      paymentInfo: {
        billingName: "",
        cardNumber: "",
        expiryDate: "",
        cvvCode: "",
        addressId: null,
        billingAddress: {
          street: "",
          number: "",
          apartmentNo: "",
          city: "",
          stateOrProvince: "",
          country: "",
          postalCode: "",
        },
      },
      deliveryInfo: {
          address: {
          street: "",
          number: "",
          apartmentNo: "",
          city: "",
          stateOrProvince: "",
          country: "",
          postalCode: "",
        },
      },
      subtotal: 0,
      deliveryFee: 5,
      taxRate: 0.15, // 15% QST
          };
      },
      created() {
          this.fetchCartData();
          this.fetchPaymentInfo();
          this.fetchDeliveryInfo();
      },
      computed: {
          LOGGEDINUSERNAME() {
          return sessionStorage.getItem('loggedInUsername');
          },
          PERMISSIONLEVEL() {
          return sessionStorage.getItem('permissionLevel');
          },
          taxes() {
          return this.subtotal * this.taxRate;
          },
          total() {
          return this.subtotal + this.deliveryFee + this.taxes - this.promotions;
          },
          promotions() {
            return this.games.reduce((sum, game) => {
              return sum + game.price * game.quantity * game.promotionPercentage * 0.01;
             }, 0);
          }
      },
  methods: {
    convertExpiryDateToMMYY(dateStr) {
      // dateStr is in 'YYYY-MM-DD'
      const date = new Date(dateStr);
      const month = ('0' + (date.getMonth() + 1)).slice(-2); // Months are zero-based
      const year = date.getFullYear().toString().slice(-2);   // Last two digits of the year
      return `${month}/${year}`;
    },

    convertExpiryDateToYYYYMMDD(expiryStr) {
      // expiryStr is in 'MM/YY'
      const [monthStr, yearStr] = expiryStr.split('/');
      const month = parseInt(monthStr, 10);
      const year = parseInt(yearStr, 10) + 2000; // Assuming years are in 2000s
      // Set day to the last day of the month
      const day = new Date(year, month, 0).getDate(); // Zero gets the last day of the previous month
      return `${year}-${('0' + month).slice(-2)}-${('0' + day).slice(-2)}`;
    },

    resolveImagePath(image) {
      try {
        // Resolve path using import.meta.URL
        return new URL(`../assets/${image}`, import.meta.url).href;
      } catch (error) {
        // Fail
        console.log("Error resolving image path: ", error);
        return '';
      }
    },
    useBillingAddress() {
      this.deliveryInfo.name = this.paymentInfo.billingName;
      this.deliveryInfo.address = { ...this.paymentInfo.billingAddress };
    },
    async fetchCartData() {
      try {
        const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart`);
        const cartData = response.data;

        // Map backend data to your games array
        this.games = cartData.games.map((game) => ({...game, quantity: game.quantity || 1,}));
        this.subtotal = cartData.subtotalPrice;
      } catch (error) {
        console.error('Error fetching cart data:', error);
        // Handle error appropriately
      }
    },
    async fetchPaymentInfo() {
      try {
        const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/paymentInfo`);
        const paymentData = response.data;

        // Store the paymentInfoId
        this.paymentInfoId = paymentData.paymentInfoID;
         // Convert expiryDate to 'MM/YY' format
        const expiryDateMMYY = this.convertExpiryDateToMMYY(paymentData.expiryDate);
        // Map the backend data to paymentInfo object
        this.paymentInfo = {
          billingName: paymentData.billingName,
          cardNumber: paymentData.cardNumber,
          expiryDate: expiryDateMMYY,
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
        this.deliveryInfo = {
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
    validatePaymentInfo() {
      this.paymentErrors = {}; // Reset errors
      let valid = true;

      // Credit Card Number must be 16 digits
      const cardNumber = this.paymentInfo.cardNumber.replace(/\s+/g, '');
      if (!/^\d{16}$/.test(cardNumber)) {
        this.paymentErrors.cardNumber = 'Credit Card Number must be 16 digits.';
        valid = false;
      }

        // Expiry Date must be after today's date
        const expiryDate = this.paymentInfo.expiryDate;
        if (!/^\d{2}\/\d{2}$/.test(expiryDate)) {
          this.paymentErrors.expiryDate = 'Expiry Date must be in MM/YY format.';
          valid = false;
        } else {
          const [expMonthStr, expYearStr] = expiryDate.split('/');
          const expMonth = parseInt(expMonthStr, 10);
          const expYear = parseInt(expYearStr, 10) + 2000; // Assuming years are in 2000s

          if (expMonth < 1 || expMonth > 12) {
            this.paymentErrors.expiryDate = 'Expiry Month must be between 01 and 12.';
            valid = false;
          } else {
            // Get the last day of the expiry month
            const lastDayOfExpiryMonth = new Date(expYear, expMonth, 0);
            const today = new Date();
            today.setHours(0, 0, 0, 0); // Normalize today's date to midnight

            if (lastDayOfExpiryMonth < today) {
              this.paymentErrors.expiryDate = 'Expiry Date must be after today\'s date.';
              valid = false;
            }
          }
        }

      // CVV must be 3 digits
      const cvvCode = this.paymentInfo.cvvCode;
      if (!/^\d{3}$/.test(cvvCode)) {
        this.paymentErrors.cvvCode = 'CVV must be 3 digits.';
        valid = false;
      }

      return valid;
    },

    validateDeliveryInfo() {
      this.deliveryErrors = {}; // Reset errors
      let valid = true;

      // Check if Address Number is empty
      if (!this.deliveryInfo.number || String(this.deliveryInfo.number).trim() === '') {
        this.deliveryErrors.number = 'Address Number is required.';
        valid = false;
      }

      // Check if Street is empty
      if (!this.deliveryInfo.street || this.deliveryInfo.street.trim() === '') {
        this.deliveryErrors.street = 'Street is required.';
        valid = false;
      }

      // Check if City is empty
      if (!this.deliveryInfo.city || this.deliveryInfo.city.trim() === '') {
        this.deliveryErrors.city = 'City is required.';
        valid = false;
      }

      // Check if State/Province is empty
      if (!this.deliveryInfo.stateOrProvince || this.deliveryInfo.stateOrProvince.trim() === '') {
        this.deliveryErrors.stateOrProvince = 'State/Province is required.';
        valid = false;
      }

      // Check if Country is empty
      if (!this.deliveryInfo.country || this.deliveryInfo.country.trim() === '') {
        this.deliveryErrors.country = 'Country is required.';
        valid = false;
      }

      // Postal Code must have the form A1A1A1
      const postalCode = this.deliveryInfo.postalCode;
      if (!/^[A-Za-z]\d[A-Za-z]\d[A-Za-z]\d$/.test(postalCode)) {
        this.deliveryErrors.postalCode = 'Invalid Postal Code';
        valid = false;
      }

      return valid;
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

    async saveDeliveryInfo() {
      if (this.validateDeliveryInfo()){
        try {
          const urlBase = `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/address`;
          if (this.deliveryInfoExists) {
            // Update existing delivery information
            const response = await axios.put(`${urlBase}/${this.deliveryInfoId}?loggedInUsername=${this.LOGGEDINUSERNAME}`, this.deliveryInfo);
            this.paymentInfo.addressId = response.data.addressID;

            console.log(response.data.addressID);
            console.log(this.paymentInfo.addressId);
            this.$swal({
                title: 'Success',
                text: 'Delivery information updated successfully.',
                icon: 'success',
              });
          } else {
            // Add new delivery information
            const response = await axios.post(`${urlBase}?loggedInUsername=${this.LOGGEDINUSERNAME}`, this.deliveryInfo);
            this.$swal({
                title: 'Success',
                text: 'Delivery information saved successfully.',
                icon: 'success',
              });

            // Set deliveryInfoExists to true and store the new deliveryInfoId
            this.deliveryInfoExists = true;
            this.deliveryInfoId = response.data.addressID;
            this.paymentInfo.addressId = response.data.addressID;
          }
        } catch (error) {
          console.error('Error saving delivery information:', error);
          this.$swal({
                title: 'Error',
                text: 'Error saving delivery information',
                icon: 'error',
              });
        }
      }
    },
    async placeOrder() {
      try {
        const response = await axios.post(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/orders?loggedInUsername=${this.LOGGEDINUSERNAME}`, {
          paymentInfoID: this.paymentInfoId,
          addressID: this.deliveryInfoId,
          games: this.games.map((game) => ({
            gameID: game.id,
            quantity: game.quantity,
          })),
        });
        this.$router.push('/');
        this.$swal({
                title: 'Success',
                text: 'Order Placed Successfully',
                icon: 'success',
              });
      } catch (error) {
        console.error('Error placing order:', error);
        this.$swal({
                title: 'Error',
                text: 'Error placing order',
                icon: 'error',
              });
      }
    },
  },
};
</script>

<style scoped>

.error-message {
  color: red;
  font-size: 0.9em;
}

.checkout {
  margin: 15%;
  margin-top: 100px;
}

.title {
  text-align: left;
}

.order-items {
  margin-top: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.game-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  margin-right: 20px;
}

.game-details {
  flex: 1;
}

.game-title {
  margin: 0;
}

.game-description {
  margin: 5px 0;
}

.game-stock {
  font-weight: bold;
  margin: 5px 0;
}

.in-stock {
  color: green;
}

.out-of-stock {
  color: red;
}

.game-quantity {
  display: flex;
  align-items: center;
  margin: 10px 0;
}

.quantity-number {
  font-size: 16px;
}

/* Payment and Delivery Information Styles */
.payment-info,
.delivery-info {
  margin-top: 40px;
}

.payment-info h2,
.delivery-info h2 {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  color: rgba(156, 163, 175, 1);
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  border-radius: 0.375rem;
  padding: 8px;
  box-sizing: border-box;
  background-color: #1e1e1e;
  color: rgba(243, 244, 246, 1);
  border: 1px solid rgba(55, 65, 81, 1);
}

.form-group button {
  padding: 10px 15px;
  cursor: pointer;
  background-color: #BB86FC;
}

.payment-info h3,
.delivery-info h3 {
  margin-top: 20px;
  margin-bottom: 10px;
}
/* Order Breakdown Styles */
.order-breakdown {
margin-top: 40px;
padding: 20px;
border: 1px solid #ccc;
}

.order-breakdown h2 {
margin-bottom: 20px;
}

.breakdown-item,
.breakdown-total {
display: flex;
justify-content: space-between;
margin-bottom: 10px;
}

.breakdown-total {
font-weight: bold;
font-size: 1.2em;
}

.place-order-button {
margin-top: 20px;
padding: 10px 20px;
width: 100%;
font-size: 16px;
cursor: pointer;
background-color:  #BB86FC;
}
</style>