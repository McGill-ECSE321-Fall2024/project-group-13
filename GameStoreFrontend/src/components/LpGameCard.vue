<template>
    <div class="game-card">
      <img :src="image" alt="Game Image" class="game-image" />
      <div class="game-content">
        <h2 class="game-title">{{ title }}</h2>
        <p class="game-price">{{ price }}</p>
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
        type: String,
        required: true
      },
      description: {
        type: String,
        default: ''
      }
    }
  }
  </script>
  
  <style scoped>
  .game-card {
    width: 450px;
    height: 500px;
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
    flex-grow: 1;
    color: #B0B0B0;
  }
  
  .buy-button {
    background-color: #BB86FC;
    color: #FFFFFF;
    border: none;
    padding: 12px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 1em;
  }
  
  .buy-button:hover {
    background-color: #9A67EA;
  }
  
  </style>
  