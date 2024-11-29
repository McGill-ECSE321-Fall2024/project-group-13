<template>
  <main id="game-page">
    <section class="header-container" v-if="game">
      <h1 id="title">{{ game.title }}</h1>
    </section>

    <section class="game-header" v-if="game">
      <div class="game-overview">
        <img
          :src="`../src/assets/${game.img}`"
          :alt="game.title"
          width="480"
          height="270"
          @error="handleImageError"
        />

        <div id="game-info">
          <p id="description">
            {{ game.description }}
          </p>
          <div class="rating-category">
            <p id="game-rating" v-if="game.rating != 0">
              Audience Rating: {{ game.rating }}
            </p>
            <p id="game-rating" v-if="game.rating == 0">
              Audience Rating: None
            </p>
            <p id="category">Game Category: {{ game.categoryName }}</p>
            <p id="parental-rating">
              Parental Rating: {{ game.parentalRating }}
            </p>
            <p id="stock">Current Stock: {{ game.stock }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="game-actions" v-if="game">
      <div class="action-options" v-if="permissionLevel == 1">
        <div>
          <button class="action-buttons" id="buy-now">Add to cart</button
          ><span id="price">{{ game.price }}</span>
          <span id="promotion">{{
            game.promotion ? "-" + game.promotionPercentage + "%" : ""
          }}</span>
        </div>

        <div>
          <button class="action-buttons" id="add-wishlist">
            Add to your wishlist
          </button>
        </div>
      </div>

      <div class="action-options" v-if="permissionLevel == 0">
        <div>
          <button class="log-in-buttons">Log in</button>
        </div>

        <div>
          <button class="log-in-buttons">Sign up</button>
        </div>
      </div>

      <div class="action-options" v-if="permissionLevel == 2">
        <div>
          <button class="archive-request-buttons">Request to Archive</button>
        </div>
      </div>

      <div class="action-options" v-if="permissionLevel == 3">
        <div>
          <button class="archive-buttons">Archive</button>
        </div>
      </div>
      

    </section>

    <section class="review-section" v-if="game">
      <h1>Reviews</h1>

      <div v-if="reviews.length === 0">
        <h3 id="chill-header">
          No reviews yet. It's chill, sometimes games are just underground and
          misunderstood.
        </h3>
        <img
          id="chill-guy"
          :src="`../src/assets/chillguy.jpg`"
          :alt="'Chill Guy'"
          width="480"
          height="270"
          @error="handleImageError"
        />
      </div>

      <div class="reviews">
        <div class="review" v-for="(review, index) in reviews" :key="index">
          <div class="review-header">
            <p class="username">{{ review.username }}</p>
            <div class="likes-container">
              <button class="like-button">Like</button>
              <p class="likes">Likes: {{ review.likes }}</p>
            </div>
          </div>
          <p class="content">{{ review.content }}</p>
          <div class="review-footer">
            <p class="rating">Rating: {{ review.rating }}/5</p>
            <p class="date">Date: {{ formatDate(review.date) }}</p>
          </div>
        </div>
      </div>

      <!-- Add Review Section -->
      <div v-if="!canReview">
        <!-- Add Review Button -->
        <button class="add-review-button" @click="buttonWasPressed = true" v-if="!buttonWasPressed">Add a Review</button>

        <!-- Review Form -->
        <div v-if="buttonWasPressed" class="review-form-container">
          <form @submit.prevent="submitReview" class="review-form">

            <div class="form-group">
              <label for="reviewText">Review:</label>
              <textarea id="reviewText" v-model="reviewText" required></textarea>
            </div>

            <div class="form-group">
              <label for="reviewScore">Score:</label>
              <select id="reviewScore" v-model.number="reviewScore" required>
                <option disabled value="">Select a score</option>
                <option v-for="score in scores" :key="score" :value="score">{{ score }}</option>
              </select>
            </div>

            <div class="form-buttons">
              <button type="submit" class="submit-review-button">Submit Review</button>
              <button type="button" class="cancel-review-button" @click="cancelReview">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </section>
  </main>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
});

export default {
  name: "GameView",
  data() {
    return {
      game: null,
      gameID: null,
      reviews: [],
      error: null,
      permissionLevel: 0,

      // Review Form section
      reviewText: '',
      reviewScore: '',
      canReview: false,
      buttonWasPressed: false,
      scores: [1, 2, 3, 4, 5], // Possible scores for reviews
    };
  },

  created() {
    this.gameID = this.$route.params.gameID;
    this.fetchGameDetails(this.gameID);
    // this.fetchGameDetails(2352);
    this.fetchUserDetails();
    this.checkCanReview();
  },
  methods: {
    async fetchGameDetails(gameID) {
      try {
        const [gameResponse, reviewListResponse] = await Promise.all([
          axiosClient.get(`/games/${gameID}`, {
            params: { loggedInUsername: "owner" },
          }),
          axiosClient.get(`/games/${gameID}/reviews`),
        ]);

        this.game = gameResponse.data;
        this.reviews = reviewListResponse.data.reviews || [];
      } catch (error) {
        console.error("Error fetching data:", error);
        this.error = "Failed to load game details.";
      }
    },

    async fetchUserDetails() {
      try {
        this.permissionLevel = sessionStorage.getItem("permissionLevel");
        this.username = sessionStorage.getItem("loggedInUsername"); 
      } catch (error) {
        this.permissionLevel = 0;
        this.username = "guest";

        console.error("Error fetching data:", error);
        this.error = "Failed to load permission level.";
      }
    },

    async checkCanReview() {
      try {
        const response = await axiosClient.get(`/users/${this.username}/${this.gameID}"`, {});

        this.canReview = response.data.canReview;

      } catch (error) {
        console.error("Error fetching data:", error);
        this.error = "Failed to check if user can review.";
        this.canReview = false;
      }
    },

    async submitReview() {
      try {

        // Prepare the review request dto
        const reviewRequest = {
          description: this.reviewText,
          score: this.reviewScore,
        };

        console.log('Review Request:', reviewRequest);
        console.log('Hi');
        console,log('Game ID:', this.gameID);
        console.log('Bye');
        console.log('Username:', this.username);

        // Send POST request to the API
        const response = await axios.post(
          `/games/${this.gameID}/reviews`,
          reviewRequest,
          {
            params: {
              loggedInUsername: this.username, // Replace with the actual username variable
            },
          }
        );

        // Disable the review form after submission
        this.canReview = false;

        // Handle successful submission
        console.log('Review submitted:', response.data.getStatusCode());
        console.log('Answer:', response.data.getBody());

        // Reset form and hide it
        this.resetForm();
      } catch (error) {
        console.error('Error submitting review:', error);
      }
    },

    cancelReview() {
      this.resetForm();
    },

    resetForm() {
      this.buttonWasPressed = false;
      this.reviewText = '';
      this.reviewScore = '';
    },

    handleImageError(event) {
      event.target.src = "../assets/placeholder.jpg";
    },

    formatDate(dateString) {
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
  },
};
</script>

<style scoped>
#game-page {
  max-width: 1280px;
  margin: 0 auto;
  padding: 5rem 6rem;
  font-weight: normal;
}

.game-header {
  text-align: center;
  background-color: #1e1e1e;
  color: white;

  display: flex;
  flex-direction: column;

  padding: 1rem 1rem;

  margin-left: 0%;
  margin-bottom: 20px;
  width: 100%;
  height: 100%;

  border-radius: 10px;
}

#title {
  font-size: 3rem;
  color: white;
  margin: 0;
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  margin-bottom: 1rem;
}

.game-overview {
  display: flex;

  img {
    border-radius: 5px;
  }
}

#game-info {
  display: flex;
  flex-direction: column;
  font-size: 1.2rem;

  margin-left: 20px;

  .rating-category {
    display: flex;
    flex-direction: row;

    flex-wrap: wrap;
    align-content: center;

    gap: 1rem;

    justify-content: space-evenly;

    flex: 1; /*Takes up the remaining space */

    font-size: 0.8rem;

    color: #619bda; /* Sets a different color */

    p {
      display: flex;
      justify-content: center;
      align-items: center;

      line-height: 1.8rem; /* Centers the botom two boxes' text */

      text-align: center;
      font-weight: bold;
      font-size: 0.9rem;
      color: rgb(255, 255, 255);

      border: solid 1px #c19aff;
      border-radius: 5px;
      width: 12rem;
      height: 1.8rem;

      background-color: #c19aff;
    }

    #category {
      /* margin-top: 1rem; */
      background-color: rgb(103, 159, 0);
      border-color: rgb(103, 159, 0);
    }

    #parental-rating {
      background-color: rgb(77, 0, 217);
      border-color: rgb(77, 0, 217);
    }

    #stock {
      background-color: rgb(17, 97, 255);
      border-color: rgb(17, 97, 255);
    }

    #game-rating {
      background-color: rgb(255, 0, 81);
      border-color: rgb(255, 0, 81);
    }
  }
}

.game-actions {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 20px;

    button {
        padding: 10px;
        border-radius: 5px;
        border: none;
        cursor: pointer;
        font-size: 1rem;
        box-sizing: border-box;
        transition: background-color 0.2s, transform 0.1s;
    }

    .action-options {
        display: flex;
        flex-direction: row;
        gap: 20px;

        button {
            background-color: #619bda;
            color: white;
        }

        /* Hover and Active States */
        button:hover {
            background-color: #a970ff;
            transform: scale(1.05);
        }

        button:active {
            background-color: #8c3de3;
            transform: scale(0.95);
        }

        /* Specific Button Styles */
        .log-in-buttons {
            background-color: #51994f;
        }

        .log-in-buttons:hover {
            background-color: #7fcb8a;
            transform: scale(1.05);
        }

        .log-in-buttons:active {
            background-color: #51994f;
            transform: scale(0.95);
        }

        .archive-request-buttons {
            background-color: #1c50eb;
        }

        .archive-request-buttons:hover {
            background-color: #37a8ff;
            transform: scale(1.05);
        }

        .archive-request-buttons:active {
            background-color: #37a8ff;
            transform: scale(0.95);
        }

        .archive-buttons {
            background-color: #ff0000;
        }

        .archive-buttons:hover {
            background-color: #ff4d4d;
            transform: scale(1.05);
        }

        .archive-buttons:active {
            background-color: #ff0000;
            transform: scale(0.95);
        }

        /* Specific Styles for #buy-now and #price */
        #buy-now {
            border-radius: 5px 0 0 5px;
            background-color: #7347ff;
            color: white;
        }

        #buy-now:hover {
            background-color: #a970ff;
            transform: scale(1.05);
        }

        #buy-now:active {
            background-color: #8c3de3;
        }

        #price {
            color: white;
            padding: 7px 10px;
            border-radius: 0 5px 5px 0;
            background-color: #997aff;
            display: inline-block;
          box-sizing: border-box;
            transition: transform 0.1s;
        }

        /* Scale #price along with #buy-now */
        #buy-now:hover + #price {
            transform: scale(1.05);
        }

        #promotion {
            padding: 10px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 1rem;
            color: #5ba022;
        }

        #add-wishlist {
            background-color: #7347ff;
            color: white;
        }
    }

    #view-wishlist {
        background-color: #92c3e4;
        color: white;
    }
}


.review-section {
  font-size: 1.3rem;
  border-radius: 10px;
  padding: 10px;
  margin-top: 100px;
  background-color: #1e1e1e;

  h1 {
    color: white;
  }

  #chill-header {
    color: white;
    text-align: center;

    margin: 40px 0px;
  }

  #chill-guy {
    border-radius: 5px;
    margin: 0 auto;
    display: block;
  }

  .reviews {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;

    .review {
      width: 100%;
      background-color: #696060;
      color: white;
      padding: 10px;
      border-radius: 5px;
      margin: 10px 0;

      .review-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .username {
          font-size: 1rem;
          font-weight: bold;
          text-decoration: underline;
          margin: 0;
        }

        .likes-container {
          display: flex;
          align-items: center;

          .like-button {
            padding: 5px 10px;
            margin-right: 10px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            background-color: #2f73f1;
            color: white;
            font-size: 0.9rem;
          }

          .likes {
            margin: 0;
            font-size: 0.9rem;
          }
        }
      }

      .content {
        margin: 10px 0;
        font-size: 1rem;
      }

      .review-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .rating,
        .date {
          font-size: 0.9rem;
          margin: 0;
        }
      }
    }
  }
}

/* Add Review Button */
.add-review-button {
  background-color: #7347ff;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  box-sizing: border-box;
  transition: background-color 0.2s, transform 0.1s;
  margin-bottom: 20px;
}

.add-review-button:hover {
  background-color: #a970ff;
  transform: scale(1.05);
}

.add-review-button:active {
  background-color: #8c3de3;
  transform: scale(0.95);
}

/* Review Form Container */
.review-form-container {
  background-color: #1e1e1e;
  padding: 20px;
  border-radius: 10px;
  margin-top: 20px;
}

/* Review Form */
.review-form {
  display: flex;
  flex-direction: column;
}

/* Form Group */
.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  color: white;
}

.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  border-radius: 5px;
  border: 1px solid #555;
  background-color: #333;
  color: white;
}

/* Input Focus Styles */
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #a970ff;
  box-shadow: 0 0 5px rgba(169, 112, 255, 0.5);
}

/* Form Buttons */
.form-buttons {
  display: flex;
  gap: 10px;
}

.submit-review-button,
.cancel-review-button {
  background-color: #7347ff;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  box-sizing: border-box;
  transition: background-color 0.2s, transform 0.1s;
}

.submit-review-button:hover,
.cancel-review-button:hover {
  background-color: #a970ff;
  transform: scale(1.05);
}

.submit-review-button:active,
.cancel-review-button:active {
  background-color: #8c3de3;
  transform: scale(0.95);
}
</style>
