<template>
  <div class="game-card">
    <div class="image-wrapper">
      <img :src="resolveImagePath(image)" alt="Game Image" class="game-image" />

      <!-- Promotional Subtext -->
      <div class="promosubtext" v-if="promotionPercentage != 0 && status != 'Archived'">
        <div class="promopct" v-if="promotionPercentage != 0 && status != 'Archived'">
          <span v-if="promotionPercentage != 0">-{{promotionPercentage}}%</span>
        </div>
      </div>
    </div>
    
    <div class="game-content">
      <h2 class="game-title">{{ title }}</h2>
      <p class="game-price" v-if="promotionPercentage === 0">${{ price.toFixed(2) }}</p>
      <div class="promoprice-wrapper"> 
        <p class="game-old-price" v-if="promotionPercentage != 0">${{ price.toFixed(2)}}</p>
        <p class="game-new-price" v-if="promotionPercentage != 0">${{ (price - (price * promotionPercentage / 100)).toFixed(2) }}</p>
      </div>

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
      },
      gameCategory: {
        type: String,
        required: true
      },
      promotionPercentage: {
        type: Number,
        required: true
      },  
      rating : {
        type: Number,
        required: true
      },

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
    width: 215px;
    height: 230px;
    background-color: #121212;
    color: #FFFFFF;
    border-radius: 10px;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.7);
    transition: transform 0.3s, box-shadow 0.3s;
    cursor: pointer;
  }
  
  .game-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.8);
  }
  
  .image-wrapper {
    position: relative; 
    width: 100%;
    height: 130px;
  }
  
  .game-image {
    width: 100%;
    height: 130px;
    object-fit: cover;
  }
  
  .promosubtext {
    position: absolute; 
    top: 0px; 
    left: 0px; 
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 5px 10px;
    border-top-left-radius: 10px;
    border-bottom-right-radius: 10px;
    gap: 8px;
    background: rgba(7, 226, 43, 0.1);
    backdrop-filter: blur(10px);
  }
  
  .promopct > span {
    font-weight: bold;
    color: rgb(170, 228, 170);
    font-size: 0.75em;
  }

  .game-content {
    padding: 10px;
    display: flex;
    flex-direction: column;
    text-align: left;
    flex-grow: 1;
  }
  
  .game-title {
    font-size: 1.20em;
    margin: 0;
    color: #BB86FC;
  }
  
  .game-price {
    font-size: 0.9em;
    color: #ece6e6;
    margin: 5px 0;
  }
  
  .game-old-price {
    font-size: 0.9em;
    /* make old price more transparent */
    color: rgba(236, 230, 230, 0.5);
    margin: 5px 0;
    text-decoration: line-through;
  }

  .game-new-price {
    font-size: 0.9em;
    color: #ece6e6;
    margin: 5px 0;
  }

  .promoprice-wrapper {
    display: flex;
    flex-direction: row;
    gap: 8px;
  }
  
  .game-description {
    font-size: 0.9em;
    color: #CCCCCC;
    margin: 5px 0;
    flex-grow: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
  }
  </style>
  
  