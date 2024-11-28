<script setup>
import { RouterLink, RouterView} from 'vue-router';
import {session} from './session.js';
</script>

<template>
  <div id="app">
    <header>
      <nav class="navbar">
        <div class="nav-container">
          <RouterLink to="/" class="nav-logo">Logo</RouterLink>
          <div class="nav-links">
            <RouterLink to="/" class="nav-item">Home</RouterLink> 
            <RouterLink to="/browse" class="nav-item">Browse</RouterLink>
            <RouterLink to="/login" class="nav-item" v-if="session.permissionLevel==0">Login/Register</RouterLink>
            <button class="nav-item" v-if="session.permissionLevel!=0" @click="session.logout" style="background: none; border: none; cursor: pointer;">Logout</button>
            <RouterLink to="/account" class="nav-item" v-if="session.permissionLevel!=0">Account</RouterLink>
            <RouterLink to="/cart" class="nav-item" v-if="session.permissionLevel==1">Cart</RouterLink>
            <RouterLink to="/wishlist" class="nav-item" v-if="session.permissionLevel==1">Wishlist</RouterLink>
            <RouterLink to="/checkout" class="nav-item" v-if="session.permissionLevel==1">Checkout</RouterLink>
            <RouterLink to="/owner-dashboard" class="nav-item" v-if="session.permissionLevel==3">Owner Dashboard</RouterLink>
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
  }
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
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-logo {
  color: #fff;
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 1rem;
}

.nav-item {
  color: #fff;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.router-link-exact-active.nav-item {
  background-color: rgba(255, 255, 255, 0.3);
}

main {
  flex: 1;
  padding: 0;
  margin: 0 auto;
  width: 100%;
}

</style>
