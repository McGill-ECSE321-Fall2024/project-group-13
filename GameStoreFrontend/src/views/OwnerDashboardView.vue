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
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;" >
            <h2 style="margin: 0;">Games</h2>
            <select v-model="selectedPendingArchiveGame" @change="filterByPendingArchive" style="margin-left: auto; margin-right: 10px; width:200px; height:40px; margin-bottom: 0px; " >
              <option value="" disabled selected hidden>Archive Request...</option>
              <option value="">No Filter</option> <!-- Option to show all games -->
              <option v-for="game in pendingArchiveGames" :value="game.id" :key="game.id">
                {{ game.title }}
              </option>
            </select>
            <button class="action__btn" @click="toggleAddGame">Add Game</button>
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
              <select v-model="newGame.parentalRating" id="game-rating">
                <option value="All">All</option>
                <option value="10+">10+</option>
                <option value="14+">14+</option>
                <option value="18+">18+</option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-status">Status:</label>
              <select v-model="newGame.status" id="game-status">
                <option value="Visible">Visible</option>
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

            <div class="button-container">
              <button class="action__btn" @click="saveGame">Save</button>
              <button class="action__btn" @click="toggleAddGame">Cancel</button>
            </div>
          </div>

          <!-- Conditional Fields for Updating Game -->
          <div
            v-if="isUpdatingGame"
            style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;"
          >
            <div style="margin-bottom: 10px;">
              <label for="game-title">Game Title:</label>
              <input v-model="currentGame.title" id="game-title" type="text" required />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-description">Description:</label>
              <textarea v-model="currentGame.description" id="game-description"></textarea>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-img">Image URL:</label>
              <input v-model="currentGame.img" id="game-img" type="text" required />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-stock">Stock:</label>
              <input v-model="currentGame.stock" id="game-stock" type="number" min="0" required />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-price">Price:</label>
              <input v-model="currentGame.price" id="game-price" type="number" min="0" step="0.01" required />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-rating">Parental Rating:</label>
              <select v-model="currentGame.parentalRating" id="game-rating" required>
                <option value="All">All</option>
                <option value="10+">10+</option>
                <option value="14+">14+</option>
                <option value="18+">18+</option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-status">Status:</label>
              <select v-model="currentGame.status" id="game-status" required>
                <option value="Visible">Visible</option>
                <option value="Unavailable">Unavailable</option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="game-category">Category:</label>
              <select v-model="currentGame.categoryId" id="game-category" required>
                <option v-for="category in categories" :value="category.id" :key="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>

            <div class="button-container">
              <button class="action__btn" @click="saveUpdatedGame">Save</button>
              <button class="action__btn" @click="resetUpdateGame">Cancel</button>
            </div>
          </div>

          <!-- List of Games -->
          <ul class="list" style="list-style: none; padding: 0; margin: 0;">
            <li
              v-for="game in games"
              :key="game.id"
              :style="getGameStyle(game.status)"
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
              </div>
              <!-- Delete Button -->
              <div style="display: flex; gap: 10px; justify-content: flex-end;">
                <button class="action__btn" @click="updateGame(game)">Update</button>
                <button class="delete__btn" @click="deleteGame(game.id)">Archive</button>
              </div>
            </li>
          </ul>
        </section>

        <!-- Promotions Section -->
        <section class="box">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h2 style="margin: 0;">Promotions</h2>
            <button class="action__btn" @click="toggleAddPromotion">Add Promotion</button>
          </div>

          <!-- Conditional Fields for Adding/Updating Promotion -->
          <div
            v-if="isAddingPromotion || isUpdatingPromotion"
            style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;"
          >
            <div style="margin-bottom: 10px;">
              <label for="game-select">Select Game:</label>
              <select v-model="currentPromotion.gameID" id="game-select">
                <option v-for="game in games" :value="game.id" :key="game.id">
                  {{ game.title }}
                </option>
              </select>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="title">Title:</label>
              <input v-model="currentPromotion.title" id="title" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="description">Description:</label>
              <textarea v-model="currentPromotion.description" id="description"></textarea>
            </div>

            <div style="margin-bottom: 10px;">
              <label for="percentage">Percentage (%):</label>
              <input v-model="currentPromotion.percentage" id="percentage" type="number" min="1" max="100" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="start-date">Start Date:</label>
              <input v-model="currentPromotion.startDate" id="start-date" type="date" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="end-date">End Date:</label>
              <input v-model="currentPromotion.endDate" id="end-date" type="date" />
            </div>

            <div class="button-container">
              <button class="action__btn" @click="isUpdatingPromotion ? saveUpdatedPromotion() : savePromotion()">
                Save
              </button>
              <button class="action__btn" @click="toggleAddPromotion">Cancel</button>
            </div>
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
              <div style="display: flex; gap: 10px;  justify-content: flex-end;">
                <button class="action__btn" @click="updatePromotion(promotion)">Update</button>
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

            <div class="button-container">
              <button class="action__btn" @click="saveCategory">Save</button>
              <button class="action__btn" @click="toggleAddCategory">Cancel</button>
            </div>
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

            <div class="button-container">
              <button class="action__btn" @click="saveEmployee">Save</button>
              <button class="action__btn" @click="toggleAddEmployee">Cancel</button>
            </div>
          </div>

          <!-- Conditional Fields for Updating Employee -->
          <div
            v-if="isUpdatingEmployee"
            style="margin-bottom: 15px; padding: 10px; border: 1px solid #ccc; border-radius: 5px;"
          >
            <div style="margin-bottom: 10px;">
              <label for="username">Username:</label>
              <input v-model="currentEmployee.username" id="username" type="text" disabled />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="name">Name:</label>
              <input v-model="currentEmployee.name" id="name" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="email">Email:</label>
              <input v-model="currentEmployee.email" id="email" type="email" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="phone-number">Phone Number:</label>
              <input v-model="currentEmployee.phoneNumber" id="phone-number" type="text" />
            </div>

            <div style="margin-bottom: 10px;">
              <label for="password">Password (required, min 8 characters):</label>
              <input v-model="currentEmployee.password" id="password" type="password" required />
            </div>


            <div style="margin-bottom: 10px;">
              <label for="is-active">Active:</label>
              <input v-model="currentEmployee.isActive" id="is-active" type="checkbox" />
            </div>

            <div class="button-container">
              <button class="action__btn" @click="saveUpdatedEmployee">Save</button>
              <button class="action__btn" @click="resetUpdateEmployee">Cancel</button>
            </div>
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
                <strong>{{ employee.name }}</strong><br />
                <span>Username: {{ employee.username }}</span><br />
                <span>Email: {{ employee.email }}</span><br />
                <span>Phone: {{ employee.phoneNumber }}</span><br />
                <span>Permission Level: {{ employee.permissionLevel }}</span><br />
                <span>Status: {{ employee.isActive ? 'Active' : 'Inactive' }}</span>
              </div>
              <!-- Delete Button -->
              <div style="display: flex; gap: 10px;  justify-content: flex-end;">
                <button class="action__btn" @click="updateEmployee(employee)">Update</button>
                <button class="delete__btn" @click="deleteEmployee(employee.username)">Archive</button>
              </div>
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
        status: "Visible", // Default status
        categoryId: null, // ID of the selected category
      },
      isUpdatingPromotion: false, // Tracks whether the "Update Promotion" form is visible
      currentPromotion: {
        id: null,
        gameID: null,
        title: "",
        description: "",
        percentage: null,
        startDate: "",
        endDate: "",
      },
      pendingArchiveGames: [], // Stores games with "PendingArchive" status
      selectedPendingArchiveGame: "", // Stores the selected game ID
      isUpdatingEmployee: false, // Tracks whether the "Update Employee" form is visible
      currentEmployee: {
        username: "",
        name: "",
        email: "",
        phoneNumber: "",
        isActive: true,
      },
      isUpdatingGame: false, // Tracks whether the "Update Game" form is visible
      currentGame: {
        id: null,
        title: "",
        description: "",
        img: "",
        stock: 0,
        price: 0.0,
        parentalRating: "",
        status: "Visible", // Default value
        categoryId: null, // Default category
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
        this.pendingArchiveGames = this.games.filter(
          (game) => game.status === "PendingArchive"
        );
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
          id: category.id,
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
            id: promotion.promotionID,
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
      if (this.isUpdatingPromotion) {
        this.isUpdatingPromotion = false;
        this.isAddingPromotion = false;
        this.resetCurrentPromotion();
      }
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
        status: "Visible",
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
    updatePromotion(promotion) {
      this.isAddingPromotion = false;
      this.isUpdatingPromotion = true;
        // Explicitly map the promotion data, including the id
      this.currentPromotion = {
        id: promotion.id, // Ensure the id is set here
        gameID: promotion.gameID || null,
        title: promotion.title || "",
        description: promotion.description || "",
        percentage: promotion.percentage || null,
        startDate: promotion.startDate || "",
        endDate: promotion.endDate || "",
      };
    },
    async saveUpdatedPromotion() {
      console.log("Updating Promotion Details:", this.currentPromotion);

      if (
        !this.currentPromotion.gameID ||
        !this.currentPromotion.title ||
        !this.currentPromotion.description ||
        !this.currentPromotion.percentage ||
        !this.currentPromotion.startDate ||
        !this.currentPromotion.endDate
      ) {
        alert("Please fill in all fields.");
        return;
      }

      try {
        const response = await axiosClient.put(
          `/games/promotions/${this.currentPromotion.id}`,
          {
            title: this.currentPromotion.title,
            description: this.currentPromotion.description,
            percentage: this.currentPromotion.percentage,
            startDate: this.currentPromotion.startDate,
            endDate: this.currentPromotion.endDate,
            gameID: this.currentPromotion.gameID,
          },
          {
            params: { loggedInUsername: "owner" },
          }
        );

        console.log("Update Promotion Response:", response.data);

        // Update the promotion in the list
        const updatedIndex = this.promotions.findIndex(
          (promo) => promo.id === this.currentPromotion.id
        );
        if (updatedIndex !== -1) {
          this.promotions[updatedIndex] = { ...this.currentPromotion };
        }

        // Reset and hide form
        this.resetCurrentPromotion();
        this.isUpdatingPromotion = false;

        alert("Promotion updated successfully!");
      } catch (error) {
        console.error("Error updating promotion:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while updating the promotion."
        );
      }
    },
    resetCurrentPromotion() {
      this.currentPromotion = {
        id: null,
        gameID: null,
        title: "",
        description: "",
        percentage: null,
        startDate: "",
        endDate: "",
      };
    },
    updateEmployee(employee) {
      this.isAddingEmployee = false;
      this.isUpdatingEmployee = true;
      this.currentEmployee = { ...employee }; // Clone the employee object
    },

    async saveUpdatedEmployee() {
      console.log("Updating Employee Details:", this.currentEmployee);

      if (!this.currentEmployee.username || !this.currentEmployee.name || !this.currentEmployee.email) {
        alert("Please fill in all required fields.");
        return;
      }

      try {
        const response = await axiosClient.put(
          `/employees/${this.currentEmployee.username}`,
          {
            name: this.currentEmployee.name,
            email: this.currentEmployee.email,
            phoneNumber: this.currentEmployee.phoneNumber,
            password: this.currentEmployee.password,
            isActive: this.currentEmployee.isActive,
          },
          {
            params: { 
              loggedInUsername: "owner",
              isUpdate: true, // This is for updating an existing employee
              },
          }
        );

        console.log("Update Employee Response:", response.data);

        // Update the employee in the list
        const updatedIndex = this.employees.findIndex(
          (emp) => emp.username === this.currentEmployee.username
        );
        if (updatedIndex !== -1) {
          this.employees[updatedIndex] = { ...this.currentEmployee };
        }

        // Reset and hide form
        this.resetUpdateEmployee();
        this.isUpdatingEmployee = false;

        alert("Employee updated successfully!");
      } catch (error) {
        console.error("Error updating employee:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while updating the employee."
        );
      }
    },

    resetUpdateEmployee() {
      this.currentEmployee = {
        username: "",
        name: "",
        email: "",
        phoneNumber: "",
        isActive: true,
        password: "",
      };
      this.isUpdatingEmployee = false;
    },

    async saveUpdatedGame() {
      console.log("Updating Game Details:", this.currentGame);

      // Validate required fields
      if (
        !this.currentGame.title ||
        !this.currentGame.description ||
        !this.currentGame.img ||
        this.currentGame.stock < 0 ||
        this.currentGame.price < 0 ||
        !this.currentGame.parentalRating ||
        !this.currentGame.status ||
        !this.currentGame.categoryId
      ) {
        alert("All fields are required, and stock/price must be valid.");
        return;
      }

      try {
        const response = await axiosClient.put(
          `/games/${this.currentGame.id}`,
          {
            title: this.currentGame.title,
            description: this.currentGame.description,
            img: this.currentGame.img,
            stock: this.currentGame.stock,
            price: this.currentGame.price,
            parentalRating: this.currentGame.parentalRating,
            status: this.currentGame.status,
            categoryId: this.currentGame.categoryId,
          },
          {
            params: {
              loggedInUsername: "owner", // Replace with actual username if dynamic
              isUpdate: true, // Required parameter for updates
            },
          }
        );

        console.log("Update Game Response:", response.data);

        // Update the game in the list
        const updatedIndex = this.games.findIndex(
          (game) => game.id === this.currentGame.id
        );
        if (updatedIndex !== -1) {
          this.games[updatedIndex] = { ...this.currentGame };
        }

        // Reset and hide form
        this.resetUpdateGame();
        this.isUpdatingGame = false;

        alert("Game updated successfully!");
      } catch (error) {
        console.error("Error updating game:", error);
        alert(
          error.response?.data?.message ||
            "An error occurred while updating the game."
        );
      }
    },

    updateGame(game) {
      this.isAddingGame = false;
      this.isUpdatingGame = true;
      this.currentGame = { ...game }; // Clone the game object
    },

    resetUpdateGame() {
      this.currentGame = {
        id: null,
        title: "",
        description: "",
        img: "",
        stock: 0,
        price: 0.0,
        parentalRating: "",
        status: "Visible",
        categoryId: null,
      };
      this.isUpdatingGame = false;
    },


    async deleteGame(id) {
      try {
        const response = await axiosClient.delete(`/games/${id}`, {
          params: { loggedInUsername: "owner" },
        });

        const deletedGame = this.games.find((game) => game.id === id);
        // Update the games list by removing the deleted game
        this.games = this.games.filter((game) => game.id !== id);
        deletedGame.status = "Archived";
        this.games.push(deletedGame);
        console.log("Game deleted successfully:", response.data);
        alert("Game deleted successfully!");
      } catch (error) {
        alert("These games cannot be archived.")
      }
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
    filterByPendingArchive() {
    if (this.selectedPendingArchiveGame) {
      this.games = this.games.filter(
        (game) => game.id === this.selectedPendingArchiveGame
      );
    } else {
      this.fetchGames(); // Reset to fetch all games
    }
    },
    getGameStyle(status) {
      switch (status) {
        case 'Archived':
          return {
            backgroundColor: 'rgba(255, 0, 0, 0.1)', // Light red tint
            color: 'grey' // Greyed-out text
          };
        case 'PendingArchive':
          return {
            backgroundColor: 'rgba(0, 0, 255, 0.1)', // Light blue tint
            color: 'grey' // Greyed-out text
          };
        default:
          return {}; // No special styles for other statuses
      }
    }

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
  display: flex;
  align-items: bottom; /* Align items vertically */
  gap: 10px; /* Add spacing between dropdown and button */
}

.controls select {
  height: 30px; /* Match the button's height */
  padding: 0 10px; /* Adjust padding for better spacing */
  font-size: 14px; /* Match the font size with the button */
  color: #fff;
  background-color: #313134;
  border: 2px solid transparent;
  border-radius: 7px;
  outline: none;
  transition: border 0.2s ease, background-color 0.2s ease;
  display: inline-block; /* Ensure proper layout */
  box-sizing: border-box; /* Include padding in height calculation */
}

.controls select:focus {
  border: 2px solid #a970ff; /* Add focus styling */
  background-color: #0e0e10;
}

.list {
  max-height: 400px;
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
  margin-bottom: 10px; /* Optional: spacing between items */
  background-color: #2e2e2e; /* Optional: background for better visibility */
}

.list li div {
  flex-grow: 1; /* Allow text to take up available space */
  padding: 0 10px; /* Add spacing around text */
  border-radius: 5px;
}

.list button {
  margin-left: 10px; /* Add spacing between button and text if needed */
}

.list li div strong {
  font-size: 18px; /* Adjust the size as needed */
  font-weight: bold;
  color: #ffffff;
}

.action__btn {
  background-color: #a970ff;
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

.delete__btn {
  background-color: #ff0000;
  color: #ffffff;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  margin: 0 8px;
  cursor: pointer;
  font-size: 15px;
  transition: background-color 0.3s ease, transform 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action__btn:hover,
.delete__btn:hover {
  background-color: #a970ff;
  transform: scale(1.05);
}

.action__btn:active,
.delete__btn:active {
  background-color: #8c3de3;
  transform: scale(0.98);
}

.action__btn:focus,
.delete__btn:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5);
}

/* Input Fields */
input[type="text"],
input[type="number"],
input[type="date"],
input[type="email"],
textarea,
select {
  width: 80%;
  padding: 10px;
  font-size: 14px;
  color: #fff;
  background-color: #313134;
  border: 2px solid transparent;
  border-radius: 7px;
  outline: none;
  margin-bottom: 10px;
  transition: border 0.2s ease, background-color 0.2s ease;
}

input[type="text"]:hover,
input[type="number"]:hover,
input[type="date"]:hover,
input[type="email"]:hover,
textarea:hover,
select:hover {
  border: 2px solid rgba(255, 255, 255, 0.16);
}

input[type="text"]:focus,
input[type="number"]:focus,
input[type="date"]:focus,
input[type="email"]:focus,
textarea:focus,
select:focus {
  border: 2px solid #a970ff;
  background-color: #0e0e10;
}

/* Labels */
label {
  font-size: 14px;
  color: #fff;
  margin-bottom: 5px;
  display: block;
}

.button-container {
  display: flex;
  justify-content: space-between; /* Push items to opposite ends */
  width: 100%; /* Adjust as necessary */
}
</style>