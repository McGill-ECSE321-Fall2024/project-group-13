import axios from 'axios';
<template>
  <div id="dashboard">
    <!-- Games Section -->
    <section class="box">
      <h2>Games</h2>
      <div class="controls">
        <button @click="addGame">Add Game</button>
        <input type="text" placeholder="Filter games..." v-model="gameFilter" />
      </div>
      <ul class="list">
        <li v-for="game in filteredGames" :key="game.id">
          <div v-if="!game.isNew">
            {{ game.name }}
            <button @click="deleteGame(game.id)">Delete</button>
          </div>
          <div v-else>
            <input type="text" v-model="game.tempName" placeholder="Enter game name" />
            <button @click="saveGame(game.id)">Save</button>
            <button @click="cancelGame(game.id)">Cancel</button>
          </div>
        </li>
      </ul>
    </section>

    <!-- Promotions Section -->
    <section class="box">
      <h2>Promotions</h2>
      <div class="controls">
        <button @click="addPromotion">Add Promotion</button>
      </div>
      <ul class="list">
        <li v-for="promotion in promotions" :key="promotion.id">
          <div v-if="!promotion.isNew">
            {{ promotion.name }}
          </div>
          <div v-else>
            <input type="text" v-model="promotion.tempName" placeholder="Enter promotion name" />
            <button @click="savePromotion(promotion.id)">Save</button>
            <button @click="cancelPromotion(promotion.id)">Cancel</button>
          </div>
        </li>
      </ul>
    </section>

    <!-- Categories Section -->
    <section class="box">
      <h2>Categories</h2>
      <div class="controls">
        <button @click="addCategory">Add Category</button>
      </div>
      <ul class="list">
        <!-- Iterate over categories -->
        <li v-for="(category, index) in categories" :key="index">
          <!-- Display existing category -->
          <div v-if="!category.isNew">
            <strong>{{ category.name }}</strong>: {{ category.description }}
            <button @click="deleteCategory(index)">Delete</button>
          </div>
          <!-- Add or edit a category -->
          <div v-else>
            <input
              type="text"
              v-model="category.tempName"
              placeholder="Enter category name"
            />
            <input
              type="text"
              v-model="category.tempDescription"
              placeholder="Enter category description"
            />
            <button @click="saveCategory(index)">Save</button>
            <button @click="cancelCategory(index)">Cancel</button>
          </div>
        </li>
      </ul>
    </section>

    <!-- Employees Section -->
    <section class="box">
      <h2>Employees</h2>
      <div class="controls">
        <button @click="addEmployee">Add Employee</button>
      </div>
      <ul class="list">
        <li v-for="employee in employees" :key="employee.id">
          <div v-if="!employee.isNew">
            {{ employee.name }}
            <button @click="deleteEmployee(employee.id)">Delete</button>
          </div>
          <div v-else>
            <input type="text" v-model="employee.tempName" placeholder="Enter employee name" />
            <button @click="saveEmployee(employee.id)">Save</button>
            <button @click="cancelEmployee(employee.id)">Cancel</button>
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080',
});

export default {
  data() {
    return {
      games: [],
      promotions: [],
      categories: [],
      employees: [],
      gameFilter: "",
    };
  },
  computed: {
    filteredGames() {
      return this.games.filter((game) =>
        game.name && game.name.toLowerCase().includes(this.gameFilter.toLowerCase())
      );
    },
  },
  mounted() {
    this.fetchGames();
    this.fetchCategories();
  },
  methods: {
    async fetchGames() {
      try {
        const response = await axios.get('/games', {
          params: { loggedInUsername: 'owner' },
        });
        this.games = response.data.games;
      } catch (error) {
        console.error('Error fetching games:', error);
      }
    },
    async fetchCategories() {
      try {
        // Fetch categories from the backend
        const categoriesResponse = await axiosClient.get('/categories', {
          params: { loggedInUsername: 'owner' },
        });

        //console.log(categoriesResponse.data);
        

        // Map the response to the categories array
        this.categories = categoriesResponse.data.gameCategories.map((category) => ({
          name: category.name,
          description: category.description,
          isNew: false, // Add a flag to track editable state
        }));

        console.log(this.categories);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    },
    async addGame() {
      const newGame = {
        tempName: "",
        isNew: true,
      };
      this.games.push(newGame);
    },
    async saveGame(id) {
      const game = this.games.find((game) => game.id === id || game.isNew);
      if (game && game.tempName.trim()) {
        try {
          if (game.isNew) {
            const response = await axios.post('/games',
              {
                title: game.tempName,
              },
              { params: { loggedInUsername: 'owner' } }
            );
            Object.assign(game, response.data);
            game.isNew = false;
          } else {
            await axios.put(`/games/${id}`,
              {
                title: game.tempName,
              },
              { params: { loggedInUsername: 'owner' } }
            );
            game.name = game.tempName;
          }
          game.tempName = "";
        } catch (error) {
          console.error('Error saving game:', error);
        }
      } else {
        alert("Game name cannot be empty");
      }
    },
    async deleteGame(id) {
      try {
        await axios.delete(`/games/${id}`, {
          params: { loggedInUsername: 'owner' },
        });
        this.games = this.games.filter((game) => game.id !== id);
      } catch (error) {
        console.error('Error deleting game:', error);
      }
    },
    async cancelGame(id) {
      this.games = this.games.filter((game) => game.id !== id || game.isNew);
    },
  },
};
</script>

<style>
#dashboard {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.box {
  border: 1px solid #ccc;
  padding: 20px;
  border-radius: 5px;
  flex: 1 1 calc(50% - 40px);
}
.controls {
  margin-bottom: 10px;
}
.list {
  max-height: 200px;
  overflow-y: auto;
  list-style: none;
  padding: 0;
}
.list li {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  padding: 5px;
  border-bottom: 1px solid #eee;
}
</style>
