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
          <p class="game-description">{{ game.price }}</p>
          <div class="game-quantity">
            <span class="quantity-number">Quantity: {{ game.quantity }}</span>
          </div>
        </div>
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
        </div>

        <!-- CVV -->
        <div class="form-group">
          <label for="cvv">CVV</label>
          <input type="text" id="cvv" v-model="paymentInfo.cvvCode" required />
        </div>

        <div class="form-group">
          <button type="button" @click="savePaymentInfo">
             {{ paymentInfoExists ? 'Update Payment Information' : 'Save New Payment Information' }}
         </button>
        </div>
      </form>
    </div>

    <!-- Delivery Information -->
    <div class="delivery-info">
      <h2>Delivery Information</h2>
      <form>

        <div class="form-group">
          <label for="deliveryStreet">Street</label>
          <input
            type="text"
            id="deliveryStreet"
            v-model="deliveryInfo.street"
            required
          />
        </div>
        <div class="form-group">
          <label for="deliveryAddressNumber">Address Number</label>
          <input
            type="text"
            id="deliveryAddressNumber"
            v-model="deliveryInfo.number"
            required
          />
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
        <div class="form-group">
          <label for="deliveryCity">City</label>
          <input
            type="text"
            id="deliveryCity"
            v-model="deliveryInfo.city"
            required
          />
        </div>
        <div class="form-group">
          <label for="deliveryProvinceState">Province/State</label>
          <input
            type="text"
            id="deliveryProvinceState"
            v-model="deliveryInfo.stateOrProvince"
            required
          />
        </div>
        <div class="form-group">
          <label for="deliveryCountry">Country</label>
          <input
            type="text"
            id="deliveryCountry"
            v-model="deliveryInfo.country"
            required
          />
        </div>
        <div class="form-group">
          <label for="deliveryPostalCode">Postal Code</label>
          <input
            type="text"
            id="deliveryPostalCode"
            v-model="deliveryInfo.postalCode"
            required
          />
        </div>
        <div class="form-group">
          <button type="button" @click="saveDeliveryInfo">
             {{ deliveryInfoExists ? 'Update Delivery Information' : 'Save New Delivery Information' }}
         </button>
        </div>
      </form>
    </div>
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
        const response = await axios.get('http://localhost:8080/customers/defaultCustomer/cart');
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
        const response = await axios.get('http://localhost:8080/customers/defaultCustomer/paymentInfo');
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
        const response = await axios.get('http://localhost:8080/customers/defaultCustomer/address?loggedInUsername=defaultCustomer');
        const deliveryData = response.data;
        // Store the deliveryInfoId
        this.deliveryInfoId = deliveryData.addressID;
        console.log(deliveryData.addressID);
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
    async savePaymentInfo() {
      try {
        const urlBase = 'http://localhost:8080/customers/defaultCustomer/paymentInfo';
        if (this.paymentInfoExists) {
          // Update existing payment information
          const response = await axios.put(`${urlBase}/${this.paymentInfoId}`, this.paymentInfo);
          alert('Payment information updated successfully.');
        } else {
          // Add new payment information
          const response = await axios.post(urlBase, this.paymentInfo);
          alert('Payment information saved successfully.');

          // Set paymentInfoExists to true and store the new paymentInfoId
          this.paymentInfoExists = true;
          this.paymentInfoId = response.data.id;
        }
      } catch (error) {
        console.error('Error saving payment information:', error);
        alert('Error saving new payment information');
      }
    },
    async saveDeliveryInfo() {
      try {
        const urlBase = 'http://localhost:8080/customers/defaultCustomer/address';
        if (this.paymentInfoExists) {
          // Update existing payment information
          const response = await axios.put(`${urlBase}/${this.deliveryInfoId}?loggedInUsername=defaultCustomer`, this.deliveryInfo);
          alert('Delivery information updated successfully.');
        } else {
          // Add new payment information
          const response = await axios.post(`${urlBase}?loggedInUsername=defaultCustomer`, this.deliveryInfo);
          alert('Delivery information saved successfully.');

          // Set deliveryInfoExists to true and store the new deliveryInfoId
          this.deliveryInfoExists = true;
          this.deliveryInfoId = response.data.id;
        }
      } catch (error) {
        console.error('Error saving delivery information:', error);
        alert('Error saving new delivery information');
      }
    },
    async placeOrder() {
      try {
        const response = await axios.post('http://localhost:8080/customers/defaultCustomer/orders?loggedInUsername=defaultCustomer', {
          paymentInfoID: this.paymentInfoId,
          addressID: this.deliveryInfoId,
          games: this.games.map((game) => ({
            gameID: game.id,
            quantity: game.quantity,
          })),
        });
        this.$router.push('/');
        alert('Order placed successfully.');
      } catch (error) {
        console.error('Error placing order:', error);
        alert('Error placing order');
      }
    },
  },
};
</script>

<style scoped>

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
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
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