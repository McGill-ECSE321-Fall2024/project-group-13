<template>
    <div class="create-account">
    <p class="title">Sign Up</p>
    <form class="form">
        <div class="input">
            <label for="name">Name</label>
            <input type="text" name="name" id="name" placeholder="" required v-model="name">
        </div>
        <div class="input">
            <label for="username">Username</label>
            <input type="text" name="username" id="username" placeholder="" required v-model="username">
        </div>
        <div class="input">
            <label for="password">Password</label>
            <input type="password" name="password" id="password" placeholder="" required v-model="password">
        </div>
        <div class="input">
            <label for="email">Email</label>
            <input type="text" name="email" id="email" placeholder="" required v-model="email">
        </div>
        <div class="input">
            <label for="phone-number">Phone Number</label>
            <input type="text" name="phone-number" id="phone-number" placeholder="" required v-model="phoneNumber">
        </div>
            <button class="sign-in nav-item" @click="attemptSignUp" v-bind:disabled="!isInputValid()">Create Account</button>
            <p class="login">Already have an account?
            <RouterLink to="/Login" class="nav-item">Login</RouterLink>
            <p v-if="errorMessage" class="error-message">{{  errorMessage }}</p>
        </p>
    </form>
    </div>
</template>

<script>
import axios from "axios";
import { session } from '../session.js'


const axiosClient = axios.create({
	// NOTE: it's baseURL, not baseUrl
	baseURL: "http://localhost:8080"
});

export default {
    name: 'SignUpView',

    data() {
		return {
            name: null,
			username: null,
			password: null,
            email: null,
            phoneNumber: null,
            errorMessage: null
		};
	},

    methods: {
        async attemptSignUp() {
            event.preventDefault();  // Prevent the form from submitting and updating the URL

			const customerRequest = {
                name: this.name,
				username: this.username,
				password: this.password,
                email: this.email,
                phoneNumber: this.phoneNumber
			};
			try {
                console.log(sessionStorage.getItem("loggedInUsername"));
                console.log(customerRequest)
				const response = await axiosClient.post("/customers", customerRequest, {
                    params: { loggedInUsername: sessionStorage.getItem("loggedInUsername") }   // Add the query parameter
                });
                this.username = response.data.username;
                this.permissionLevel = response.data.permissionLevel;
                sessionStorage.setItem("loggedInUsername", this.username);
                sessionStorage.setItem("permissionLevel", 1);
                this.clearInputs();
                session.updateSession(response.data.username, response.data.permissionLevel);
                console.log("loggedInUsername is now:", sessionStorage.getItem("loggedInUsername"));
                console.log("permissionLevel is now:", sessionStorage.getItem("permissionLevel"));
                this.$router.push("/account");
			}
			catch (error) {
                // Check if the error is a server response with a status code
                if (error.response) {
                    const status = error.response.status;
                    const message = error.response.data?.message || "An error occurred.";
                    
                    // Display user-friendly messages based on status codes or backend message
                    if (status === 400 || status === 404 || status == 403) {
                        this.errorMessage = message; // Example: Invalid credentials
                        console.log(message);
                    } else if (status === 403) {
                        this.errorMessage = "Access denied. Please contact support.";
                    } else {
                        this.errorMessage = "An unexpected error occurred.";
                    }
                } else {
                    // Network or unexpected error
                    console.error(error);
                    this.errorMessage = "Unable to connect to the server.";
                }
            }
        },

        clearInputs() {
			this.username = null,
			this.password = null,
            this.permissionLevel = 0,
            this.errorMessage = null
		},

        isInputValid() {
            return this.username   
            && this.password
            && this.name
            && this.email
            && this.phoneNumber
        }
    }
}

</script>

<style scoped>
.create-account {
  width: 500px;
  margin-top: 100px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 10px;
  background-color: #1e1e1e;
  padding: 2rem;
  color: rgba(243, 244, 246, 1);
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

.sign-in {
  margin-top: 10px;
  display: block;
  width: 100%;
  background-color: rgba(167, 139, 250, 1);
  padding: 0.75rem;
  text-align: center;
  color: rgba(17, 24, 39, 1);
  border: none;
  border-radius: 0.375rem;
  font-weight: 600;
  cursor: pointer;
}

.sign-in:hover {
  text-decoration: underline rgba(55, 65, 81, 1);
}

.sign-in:disabled {
  background-color: rgba(75, 85, 99, 1);
  color: rgba(243, 244, 246, 0.5);
  cursor: not-allowed; 
}

.error-message {
    color: red;
    font-size: 0.875rem;
    margin-top: 10px;
    text-align: center;
}

.login {
  text-align: center;
  font-size: 0.75rem;
  line-height: 1rem;
  color: rgba(156, 163, 175, 1);
}
</style>