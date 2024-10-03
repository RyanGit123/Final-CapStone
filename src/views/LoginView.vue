<template>
  <div class="page-container">
    <header class="header">
        <div class="header-left">
          <img src="/fakelogo1.jpg" alt="App Logo" class="logo" />
        </div>
        <div class="header-center">
          <h1>Company Name</h1>
        </div>
      </header>
    
    <div id="login" class="login-container">
      <form v-on:submit.prevent="login">
        <h1>Please Sign In</h1>
        <div role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div role="alert" v-if="this.$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <InputText type="text" id="username" v-model="user.username" required autofocus />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <InputText type="password"  id="password" toggleMask v-model="user.password" required />
        </div>
        <button type="submit" class="submit-button">Sign in</button>
        <p>
          <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";
import InputText from 'primevue/inputtext';

export default {
  components: {
    InputText,
  },
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/homeExpand");
          }
        })
        .catch(error => {
          const response = error.response;
          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
}
.header {
    display: grid;
    grid-template-columns: 1fr 3fr 1fr;
    align-items: center;
    padding: 1rem;
   background-color: #456990;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .header-left img {
  display: block;
  max-width: 100%;
  height: auto;
  padding: 0;
  margin: 0;
  border: none;
}

.header-left img {
  display: inline-flex;
  max-width: 100%; /* Ensure the image does not exceed the container */
  height: auto;
  padding: 0;
  margin: 0;
  line-height: 0; /* Remove space below image */
}
  
  .header-center {
    display: flex;
    justify-content: center;
  }
  

.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background: #fffffffd;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}
.form-input-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
label {
  margin-right: 0.5rem;
}
.submit-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 1rem;
}
</style>
