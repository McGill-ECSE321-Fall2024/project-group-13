<template>
    <div class="wishlist">
      <div class="wishlist-page-view">

        <div>
          <h1 class="title">My Wishlist</h1>
        </div>
        
        <hr class="below-title"> 
        
        <div>
          <button @click="clearCart" v-if="isACustomer" id="clearBtn"> Clear Wishlist</button>
        </div>

        <div class="wishlist-items">
          
          <!-- div only appears if not a customer-->
          <div v-if="!isACustomer" id="notACustomer"> Must be a logged in customer to view your own wishlist </div>

          <!-- will need to add games here-->
          <div v-for="(game, index) in wishlistItems" :key="index" class="wishlist-item">
            <img :src=resolveImagePath(game.img) alt="Game Image" class="game-image" />

            <div class="game-details">
              <h2 class="game-title">{{ game.title }}</h2>
              <p class="game-description">{{ game.description }}</p>
            </div>
                  
            <div class="game-actions">
              <button class="action-button">Add to Cart</button>
              <button @click="clearCartItem(game.gameID)" class="action-button">Remove from Wishlist</button>
            </div>
          </div>      

          </div>

        </div>
      
      </div>
</template>

<!-- will be removing these games once I begin adding functionality-->
<script>
    import axios from 'axios';

    const axiosClient = axios.create({
        baseURL: 'http://localhost:8080'
    });
  
export default {
    name: 'WishlistView',
    
    data() {
       return {
         wishlistItems: [],
         //errorMessage: null
         isACustomer: false
       };
     },

    async created() {
      // Fetch the games from logged in user's wishlist (if they are a customer)
      // FIX THIS ISSUE WITH GUEST
      const username = sessionStorage.getItem("loggedInUsername") 
      const permission = parseInt(sessionStorage.getItem("permissionLevel"))
      console.log("loggedInUsername is now:" , username);
      console.log("permissionLevel is now: ", permission);
    
      try {
            if (permission === 1) {
              const wishlistGamesResponse = await axiosClient.get('/customers/' + username + '/wishlist');
              // if not a customer, display that you must a logged in customer
              console.log(wishlistGamesResponse.data); 
              this.wishlistItems = wishlistGamesResponse.data.games
              this.isACustomer = true;
            } else {
              console.log("User must be a customer account");
            }
       } catch (error) {
          console.error('Error fetching data:', error);
          this.wishlistItems = [];

          if (error.response) {
            const status = error.response.status;
            const message = error.response.data?.message || "An error occurred.";
                    
            // Display user-friendly messages based on status codes or backend message
            if (status == 403) {
              this.errorMessage = message; // Example: Invalid credentials
              console.log(message);
            } else {
              this.errorMessage = "An unexpected error occurred.";
            }
            this.wishlistItems = [];
            } else {
              // Network or unexpected error
              console.error(error);
              this.errorMessage = "Unable to connect to the server.";
              this.wishlistItems = [];
            }
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

      async clearCart() {
        const username = sessionStorage.getItem("loggedInUsername") 
        const permission = parseInt(sessionStorage.getItem("permissionLevel"))
        console.log("loggedInUsername is now:" , username);
        console.log("permissionLevel is now: ", permission);
        try {
          const wishlistGamesResponse = await axiosClient.delete('/customers/' + username + '/wishlist');
          // if not a customer, display that you must a logged in customer
          console.log("items are now deleted");  
          this.wishlistItems = wishlistGamesResponse.data.games
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      },

      async clearCartItem(gameID) {
        const username = sessionStorage.getItem("loggedInUsername") 
        const permission = parseInt(sessionStorage.getItem("permissionLevel"))
        //const gameId 
        console.log("loggedInUsername is now:" , username);
        console.log("permissionLevel is now: ", permission);

        try {
          const wishlistGamesResponse = await axiosClient.delete('/customers/' + username + '/wishlist/' + gameID);
          this.wishlistItems = this.wishlistItems.filter(wishlistItemToDelete => Number(wishlistItemToDelete.gameID) !== Number(gameID));
          // if not a customer, display that you must a logged in customer
          console.log("item is now deleted");  
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      }

    // one for adding to cart
    // one for removing from wishlist

    }, 
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

#notACustomer {
  text-align: center;
  color: white;
  font-size: 30px;
  height: 50px;
  width: 100%;
  margin: 1%;
  height: 20px;
}
</style>