<template>
    <div class="create-account">
    <p class="title">Sign Up</p>
    <form class="form">
        <div class="input">
            <label for="name">Name</label>
            <input type="text" name="name" id="name" placeholder="" required>
        </div>
        <div class="input">
            <label for="username">Username</label>
            <input type="text" name="username" id="username" placeholder="" required>
        </div>
        <div class="input">
            <label for="password">Password</label>
            <input type="text" name="password" id="password" placeholder="" required>
        </div>
        <div class="input">
            <label for="email">Email</label>
            <input type="text" name="email" id="email" placeholder="" required>
        </div>
        <div class="input">
            <label for="phone-number">Phone Number</label>
            <input type="text" name="phone-number" id="phone-number" placeholder="" required>
        </div>
        <RouterLink to="/Account" class="nav-item">
            <button class="sign-in" @click="attemptSignUp">Create Account</button>
        </RouterLink>
    </form>
    </div>
</template>

<script>
export default {
    name: 'SignUpView',

    methods: {
        async attemptSignUp() {
            var loggedIn = sessionStorage.getItem("loggedInUsername");
            console.log(loggedIn);
            var permissionLevel = sessionStorage.getItem("permissionLevel");
            console.log(permissionLevel);
        }
    }
}
</script>

<style scoped>
.create-account {
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
</style>