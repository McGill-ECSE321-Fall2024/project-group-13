<template>
  <div class="cart">
    <div class="cart-header">
      <h1 class="title">My Cart</h1>
      <button
      class="clear-cart-button"
      @click="confirmClearCart"
      :disabled="games.length === 0"
    >
      Clear Cart
    </button>
    </div>
    <!-- Check if the cart is empty -->
    <div v-if="games.length > 0">
      <!-- Existing cart items -->
      <div class="cart-items">
        <div v-for="(game, index) in games" :key="index" class="cart-item">
          <img :src="resolveImagePath(game.img)" alt="Game Image" class="game-image" />
          <div class="game-details">
            <h2 class="game-title">{{ game.title }}</h2>
            <p class="game-description">{{ game.description }}</p>
            <p
              class="game-stock"
              :class="{ 'in-stock': game.stock > 0, 'out-of-stock': game.stock === 0 }"
            >
              {{ game.stock > 0 ? 'In Stock' : 'Out of Stock' }}
            </p>
            <p class="game-price">${{ game.price.toFixed(2) }}</p>
            <div class="game-quantity">
              <button
                class="quantity-button"
                @click="decreaseQuantity(game.gameID, game.quantity)"
              >
                -
              </button>
              <span class="quantity-number">{{ game.quantity }}</span>
              <button
                class="quantity-button"
                @click="increaseQuantity(game.gameID, game.quantity, game.stock)"
              >
                +
              </button>
            </div>
          </div>
          <div class="game-actions">
            <button class="action-button" @click="removeFromCart(game.gameID)">
              Remove from Cart
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Display this when the cart is empty -->
    <div v-else class="empty-cart">
      <p>
        Your Cart is empty.
        <router-link to="/browse">Browse games</router-link>
        to fill your cart.
      </p>
    </div>

    <!-- Cart summary and checkout button -->
    <div class="cart-summary">
      <!-- Only show subtotal if there are items in the cart -->
      <p class="subtotal" v-if="games.length > 0">
        Subtotal: ${{ subtotalPrice.toFixed(2) }}
      </p>
      <button
        class="checkout-button"
        @click="goToCheckout"
        :disabled="games.length === 0"
      >
        Checkout
      </button>
    </div>
  </div>
</template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'CartView',
    data() {
      return {
        games: [],
        subtotalPrice: 0,
      };
    },
    created() {
      this.fetchCartData();
    },

    computed: {
      LOGGEDINUSERNAME() {
        return sessionStorage.getItem('loggedInUsername');
        },
      PERMISSIONLEVEL() {
        return sessionStorage.getItem('permissionLevel');
        },
      },
    methods: {
      async fetchCartData() {
        try {
          const response = await axios.get(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart`);
          const cartData = response.data;
          this.games = cartData.games.map((game) => ({...game, quantity: game.quantity || 1,}));
          this.subtotalPrice = cartData.subtotalPrice;
        } catch (error) {
          console.error('Error fetching cart data:', error);
        }
      },
      goToCheckout() {
        this.$router.push('/checkout');
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

  async increaseQuantity(gameID, currentQuantity, stock) {
    if (currentQuantity >= stock) {
      this.$swal({
                title: 'Error',
                text: 'Cannot increase quantity beyond available stock',
                icon: 'error',
              });
      return;
    }
    const newQuantity = currentQuantity + 1;
    try {
      // Update the quantity in the backend
      await axios.put(
        `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart/${gameID}/quantity/${newQuantity}`
      );
      // Update the quantity in the local state
      const game = this.games.find(
        (game) => Number(game.gameID) === Number(gameID)
      );
      if (game) {
        // Use this.$set to ensure reactivity
        game.quantity = newQuantity;
        this.subtotalPrice = this.games.reduce((sum, game) => sum + game.price * game.quantity, 0);
      }
    } catch (error) {
      console.error('Error increasing quantity:', error);
      this.$swal({
                title: 'Error',
                text: 'There was an issue updating the quantity, please try again',
                icon: 'error',
              });
    }
  },

  async decreaseQuantity(gameID, currentQuantity) {
    const newQuantity = currentQuantity - 1;
    if (newQuantity < 0) {
      return;
    }
    try {
      if (newQuantity > 0) {
        await axios.put(
          `http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart/${gameID}/quantity/${newQuantity}`
        );
        const game = this.games.find(
          (game) => Number(game.gameID) === Number(gameID)
        );
        if (game) {
          // Use this.$set to ensure reactivity
          game.quantity = newQuantity;
          this.subtotalPrice = this.games.reduce((sum, game) => sum + game.price * game.quantity, 0);
        }
      } else {
        await this.removeFromCart(gameID);
      }
    } catch (error) {
      console.error('Error decreasing quantity:', error);
      this.$swal({
                title: 'Error',
                text: 'There was an issue updating the quantity, please try again',
                icon: 'error',
              });
    }
  },
    confirmClearCart() {
      this.$swal({
        title: 'Are you sure?',
        text: 'This will remove all items from your cart.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, clear it!',
      }).then((result) => {
        if (result.isConfirmed) {
          this.clearCart();
        }
      });
    },
    async removeFromCart(gameID) {
    try {
      await axios.delete(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart/${gameID}`);
      // Remove the game from the games array
      this.games = this.games.filter(game => Number(game.gameID) !== Number(gameID));
      // Recalculate the subtotal price
      this.subtotalPrice = this.games.reduce((sum, game) => sum + game.price * game.quantity, 0);
    } catch (error) {
      console.error('Error removing game from cart:', error);
      this.$swal({
                title: 'Error',
                text: 'There was an issue removing the cart item, please try again',
                icon: 'error',
              });
    }
  },

  async clearCart() {
    try {
      await axios.delete(`http://localhost:8080/customers/${this.LOGGEDINUSERNAME}/cart`);
      // Remove the game from the games array
      this.games = []
      // Recalculate the subtotal price
      this.subtotalPrice = 0;
    } catch (error) {
      console.error('Error clearing cart:', error);
      this.$swal({
                title: 'Error',
                text: 'There was an issue clearing the cart, please try again',
                icon: 'error',
              });
    }
  }
    },
  };
  </script>
  
  <style scoped>
  .checkout-button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

/* Optionally, adjust text color and other styles when disabled */
.checkout-button:disabled:hover {
  background-color: grey; /* Prevent hover effect */
}

.cart {
  margin: 10%;
  margin-top: 100px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  text-align: left;
}

.clear-cart-button {
  background-color: #dc3545; /* Red background for destructive action */
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

.clear-cart-button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

.clear-cart-button:disabled:hover {
  background-color: grey;
  cursor: not-allowed;
}

.clear-cart-button:hover {
  background-color: #c82333; /* Darker red */
}

.cart-items {
  margin-top: 20px;
}

.cart-item {
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

.quantity-button {
  background: none;
  border: none;
  font-size: 24px;
  padding: 0 10px;
  cursor: pointer;
  /* Add color styles */
  color: #007BFF; /* Blue color */
}

.quantity-number {
  font-size: 16px;
}

.game-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.action-button {
  margin-bottom: 10px;
  /* Apply common button styles */
  background-color: #dc3545;; /* Blue background */
  color: white;              /* White text */
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.action-button:last-child {
  margin-bottom: 0;
}

.action-button:disabled {
  background-color: grey;
  cursor: not-allowed;
}

/* Optionally, adjust text color and other styles when disabled */
.action-button:disabled:hover {
  background-color: grey; /* Prevent hover effect */
}

.checkout-button {
  float: right;
  padding: 10px 20px;
  font-size: 16px;
  /* Apply common button styles */
  background-color: #03DAC6; /* Green background */
  color: white;              /* White text */
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* Hover Effects */
.quantity-button:hover {
  color: #0056b3; /* Darker blue */
}

.action-button:hover {
  background-color: #c82333;; /* Darker blue */
}

.checkout-button:hover {
  background-color: #218838; /* Darker green */
}

.cart-summary {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
}

.subtotal {
  font-size: 18px;
  margin-right: 20px;
}
</style>
