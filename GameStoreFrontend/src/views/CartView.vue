<template>
    <div class="cart">
      <h1 class="title">My Cart</h1>
      <div class="cart-items">
        <div v-for="(game, index) in games" :key="index" class="cart-item">
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
              <button class="quantity-button">-</button>
              <span class="quantity-number">{{ game.quantity }}</span>
              <button class="quantity-button">+</button>
            </div>
          </div>
          <div class="game-actions">
            <button class="action-button">Remove from Cart</button>
            <button class="action-button">Add to Wishlist</button>
          </div>
        </div>
      </div>
      <div class="cart-summary">
        <p class="subtotal">Subtotal: $159.96</p>
        <button class="checkout-button" @click="goToCheckout">Checkout</button>
    </div>
    </div>
  </template>
  
  <script>
  import fortnite from '../assets/fortnite.jpg';
  import overwatch from '../assets/overwatch.jpeg';
  import gta from '../assets/GTA.png';
  
  export default {
    name: 'CartView',
    data() {
      return {
        games: [
          {
            image: fortnite,
            title: 'Fortnite',
            description: 'A battle royale game where you fight to be the last one standing.',
            quantity: 1,
            price: '$39.99',
            inStock: true,
          },
          {
            image: overwatch,
            title: 'Overwatch',
            description: 'A team-based multiplayer first-person shooter.',
            quantity: 2,
            price: '$29.99',
            inStock: false,
          },
          {
            image: gta,
            title: 'Grand Theft Auto V',
            description: 'An action-adventure game set in an open world.',
            quantity: 1,
            price: '$59.99',
            inStock: true,
          },
        ],
      };
    },

    methods: {
      goToCheckout() {
        this.$router.push('/checkout');
      },
    },
  };
  </script>
  
  <style scoped>
.cart {
  margin: 20px;
}

.title {
  text-align: left;
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
  background-color: #BB86FC; /* Blue background */
  color: white;              /* White text */
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.action-button:last-child {
  margin-bottom: 0;
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
  background-color: #0056b3; /* Darker blue */
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