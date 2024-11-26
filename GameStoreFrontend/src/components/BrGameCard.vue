<template>
    <div class="game-card">
      <img :src="resolveImagePath(image)" alt="Game Image" class="game-image" />
      <div class="game-content">
        <h2 class="game-title">{{ title }}</h2>
        <p class="game-price">${{ price.toFixed(2) }}</p>
        <p class="game-description">{{ description }}</p>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BrpGameCard',
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
      },
      stock: {
        type: Number,
        required: true
      },
      promotionTitle: {
        type: String,
        default: ''
      },
      categoryId: {
        type: Number,
      },
      gameId: {
        type: Number,
        required: true
      },
      visibility : {
        type: String,
        required: true
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
  },
  };
  </script>
  
  <style scoped>
  .game-card {
    width: 225px;
    height: 250px;
    background-color: #121212;
    color: #FFFFFF;
    border-radius: 10px;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.7);
    transition: transform 0.3s, box-shadow 0.3s;
  }
  
  .game-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.8);
  }
  
  .game-image {
    width: 100%;
    height: 150px;
    object-fit: cover;
  }
  
  .game-content {
    padding: 10px;
    display: flex;
    flex-direction: column;
    text-align: left;
    flex-grow: 1;
  }
  
  .game-title {
    font-size: 1.25em;
    margin: 0;
    color: #BB86FC;
  }
  
  .game-price {
    font-size: 1.0em;
    color: #ece6e6;
    margin: 5px 0;
  }
  
  .game-description {
    font-size: 0.9em;
    color: #CCCCCC;
    margin: 5px 0;
    flex-grow: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1; /* Number of lines to show */
    -webkit-box-orient: vertical;
  }
  </style>
  