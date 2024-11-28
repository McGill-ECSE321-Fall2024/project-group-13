<template>
    <div class="game-card">
      <img :src="resolveImagePath(image)" alt="Game Image" class="game-image" />
      <div class="game-content">
        <h2 class="game-title">{{ title }}</h2>
        <p class="game-price">${{ price.toFixed(2) }}</p>
        <p class="game-description">{{ description }}</p>
        <button class="buy-button">See More</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'LpGameCard',
    props: {
      image: {
        type: String,
        required: true
      },
      title: {
        type: String,
        required: true
      },
      price: {
        type: Number,
        required: true
      },
      description: {
        type: String,
        default: ''
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
    }
  }
  </script>
  
  <style scoped>
.game-card {
  width: 400px; 
  height: 450px; 
  background-color: #121212; 
  color: #FFFFFF; 
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.7);
}
  
  .game-image {
    width: 100%;
    height: 60%; 
    object-fit: cover;
  }
  
  .game-content {
    padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    flex-grow: 1;
  }
  
  .game-title {
    font-size: 1.5em;
    margin: 0;
    color: #BB86FC; 
  }
  
  .game-price {
    font-size: 1.2em;
    margin: 10px 0;
    color: #ece6e6; 
  }
  
  .game-description {
  color: #B0B0B0;
  display: -webkit-box;        
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;       /* Clamps to 1 line */
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2em;          /* Ensures line height doesn't cause overflow */
  max-height: 1.2em;           
}
  
  .buy-button {
    background-color: #BB86FC;
    color: #FFFFFF;
    border: none;
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9em;
  }
  
  .buy-button:hover {
    background-color: #9A67EA;
  }
  
  </style>
  