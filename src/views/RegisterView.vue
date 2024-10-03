<template>
  <div class="page-container">
    <header class="header">
      <div class="logo">
        <img src="/fakelogo1.jpg" alt="Company Logo" />
      </div>

      <div class="center">
        <h1>Company Name</h1>
      </div>
    </header>
    
    <div id="register" class="register-container">
      <form v-on:submit.prevent="register">
        <h1 class="text-center">Create Account</h1>
        <div role="alert" v-if="registrationErrors" class="alert">
          {{ registrationErrorMsg }}
        </div>

        <div class="form-input-group">
          <label for="name">Name</label>
          <input type="text" id="name" v-model="user.name" required autofocus />
        </div>

        <div class="form-input-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="user.username" required autofocus />
        </div>

        <div class="form-input-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="user.email" required />
        </div>

        <div class="form-input-group">
          <label for="password" >Password</label>
          <Password v-model="user.password" toggleMask input-class="custom-password-input">
            <template #header>
              <div class="font-semibold text-xm mb-4">Pick a password</div>
            </template>
            <template #footer>
              <Divider />
              <ul class="password-requirements">
                <li>Minimum 8 characters</li>
              </ul>
            </template>
          </Password>
        </div>

        <div class="form-input-group">
          <label for="confirmPassword">Confirm Password</label>
          <Password v-model="user.confirmPassword" toggleMask input-class="custom-password-input">
            <template #header>
              <div class="font-semibold text-xm mb-4">Confirm your password</div>
            </template>
          </Password>
        </div>

        <div class="form-input-group">
          <label for="accountType">Account Type</label>
          <Select v-model="role" :options="accountTypes" optionLabel="label" placeholder="Select Account Type" class="custom-select-input" />
        </div>

        <button type="submit" class="submit-button">Create Account</button>
        <p class="text-center">
          <router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import Password from 'primevue/password';
import Divider from 'primevue/divider';
import Select from 'primevue/select';
import authService from '../services/AuthService';

export default {
  components: {
    Password,
    Divider,
    Select,
  },
  data() {
    return {
      user: {
        name: '',
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        propertyId: '',
        apartmentId: '',
        role: '',
      },
      role: '',
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
      accountTypes: [
        { label: 'Landlord', value: 'Role_LandLord' },
        { label: 'Renter', value: 'Role_Renter' },
        { label: 'Maintenance worker', value: 'Role_Maintenance' },
      ],
    };
  },
  methods: {
    register() {
      // Clear previous errors
      this.clearErrors();

      // Check if passwords match
      if (this.user.password !== this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
        return;
      }
      this.user.role = this.role.value
     
      // Call the authService to register he user
      authService
        .register(this.user)
        .then((response) => {
          if (response.status === 201) {
            this.$router.push({
              path: '/login',
              query: { registration: 'success' },
            });
          }
        })
        .catch((error) => {
          const response = error.response;
          this.registrationErrors = true;
          if (response.status === 400) {
            this.registrationErrorMsg = 'Bad Request: Validation Errors';
          } else {
            this.registrationErrorMsg = 'An error occurred during registration. Please try again.';
          }
        });
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
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
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  background: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.text-center {
  text-align: center;
}

.alert {
  color: red;
  margin-bottom: 1rem;
}

.form-input-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

label {
  margin-bottom: 0.5rem;
}

input[type="text"],
input[type="email"],
.custom-password-input,
.custom-select-input,
button {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  margin-bottom: 0.5rem;
  box-sizing: border-box;
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

.submit-button:hover {
  background-color: #0056b3;
}

.password-requirements {
  list-style-type: none;
  padding: 0;
  margin: 0;
  color: #555;
}
</style>
