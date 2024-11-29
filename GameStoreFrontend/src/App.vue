<script setup>
import { RouterLink, RouterView} from 'vue-router';
import {session} from './session.js';
</script>

<template>
  <div id="app">
    <header>
      <nav class="navbar">
        <div class="nav-container">
            <RouterLink to="/" class="nav-logo">
            <img src="@/assets/navlogo3.png" alt="Logo" class="logo-image" />
            </RouterLink>
          <div class="nav-links">
            <RouterLink to="/" class="nav-item">Home</RouterLink> 
            <RouterLink to="/browse" class="nav-item">Browse</RouterLink>
            <RouterLink to="/cart" class="nav-item" v-if="session.permissionLevel==1">Cart</RouterLink>
            <RouterLink to="/wishlist" class="nav-item" v-if="session.permissionLevel==1">Wishlist</RouterLink>
            <RouterLink to="/owner-dashboard" class="nav-item" v-if="session.permissionLevel==3">Owner Dashboard</RouterLink>
            <RouterLink to="/account" class="nav-item" v-if="session.permissionLevel!=0">Account</RouterLink>
            <RouterLink to="/login" class="nav-item" v-if="session.permissionLevel==0">Login/Register</RouterLink>
            <button class="nav-item" v-if="session.permissionLevel != 0" @click="handleLogout">Logout</button>
          </div>
        </div>
      </nav>
    </header>

    <main>
      <RouterView />
    </main>
  </div>
</template>

<script>
export default {
  name: "App",
  mounted() {
    // Set default session storage variables if not already set
     sessionStorage.setItem("loggedInUsername", "guest");
      sessionStorage.setItem("permissionLevel", 0);
  },
  methods: {
    handleLogout() {
      session.logout();
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  width: 100vw;
}

header {
  width: 100%;
  background-color: #000000; 
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar {
  max-width: 1200px; 
  margin: 0 auto;
  padding: 0.5rem 1rem;
  margin-left: 10%; 
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.nav-logo {
  display: flex;
  align-items: center;
  /* Move the logo further to the left */
  margin-left: -85px;
}

.nav-links {
  display: flex;
  gap: 1rem;
}

.nav-item {
  color: #fff;
  text-decoration: none;
  padding: 10px 15px;
  border-radius: 8px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.nav-item:hover {
  background-color: rgba(147, 81, 247, 0.2);
  transform: scale(1.05);
}

.nav-item:active {
  background-color: rgba(147, 81, 247, 0.3);
  transform: scale(0.98);
}

.nav-item:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5);
}

.router-link-exact-active.nav-item {
  background-color: rgba(147, 81, 247, 0.3);
}

main {
  flex: 1;
  padding: 0;
  margin: 0 auto;
  width: 100%;
}

.logo-image {
  height: 35px;
  width: auto;
  display: block;
}

button.nav-item {
  all: unset; 
  color: #fff;
  cursor: pointer;
  padding: 10px 15px;
  border-radius: 8px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button.nav-item:hover {
  background-color: rgba(147, 81, 247, 0.2);
  transform: scale(1.05);
}

button.nav-item:active {
  background-color: rgba(147, 81, 247, 0.3);
  transform: scale(0.98);
}

button.nav-item:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(147, 81, 247, 0.5);
}
</style>

