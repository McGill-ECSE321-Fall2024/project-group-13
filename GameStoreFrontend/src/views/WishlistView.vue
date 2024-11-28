<template>
    <div class="wishlist">
      <div class="wishlist-page-view">

        <div>
          <h1 class="title">My Wishlist</h1>
        </div>
        
        <hr class="below-title"> 

        <div>
          <button id="clearBtn"> Clear Wishlist</button>
        </div>

        <div class="wishlist-items">
          
          <!-- will need to add games here-->
          <div v-for="(game, index) in wishlistItems" :key="index" class="wishlist-item">
            <img :src=resolveImagePath(game.img) alt="Game Image" class="game-image" />

            <div class="game-details">
              <h2 class="game-title">{{ game.title }}</h2>
              <p class="game-description">{{ game.description }}</p>
            </div>
                  
            <div class="game-actions">
              <button class="action-button">Add to Cart</button>
              <button class="action-button">Remove from Wishlist</button>
            </div>
          </div>      

          </div>

        </div>
      
      </div>
</template>

<!-- will be removing these games once I begin adding functionality-->
<script>
    import fortnite from '../assets/fortnite.jpg';
    import overwatch from '../assets/overwatch.jpeg';
    import gta from '../assets/GTA.png';

    import axios from 'axios';

    const axiosClient = axios.create({
        baseURL: 'http://localhost:8080'
    });
  
export default {
    name: 'WishlistView',
    // data() {
    //     return {
    //         games: [
    //         {
    //         image: fortnite,
    //         title: 'Fortnite',
    //         description: 'A battle royale game where you fight to be the last one standing.',
    //       },
    //       {
    //         image: overwatch,
    //         title: 'Overwatch',
    //         description: 'A team-based multiplayer first-person shooter.',
    //       },
    //       {
    //         image: gta,
    //         title: 'Grand Theft Auto V',
    //         description: 'An action-adventure game set in an open world.',
    //       },
    //         ]
    //     }
    // },

    data() {
       return {
         wishlistItems: []
         //gameName: null,
         //gameDescription: null,
         //gamePicture: null,
         //errorMessage: null
       };
     },

    async created() {
       try {
            // Fetch the games from logged in user's wishlist (if they are a customer)
            const loggedInUsername = 'defaultCustomer'; // need a way to somehow extract the username of the currently logged in user
            const wishlistGamesResponse = await axiosClient.get('/customers/defaultCustomer/wishlist');
            
            console.log(wishlistGamesResponse.data); 
            this.wishlistItems = wishlistGamesResponse.data.games

       } catch (error) {
            console.error('Error fetching data:', error);
            this.wishlistItems = [];
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

    // one for adding to cart
    // one for removing from wishlist
    
}
</script>

<style scoped>
.wishlist {
  margin-top: 50px;
  background-color: #1e1e1e;
  margin-right: 150px;
  margin-left: 150px;
  border-radius: 2%;
  color: white;
}

.wishlist-page-view {
  display: flex;
  flex-direction: column;
  margin-top: 100px; 
  
}

.title {
  text-align: left;
  color: white;
  font-size: 30px;
  height: 50px;
  width: 100%;
  margin: 2%;
  height: 20px;
}

.below-title {
    border: 0;
    height: 1px;
    background: #333;
    background-image: linear-gradient(to right, #ccc, purple, #ccc);
    margin-top: 10px;
    margin-left: 2%;
    margin-right: 2%;
}

.wishlist-items {
  padding: 0%;
  margin: 2%;
}

.game-details {
  flex: 1;
  margin-bottom: 10%;
}

.game-title {
  margin: 0;
  font-weight: bold;

}

.game-description {
  margin: 5px 0;
}

.wishlist-item {
  display: flex;
  align-items: center;
  margin-bottom: 2%;
  background-color: rgba(84, 84, 84, 0.65);
  padding: 20px;
  border-radius: 3%;

}

.game-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  margin-right: 20px;
  border-radius: 2%;
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

.action-button:hover {
  background-color: #0056b3; /* Darker blue */
}

#clearBtn {
    background-color: #9351f7;
    color: #ffffff; 
    border: none;
    border-radius: 8px; 
    padding: 10px 20px; 
    margin-left: 2%; 
    margin-top: 1%;
    cursor: pointer;
    font-size: 16px; 
    transition: background-color 0.3s ease, transform 0.2s ease; 
    align-items: center;
    justify-content: center;
    min-width: 100px; 
}

/* Button States */
#clearBtn:hover {
    background-color: #a970ff;
    transform: scale(1.05); 
}

#clearBtn:active {
    background-color: #8c3de3; 
    transform: scale(0.98); 
}

#clearBtn:focus {
    outline: none; 
    box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5); 
}
</style>