<template>
    <div class="order">
      <h1 class="title">Checkout</h1>
      <div class="order-items">
        <div v-for="(game, index) in games" :key="index" class="order-item">
          <img :src="game.image" alt="Game Image" class="game-image" />
          <div class="game-details">
            <h2 class="game-title">{{ game.title }}</h2>
            <p class="game-description">{{ game.description }}</p>
            <p
              class="game-stock"
              :class="{ 'in-stock': game.inStock, 'out-of-stock': !game.inStock }"
            >
              {{ game.inStock ? 'In Stock' : 'Out of Stock' }}
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
              v-model="paymentInfo.creditCardNumber"
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
            <input type="text" id="cvv" v-model="paymentInfo.cvv" required />
          </div>
  
          <!-- Billing Address -->
          <h3>Billing Address</h3>
          <div class="form-group">
            <label for="street">Street</label>
            <input
              type="text"
              id="street"
              v-model="paymentInfo.billingAddress.street"
              required
            />
          </div>
          <div class="form-group">
            <label for="addressNumber">Address Number</label>
            <input
              type="text"
              id="addressNumber"
              v-model="paymentInfo.billingAddress.addressNumber"
              required
            />
          </div>
          <div class="form-group">
            <label for="apartmentNumber">Apartment Number (Optional)</label>
            <input
              type="text"
              id="apartmentNumber"
              v-model="paymentInfo.billingAddress.apartmentNumber"
            />
          </div>
          <div class="form-group">
            <label for="city">City</label>
            <input
              type="text"
              id="city"
              v-model="paymentInfo.billingAddress.city"
              required
            />
          </div>
          <div class="form-group">
            <label for="provinceState">Province/State</label>
            <input
              type="text"
              id="provinceState"
              v-model="paymentInfo.billingAddress.provinceState"
              required
            />
          </div>
          <div class="form-group">
            <label for="country">Country</label>
            <input
              type="text"
              id="country"
              v-model="paymentInfo.billingAddress.country"
              required
            />
          </div>
          <div class="form-group">
            <label for="postalCode">Postal Code</label>
            <input
              type="text"
              id="postalCode"
              v-model="paymentInfo.billingAddress.postalCode"
              required
            />
          </div>
        </form>
      </div>
  
      <!-- Delivery Information -->
      <div class="delivery-info">
        <h2>Delivery Information</h2>
        <!-- Use Billing Address Button -->
        <div class="form-group">
          <button type="button" @click="useBillingAddress">
            Use Billing Address
          </button>
        </div>
        <form>
          <!-- Name -->
          <div class="form-group">
            <label for="deliveryName">Name</label>
            <input
              type="text"
              id="deliveryName"
              v-model="deliveryInfo.name"
              required
            />
          </div>
  
          <!-- Delivery Address -->
          <h3>Delivery Address</h3>
          <div class="form-group">
            <label for="deliveryStreet">Street</label>
            <input
              type="text"
              id="deliveryStreet"
              v-model="deliveryInfo.address.street"
              required
            />
          </div>
          <div class="form-group">
            <label for="deliveryAddressNumber">Address Number</label>
            <input
              type="text"
              id="deliveryAddressNumber"
              v-model="deliveryInfo.address.addressNumber"
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
              v-model="deliveryInfo.address.apartmentNumber"
            />
          </div>
          <div class="form-group">
            <label for="deliveryCity">City</label>
            <input
              type="text"
              id="deliveryCity"
              v-model="deliveryInfo.address.city"
              required
            />
          </div>
          <div class="form-group">
            <label for="deliveryProvinceState">Province/State</label>
            <input
              type="text"
              id="deliveryProvinceState"
              v-model="deliveryInfo.address.provinceState"
              required
            />
          </div>
          <div class="form-group">
            <label for="deliveryCountry">Country</label>
            <input
              type="text"
              id="deliveryCountry"
              v-model="deliveryInfo.address.country"
              required
            />
          </div>
          <div class="form-group">
            <label for="deliveryPostalCode">Postal Code</label>
            <input
              type="text"
              id="deliveryPostalCode"
              v-model="deliveryInfo.address.postalCode"
              required
            />
          </div>
        </form>
      </div>
      <div class="order-breakdown">
            <h2>Order Breakdown</h2>
            <div class="breakdown-item">
                <span>Subtotal: </span>
                <span>${{ subtotal.toFixed(2) }}</span>
            </div>
            <div class="breakdown-item">
                <span>Promotions: </span>
                <span>-${{ promotion.toFixed(2) }}</span>
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
            <button class="place-order-button">Place Order</button>
            </div>
        </div>
    </div>
  </template>
  
  <script>
  import fortnite from "../assets/fortnite.jpg";
  import overwatch from "../assets/overwatch.jpeg";
  import gta from "../assets/GTA.png";
  
  export default {
    name: "OrderView",
    data() {
      return {
        games: [
          {
            image: fortnite,
            title: "Fortnite",
            description:
              "A battle royale game where you fight to be the last one standing.",
            quantity: 1,
            price: "$39.99",
            inStock: true,
          },
          {
            image: overwatch,
            title: "Overwatch",
            description: "A team-based multiplayer first-person shooter.",
            quantity: 2,
            price: "$29.99",
            inStock: false,
          },
          {
            image: gta,
            title: "Grand Theft Auto V",
            description: "An action-adventure game set in an open world.",
            quantity: 1,
            price: "$59.99",
            inStock: true,
          },
        ],
        paymentInfo: {
          billingName: "",
          creditCardNumber: "",
          expiryDate: "",
          cvv: "",
          billingAddress: {
            street: "",
            addressNumber: "",
            apartmentNumber: "",
            city: "",
            provinceState: "",
            country: "",
            postalCode: "",
          },
        },
        deliveryInfo: {
          name: "",
          address: {
            street: "",
            addressNumber: "",
            apartmentNumber: "",
            city: "",
            provinceState: "",
            country: "",
            postalCode: "",
          },
        },
        subtotal: 156.96,
        deliveryFee: 5,
        promotion: 10,
        taxRate: 0.15, // 15% QST
            };
        },
        computed: {
            taxes() {
            return this.subtotal * this.taxRate;
            },
            total() {
            return this.subtotal + this.deliveryFee + this.taxes - this.promotion;
            },
        },
    methods: {
      useBillingAddress() {
        this.deliveryInfo.name = this.paymentInfo.billingName;
        this.deliveryInfo.address = { ...this.paymentInfo.billingAddress };
      },
    },
  };
  </script>
  
  <style scoped>
  .order {
    margin: 20px;
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