<template>
    <div class="wishlist">
        <h1 class="title">My Wishlist</h1>
        <hr class="below-title"> 
        <div class="wishlist-items">
            <div v-for="(game, index) in games" :key="index" class="wishlist-item">
                <img :src="game.image" alt="Game Image" class="game-image" />
                <div class="game-details">
                    <h2 class="game-title">{{ game.title }}</h2>
                    <p class="game-description">{{ game.description }}</p>
                </div>
                <p class="game-description">{{ game.price }}</p>
                <div class="game-actions">
                    <button class="action-button">Add to Cart</button>
                    <button class="action-button">Remove from Wishlist</button>
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
  
export default {
    name: 'WishlistView',
    data() {
        return {
            games: [
            {
            image: fortnite,
            title: 'Fortnite',
            description: 'A battle royale game where you fight to be the last one standing.',
          },
          {
            image: overwatch,
            title: 'Overwatch',
            description: 'A team-based multiplayer first-person shooter.',
          },
          {
            image: gta,
            title: 'Grand Theft Auto V',
            description: 'An action-adventure game set in an open world.',
          },
            ]
        }

    }
}
</script>

<style scoped>
.wishlist {
  margin: 20px;
  background-color: #1e1e1e;
  border-radius: 1%;
  color: white;
}

.title {
  text-align: left;
  padding: 2%;
  font-weight: bold;
}

.below-title {
    height: 2px;
    border: none;
    border-radius: 6px;
    background: linear-gradient(
    90deg,
    rgba(13, 8, 96, 1) 0%,
    rgba(9, 9, 121, 1) 21%,
    rgba(6, 84, 170, 1) 51%,
    rgba(0, 255, 113, 1) 100%
  );
}

.wishlist-items {
  margin-top: 1%;
  padding: 1%;
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
</style>