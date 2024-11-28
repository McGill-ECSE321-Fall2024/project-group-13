<template>
  <div class="dashboard">
    <div class="dashboardFlex">
      <!-- Title -->
      <div class="titleWrapper">
        <h1>Owner Dashboard</h1>
      </div>

      <!-- Horizontal Bar -->
      <hr style="margin-bottom: 20px;" />

      <!-- Section Grid -->
      <div class="sectionGrid">

        <!-- Games Section -->
        <section class="box">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h2 style="margin: 0;">Games</h2>
            <button class="action__btn" @click="toggleAddGame">Add Game</button>
          </div>

          <div class="controls">
            <input type="text" placeholder="Filter games..." v-model="gameFilter" />
          </div>

          <!-- Conditional Fields for Adding Game -->
          <div v-if="isAddingGame" style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
            <div style="margin-bottom: 10px;">
              <label for="game-title">Game Title:</label>
              <input v-model="newGame.title" id="game-title" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-description">Description:</label>
              <textarea v-model="newGame.description" id="game-description"></textarea>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-img">Image URL:</label>
              <input v-model="newGame.img" id="game-img" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-stock">Stock:</label>
              <input v-model="newGame.stock" id="game-stock" type="number" min="0" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-price">Price:</label>
              <input v-model="newGame.price" id="game-price" type="number" min="0" step="0.01" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-rating">Parental Rating:</label>
              <input v-model="newGame.parentalRating" id="game-rating" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-status">Status:</label>
              <select v-model="newGame.status" id="game-status">
                <option value="Available">Available</option>
                <option value="Unavailable">Unavailable</option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-category">Category:</label>
              <select v-model="newGame.categoryId" id="game-category">
                <option v-for="category in categories" :value="category.id" :key="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>

            <button class="action__btn" @click="saveGame">Save</button>
          </div>

          <!-- List of Games -->
          <ul class="list" style="list-style: none; padding: 0; margin: 0;">
            <li
              v-for="game in games"
              :key="game.id"
              style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; padding: 5px 0;"
            >
              <!-- Game Details -->
              <div style="flex: 1; text-align: left;">
                <strong>{{ game.title }}</strong><br />
                <span>Description: {{ game.description }}</span><br />
                <span>Price: ${{ game.price.toFixed(2) }}</span><br />
                <span>Stock: {{ game.stock }}</span><br />
                <span>Parental Rating: {{ game.parentalRating }}</span><br />
                <span>Status: {{ game.status }}</span><br />
                <span>Category ID: {{ game.categoryId }}</span><br />
                <img :src="game.img" alt="Game Image" style="max-width: 150px; margin-top: 5px;" />
              </div>
              <!-- Delete Button -->
              <button class="action__btn" @click="deleteGame(game.id)" style="margin-left: 10px;">Delete</button>
            </li>
          </ul>
        </section>

        <!-- Promotions Section -->
        <section class="box">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h2 style="margin: 0;">Promotions</h2>
            <button class="action__btn" @click="toggleAddPromotion">Add Promotion</button>
          </div>

          <!-- Conditional Fields for Adding Promotion -->
          <div v-if="isAddingPromotion" style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
            <div style="margin-bottom: 10px;">
              <label for="game-select">Select Game:</label>
              <select v-model="newPromotion.gameID" id="game-select">
                <option v-for="game in games" :value="game.id" :key="game.id">
                  {{ game.title }}
                </option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="title">Title:</label>
              <input v-model="newPromotion.title" id="title" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="description">Description:</label>
              <textarea v-model="newPromotion.description" id="description"></textarea>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="percentage">Percentage (%):</label>
              <input v-model="newPromotion.percentage" id="percentage" type="number" min="1" max="100" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="start-date">Start Date:</label>
              <input v-model="newPromotion.startDate" id="start-date" type="date" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="end-date">End Date:</label>
              <input v-model="newPromotion.endDate" id="end-date" type="date" />
            </div>

            <button class="action__btn" @click="savePromotion">Save</button>
          </div>

          <!-- List of Promotions -->
          <ul class="list" style="list-style: none; padding: 0; margin: 0;">
            <li
              v-for="promotion in promotions"
              :key="promotion.id"
              style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; padding: 5px 0;"
            >
              <div style="flex: 1; text-align: left;">
                <strong>{{ promotion.title }}</strong><br />
                <span>Description: {{ promotion.description }}</span><br />
                <span>Percentage: {{ promotion.percentage }}%</span><br />
                <span>Start Date: {{ promotion.startDate }}</span><br />
                <span>End Date: {{ promotion.endDate }}</span>
              </div>
            </li>
          </ul>
        </section>


        <!-- Categories Section -->
        <section class="box">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h2 style="margin: 0;">Categories</h2>
            <button class="action__btn" @click="toggleAddCategory">Add Category</button>
          </div>

          <!-- Conditional Fields for Adding Category -->
          <div v-if="isAddingCategory" style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
            <div style="margin-bottom: 10px;">
              <label for="category-name">Category Name:</label>
              <input v-model="newCategory.name" id="category-name" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="category-description">Description:</label>
              <textarea v-model="newCategory.description" id="category-description"></textarea>
            </div>

            <button class="action__btn" @click="saveCategory">Save</button>
          </div>

          <!-- List of Categories -->
          <ul class="list" style="list-style: none; padding: 0; margin: 0;">
            <li
              v-for="(category, index) in categories"
              :key="index"
              style="display: flex; justify-content: space-between; align-items: flex-start; padding: 5px 0;"
            >
              <!-- Left-aligned name and description -->
              <div style="flex: 1; text-align: left;">
                <strong>{{ category.name }}</strong>: {{ category.description }}
              </div>
            </li>
          </ul>
        </section>




        <!-- Employees Section -->
        <section class="box">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h2 style="margin: 0;">Employees</h2>
            <button class="action__btn" @click="toggleAddEmployee">Add Employee</button>
          </div>

          <!-- Conditional Fields for Adding Employee -->
          <div v-if="isAddingEmployee" style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
            <div style="margin-bottom: 10px;">
              <label for="username">Username:</label>
              <input v-model="newEmployee.username" id="username" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="name">Name:</label>
              <input v-model="newEmployee.name" id="name" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="email">Email:</label>
              <input v-model="newEmployee.email" id="email" type="email" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="password">Password:</label>
              <input v-model="newEmployee.password" id="password" type="password" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="phone-number">Phone Number:</label>
              <input v-model="newEmployee.phoneNumber" id="phone-number" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="is-active">Active:</label>
              <input v-model="newEmployee.isActive" id="is-active" type="checkbox" />
            </div>

            <button class="action__btn" @click="saveEmployee">Save</button>
          </div>

          <!-- List of Employees -->
          <ul class="list" style="list-style: none; padding: 0; margin: 0;">
            <li
              v-for="employee in activeEmployees"
              :key="employee.username"
              style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; padding: 5px 0;"
            >
              <!-- Employee Details -->
              <div style="flex: 1; text-align: left;">
                <strong>Name:</strong> {{ employee.name }}<br />
                <strong>Username:</strong> {{ employee.username }}<br />
                <strong>Email:</strong> {{ employee.email }}<br />
                <strong>Phone:</strong> {{ employee.phoneNumber }}<br />
                <strong>Permission Level:</strong> {{ employee.permissionLevel }}<br />
                <strong>Status:</strong> {{ employee.isActive ? 'Active' : 'Inactive' }}
              </div>
              <!-- Delete Button -->
              <button class="action__btn" @click="deleteEmployee(employee.username)" style="margin-left: 10px;">Delete</button>
            </li>
          </ul>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
});
export default {
  data() {
    return {
      games: [],
      promotions: [],
      categories: [],
      employees: [],
      gameFilter: "",
      isAddingPromotion: false, // Tracks whether the "Add Promotion" form is visible
      newPromotion: {
        gameID: null,
        title: "",
        description: "",
        percentage: null,
        startDate: "",
        endDate: "",
      },
      isAddingEmployee: false, // Tracks whether the "Add Employee" form is visible
      newEmployee: {
        username: "",
        name: "",
        email: "",
        password: "",
        phoneNumber: "",
        permissionLevel: 1, // Default permission level
        isActive: true, // Default status is active
      },
      isAddingCategory: false, // Tracks whether the "Add Category" form is visible
      newCategory: {
        name: "",
        description: "",
      },
      isAddingGame: false, // Tracks whether the "Add Game" form is visible
      newGame: {
        title: "",
        description: "",
        img: "",
        stock: 0,
        price: 0.0,
        parentalRating: "",
        status: "Available", // Default status
        categoryId: null, // ID of the selected category
      },
    };
  },
  computed: {
    activeEmployees() {
      return this.employees.filter((employee) => employee.isActive);
    },
  },
  mounted() {
    this.fetchGames();
    this.fetchCategories();
    this.fetchPromotions();
    this.fetchEmployees();
  },
  methods: {
    async fetchGames() {
      try {
        const response = await axiosClient.get("/games", {
          params: { loggedInUsername: "owner" },
        });
        console.log("Games response:", response.data.games); // Debugging
        this.games = response.data.games.map((game) => ({
          id: game.gameID,
          title: game.title,
          description: game.description,
          img: game.img,
          stock: game.stock,
          price: game.price,
          parentalRating: game.parentalRating,
          status: game.status,
          categoryId: game.categoryId,
        }));
      } catch (error) {
        console.error("Error fetching games:", error);
      }
    },
    async fetchCategories() {
      try {
        const categoriesResponse = await axiosClient.get("/categories", {
          params: { loggedInUsername: "owner" },
        });
        this.categories = categoriesResponse.data.gameCategories.map((category) => ({
          id: category.categoryId,
          name: category.name,
          description: category.description,
          status: category.status,
          isNew: false,
        }));
        console.log("Fetched Categories:", this.categories); // Debugging
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    },
    async fetchPromotions() {
      try {
        const response = await axiosClient.get("/games/promotions", {
          params: { loggedInUsername: "owner" },
        });

        const currentDate = new Date().toISOString().split("T")[0]; // Current date in YYYY-MM-DD

        // Filter and map promotions
        this.promotions = response.data.promotions
          .filter((promotion) => {
            console.log("Comparing promotion end date:", promotion.endDate, "with current date:", currentDate); // Debugging
            return promotion.endDate >= currentDate; // Compare as strings
          })
          .map((promotion) => ({
            title: promotion.title,
            description: promotion.description,
            percentage: promotion.percentage,
            startDate: promotion.startDate.split("T")[0], // Keep only the date
            endDate: promotion.endDate.split("T")[0], // Keep only the date
          }));
      } catch (error) {
        console.error("Error fetching promotions:", error);
      }
    },
    async fetchEmployees() {
      try {
        const response = await axiosClient.get("/employees", {
          params: { loggedInUsername: "owner" },
        });
        this.employees = response.data.employees.map((employee) => ({
          username: employee.username,
          name: employee.name,
          email: employee.email,
          phoneNumber: employee.phoneNumber,
          permissionLevel: employee.permissionLevel,
          isActive: employee.isActive,
        }));
        console.log("Fetched Employees:", this.employees); // Debugging
      } catch (error) {
        console.error("Error fetching employees:", error);
      }
    },
    toggleAddPromotion() {
      this.isAddingPromotion = !this.isAddingPromotion;
    },
    async savePromotion() {
      console.log("Game ID:", this.newPromotion.gameID);
      console.log("Title:", this.newPromotion.title);
      console.log("Description:", this.newPromotion.description);
      console.log("Percentage:", this.newPromotion.percentage);
      console.log("Start Date:", this.newPromotion.startDate);
      console.log("End Date:", this.newPromotion.endDate);
      if (
        !this.newPromotion.gameID ||
        !this.newPromotion.title ||
        !this.newPromotion.description ||
        !this.newPromotion.percentage ||
        !this.newPromotion.startDate ||
        !this.newPromotion.endDate
      ) {
        alert("Please fill in all fields.");
        return;
      }

      try {
        // Step 1: Create Promotion
        const createResponse = await axiosClient.post(
          "/games/promotions",
          {
            title: this.newPromotion.title,
            description: this.newPromotion.description,
            percentage: this.newPromotion.percentage,
            startDate: this.newPromotion.startDate,
            endDate: this.newPromotion.endDate,
          },
          {
            params: { loggedInUsername: "owner" },
          }
        );
        console.log("Create Promotion Response:", createResponse.data);
        const newPromotionID = createResponse.data.promotionID;

        // Step 2: Attach Promotion to Game
        const attachResponse = await axiosClient.post(
          `/games/${this.newPromotion.gameID}/promotions/${newPromotionID}`,
          null,
          {
            params: { loggedInUsername: "owner" },
          }
        );

        console.log("Attach Promotion Response:", attachResponse.data);

        // Add to promotions list
        this.promotions.push({
          ...attachResponse.data,
          startDate: attachResponse.data.startDate.split("T")[0],
          endDate: attachResponse.data.endDate.split("T")[0],
        });

        // Reset and hide form
        this.resetNewPromotion();
        this.isAddingPromotion = false;

        alert("Promotion added successfully!");
      } catch (error) {
        console.error("Error adding promotion:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while adding the promotion."
        );
      }
    },
    toggleAddEmployee() {
      this.isAddingEmployee = !this.isAddingEmployee;
    },
    async saveEmployee() {
      console.log("New Employee Details:", this.newEmployee);

      if (
        !this.newEmployee.username ||
        !this.newEmployee.name ||
        !this.newEmployee.email ||
        !this.newEmployee.password ||
        !this.newEmployee.phoneNumber
      ) {
        alert("Please fill in all fields.");
        return;
      }

      try {
        const response = await axiosClient.put(
          `/employees/${this.newEmployee.username}`,
          {
            name: this.newEmployee.name,
            email: this.newEmployee.email,
            password: this.newEmployee.password,
            phoneNumber: this.newEmployee.phoneNumber,
            isActive: this.newEmployee.isActive,
          },
          {
            params: {
              loggedInUsername: "owner", // Adjust according to logged-in user
              isUpdate: false, // This is for creating a new employee
            },
          }
        );

        console.log("Create Employee Response:", response.data);

        // Add the new employee to the active list
        this.employees.push({
          username: response.data.username,
          name: response.data.name,
          email: response.data.email,
          phoneNumber: response.data.phoneNumber,
          permissionLevel: response.data.permissionLevel,
          isActive: response.data.isActive,
        });

        // Reset and hide form
        this.resetNewEmployee();
        this.isAddingEmployee = false;

        alert("Employee added successfully!");
      } catch (error) {
        console.error("Error adding employee:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while adding the employee."
        );
      }
    },
    toggleAddCategory() {
      this.isAddingCategory = !this.isAddingCategory;
    },
    async saveCategory() {
      console.log("New Category Details:", this.newCategory);

      if (!this.newCategory.name || !this.newCategory.description) {
        alert("Please fill in all fields.");
        return;
      }

      try {
        const response = await axiosClient.post(
          "/categories",
          {
            name: this.newCategory.name,
            description: this.newCategory.description,
          },
          {
            params: {
              loggedInUsername: "owner", // Adjust based on the logged-in user
            },
          }
        );

        console.log("Create Category Response:", response.data);

        // Add the new category to the list
        this.categories.push({
          name: response.data.name,
          description: response.data.description,
        });

        // Reset and hide form
        this.resetNewCategory();
        this.isAddingCategory = false;

        alert("Category added successfully!");
      } catch (error) {
        console.error("Error adding category:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while adding the category."
        );
      }
    },
    toggleAddGame() {
      this.isAddingGame = !this.isAddingGame;
    },
    async saveGame() {
      console.log("New Game Details:", this.newGame);

      console.log("Category ID:", this.newGame.categoryId);
      console.log("Title:", this.newGame.title);
      
      if (
        !this.newGame.title ||
        !this.newGame.description ||
        !this.newGame.img ||
        !this.newGame.stock ||
        !this.newGame.price ||
        !this.newGame.parentalRating ||
        !this.newGame.status ||
        !this.newGame.categoryId
      ) {
        alert("Please fill in all fields.");
        return;
      }


      try {
        const response = await axiosClient.post(
          "/games",
          {
            title: this.newGame.title,
            description: this.newGame.description,
            img: this.newGame.img,
            stock: this.newGame.stock,
            price: this.newGame.price,
            parentalRating: this.newGame.parentalRating,
            status: this.newGame.status,
            categoryId: this.newGame.categoryId,
          },
          {
            params: {
              loggedInUsername: "owner", // Adjust based on the logged-in user
            },
          }
        );

        console.log("Create Game Response:", response.data);

        // Add the new game to the list
        this.games.push({
          id: response.data.gameID,
          title: response.data.title,
          description: response.data.description,
          img: response.data.img,
          stock: response.data.stock,
          price: response.data.price,
          parentalRating: response.data.parentalRating,
          status: response.data.status,
          categoryId: response.data.categoryId,
        });

        // Reset and hide form
        this.resetNewGame();
        this.isAddingGame = false;

        alert("Game added successfully!");
      } catch (error) {
        console.error("Error adding game:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while adding the game."
        );
      }
    },
    resetNewGame() {
      this.newGame = {
        title: "",
        description: "",
        img: "",
        stock: 0,
        price: 0.0,
        parentalRating: "",
        status: "Available",
        categoryId: null,
      };
    },
    resetNewCategory() {
      this.newCategory = {
        name: "",
        description: "",
      };
    },
    resetNewEmployee() {
      this.newEmployee = {
        username: "",
        name: "",
        email: "",
        password: "",
        phoneNumber: "",
        permissionLevel: 1,
        isActive: true,
      };
    },
    resetNewPromotion() {
      this.newPromotion = {
        gameID: null,
        title: "",
        description: "",
        percentage: null,
        startDate: "",
        endDate: "",
      };
    },
    deleteGame(id) {
      axiosClient
        .delete(`/games/${id}`, {
          params: { loggedInUsername: "owner" },
        })
        .then(() => {
          this.games = this.games.filter((game) => game.id !== id);
        });
    },
    deleteEmployee(username) {
      axiosClient
        .delete(`/employees/${username}`, {
          params: { loggedInUsername: "owner" },
        })
        .then(() => {
          this.employees = this.employees.filter((employee) => employee.username !== username);
        })
        .catch((error) => {
          console.error("Error deleting employee:", error);
        });
    },
  },
};
</script>

<style scoped>
.dashboard {
  text-align: center;
  margin-top: 50px;
}

.dashboardFlex {
  display: flex;
  flex-direction: column;
  margin: 5%;
  margin-top: 100px;
  margin-bottom: 30px;
}

.titleWrapper {
  height: 50px;
  width: 100%;
  text-align: left;
}

.titleWrapper > h1 {
  color: white;
  font-size: 30px;
}

hr {
  border: 0;
  height: 1px;
  background: #333;
  background-image: linear-gradient(to right, #ccc, purple, #ccc);
  margin-top: 10px;
}

.sectionGrid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.box {
  border: 1px solid #ccc;
  background-color: #1e1e1e;
  border-radius: 10px;
  padding: 20px;
  color: white;
}

.controls {
  margin-bottom: 10px;
}

.list {
  max-height: 200px;
  overflow-y: auto;
}

.list::-webkit-scrollbar {
  width: 8px;
}

.list::-webkit-scrollbar-track {
  background: #1e1e1e;
}

.list::-webkit-scrollbar-thumb {
  background-color: #555;
  border-radius: 4px;
}

.list::-webkit-scrollbar-thumb:hover {
  background-color: #888;
}

.list li {
  display: flex; /* Align items in a row */
  justify-content: space-between; /* Space between text and button */
  align-items: center; /* Vertically center text and button */
  padding: 8px 0; /* Add spacing between items */
}

.list li div {
  flex-grow: 1; /* Allow text to take up available space */
}

.list button {
  margin-left: 10px; /* Add spacing between button and text if needed */
}

.action__btn {
  background-color: #9351f7;
  color: #ffffff;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  margin: 0 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease, transform 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action__btn:hover {
  background-color: #a970ff;
  transform: scale(1.05);
}

.action__btn:active {
  background-color: #8c3de3;
  transform: scale(0.98);
}

.action__btn:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5);
}

</style>