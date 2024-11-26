<template>
    <div class="login">
        <p class="title">Login</p>
        <form class="form">
            <div class="input">
                <label for="username">Username</label>
                <input type="text" name="username" id="username" placeholder="" required v-model="username">
            </div>
            <div class="input">
                <label for="password">Password</label>
                <input type="text" name="password" id="password" placeholder="" required v-model="password">
            </div>
            <button class="sign-in" @click="attemptLogin">Sign in</button>
        </form>
        <p class="create-account">Don't have an account?
            <RouterLink to="/Register" class="nav-item">Sign up</RouterLink>
        </p>
    </div>
</template>

<script>
export default {
    name: 'LoginView',

    data() {
		return {
			username: null,
			password: null
		};
	},

    methods: {
        async attemptLogin() {
            sessionStorage.setItem("loggedInUsername", this.username);
        }
    }
}
</script>

<!--Open source: https://uiverse.io/Yaya12085/short-panda-24-->
<style scoped>
.login {
  width: 500px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 0.75rem;
  background-color: #1e1e1e;
  padding: 2rem;
  color: rgba(243, 244, 246, 1);
}

.title {
  text-align: center;
  font-size: 1.5rem;
  line-height: 2rem;
  font-weight: 700;
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

.create-account {
  text-align: center;
  font-size: 0.75rem;
  line-height: 1rem;
  color: rgba(156, 163, 175, 1);
}
</style>