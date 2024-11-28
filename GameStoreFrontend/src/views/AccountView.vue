<template>
    
    <div class="tabs">
        <button 
            class="tab" 
            :class="{ active: activeTab === 'profile' }"
            @click="activeTab = 'profile'">
            User Profile</button>
        <button 
            class="tab"
            :class="{ active: activeTab === 'update' }"
            @click="activeTab = 'update'"
        >
        Update Information </button>
        <button
            class="tab"
            :class="{ active: activeTab === 'orders' }"
            @click="activeTab = 'orders'"
        >
        Order History </button>
    </div>

    <div v-if="activeTab === 'profile'" class="active" id="profile">
        <div class="profile-info">
        <p class="title">Profile</p>
            <div class="info">
                <label for="name">Name:</label>
            </div>
            <div class="info">
                <label for="card-number">Username:</label>
            </div>
            <div class="info">
                <label for="expiry-date">Email:</label>
            </div>
            <div class="info">
                <label for="cvv">Phone Number:</label>
            </div>
        </div>
    </div>

    <div v-if="activeTab === 'update'" class="active" id="update">
        <div class="address">
        <p class="title">Address</p>
        <form class="form">
            <div class="input">
                <label for="street">Street</label>
                <input type="text" name="street" id="street" placeholder="" required>
            </div>
            <div class="input">
                <label for="number">Street No.</label>
                <input type="number" name="number" id="number" placeholder="" step="1" required>
            </div>
            <div class="input">
                <label for="apartment-number">Apartment No.</label>
                <input type="number" name="apartment-number" id="apartment-number" step="1" placeholder="">
            </div>
            <div class="input">
                <label for="city">City</label>
                <input type="text" name="city" id="city" placeholder="" required>
            </div>
            <div class="input">
                <label for="state-province">State/Province</label>
                <input type="text" name="state-province" id="state-province" placeholder="" required>
            </div>
            <div class="input">
                <label for="phone-number">Phone Number</label>
                <input type="text" name="phone-number" id="phone-number" placeholder="" required>
            </div>
            <div class="input">
                <label for="country">Country</label>
                <input type="text" name="country" id="country" placeholder="" required>
            </div>
            <div class="input">
                <label for="postal">Postal Code</label>
                <input type="text" name="postal" id="postal" placeholder="" required>
            </div>
            <button class="button">Update Address</button>
        </form>
        </div>
        <div class="payment-info">
        <p class="title">Payment Information</p>
        <form class="form">
            <div class="input">
                <label for="name">Cardholder Name</label>
                <input type="text" name="name" id="name" placeholder="" required>
            </div>
            <div class="input">
                <label for="card-number">Card Number</label>
                <input type="text" name="card-number" id="card-number" placeholder="" required>
            </div>
            <div class="input">
                <label for="expiry-date">Expiry Date</label>
                <input type="date" name="expiry-date" id="expiry-date" placeholder="" required>
            </div>

            <div class="input">
                <label for="cvv">CVV Code</label>
                <input type="number" name="cvv" id="cvv" placeholder="" required>
            </div>
            <button class="button">Update Payment Information</button>
        </form>
        </div>
    </div>

    <div v-if="activeTab === 'orders'" class="active" id="orders">
        <h2>Order History</h2>
        <p>Here is the order history content.</p>
    </div>
</template>


<script>
import axios from "axios";

const axiosClient = axios.create({
	// NOTE: it's baseURL, not baseUrl
	baseURL: "http://localhost:8080"
});

  export default {
  name: "AccountView",
  data() {
    return {
      activeTab: "profile", // Default tab
    };
  },
};
</script>

<style scoped>
.payment-info, .address, .profile-info{
  width: 500px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 0.75rem;
  background-color: #1e1e1e;
  padding: 2rem;
  color: rgba(243, 244, 246, 1);
}

.address {
    margin-top: 20px;
}

.payment-info {
    margin-top: 20px;
}

.title {
  text-align: center;
  font-size: 1.5rem;
  line-height: 2rem;
  font-weight: 700;
  color: rgba(243, 244, 246, 1);
}

.form {
  margin-top: 1.5rem;
}

.input {
  margin-top: 0.25rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.input label {
  display: block;
  color: rgba(156, 163, 175, 1);
  margin-bottom: 4px;
  margin-top: 20px;
}

.input input {
  width: 100%;
  border-radius: 0.375rem;
  border: 1px solid rgba(55, 65, 81, 1);
  outline: 0;
  background-color: #1e1e1e;
  padding: 0.75rem 1rem;
  color: rgba(243, 244, 246, 1);
}

.input input:focus {
  border-color: rgba(167, 139, 250);
}

.signup a {
  color: rgba(243, 244, 246, 1);
  text-decoration: none;
  font-size: 14px;
}

.signup a:hover {
  text-decoration: underline rgba(167, 139, 250, 1);
}

.button {
  margin-top: 10px;
  display: block;
  width: 100%;
  background-color: rgba(167, 139, 250, 1);
  padding: 0.75rem;
  text-align: center;
  color: rgba(17, 24, 39, 1);
  border: none;
  font-weight: 600;
  cursor: pointer;
}

.sign-in:hover {
  text-decoration: underline rgba(55, 65, 81, 1);
}

body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #1e1e1e;
}

.tabs {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
    margin-top: 100px;
    margin-left: auto;
    margin-right: auto;
    width: 500px;
    background-color: #1e1e1e;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.tab {
    margin-left: auto;
    margin-right: auto;
    padding: 1rem 2rem;
    cursor: pointer;
    border: none;
    background-color: #1e1e1e;
    color: rgba(243, 244, 246, 1);
    transition: background-color 0.3s;
    flex: 1;
    text-align: center;
}

.tab:last-child {
    border-right: none;
}

.tab:hover {
    background: rgba(55, 65, 81, 1);
}

.tab.active {
    background: rgba(55, 65, 81, 1);
    font-weight: bold;
}

.tab-content {
    display: none;
    width: 500px;
    text-align: center;
    margin-top: 1rem;
    background-color: #1e1e1e;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.tab-content.active {
    display: block;
}

.info{
    padding-top: 20px;
    text-align: right;
    margin-right: 330px;
}
</style>