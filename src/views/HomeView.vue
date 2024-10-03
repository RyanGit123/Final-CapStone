<template>
  <div class="page-container">
    <header class="header">
      <div class="logo">
        <img class="logo-img" src="/fakelogo1.jpg" alt="Company Logo" />
      </div>
     
      <div class="center">
        <h1>Company Name</h1>
      </div>

      <div class="Right-buttons">
        <Button label="Login" @click="goTOLogin" raised />
        <Button label="Register" @click= "register" raised />
        <Button label="Landlord Page" @click="goToMaintenance" raised />
        <Button v-if="$store.state.token != ''" label="Dashboard" @click="goToDashboard" raised />
      </div>
    </header>

    <h1 v-if="$store.state.token != ''" class="welcome-message">Welcome to Our Property Management App {{ $store.state.user.username }}</h1>
    <h1 v-else class="welcome-message">Welcome to Our Property Management App</h1>

    <div class="property-cards">
      <PropertyCard v-for="property in displayedProperties" :key="property.id" :property="property" />
    </div>

    <div class="view-more">
      <Button label="View More" @click="goToExpandedView" />
    </div>

    <footer class="footer">
      <p>© 2024 Company Name. All rights reserved.</p>
    </footer>
  </div>
</template>

<script>
import PropertyCard from '../components/PropertyCard.vue';
import { mapGetters } from 'vuex';
import Button from 'primevue/button';
import { useRouter } from 'vue-router';
import PropertyService from '../services/PropertyService';

export default {
  name: 'HomeView',
  components: {
    PropertyCard,
    Button
  },
  setup() {
    const router = useRouter();

    const goToExpandedView = () => {
      router.push({ name: 'expandedView' });
    };
    const goTOLogin = () => {
      router.push({ name: 'login' });
    };

    const register = () => {
      router.push({ name: 'register' });
    };

    const goToLandlord = () => {
      router.push({name:'landlord'})
    }

    const goToMaintenance = () => {
      router.push({ name: 'maintenance' });
    };

    return { goToExpandedView, register, goTOLogin, goToLandlord, goToMaintenance };
  },
  data() {
    return {
      properties: [],
      filter: "",
    };
  },
  computed: {
   
    displayedProperties() {
      // Shuffle the properties array and take the first 4
      const shuffled = this.properties.sort(() => 0.5 - Math.random());
      return shuffled.slice(0, 4);
    }
  },
  methods: {
    fetchProperties() {
      PropertyService.getProperties()
        .then((response) => {
          this.properties = response.data;
        })
        .catch((error) => {
          console.error("Error fetching properties:", error);
        });
    },
  },
  created() {
    this.fetchProperties();
  },
};
</script>

<style>
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
  width: 100%;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #f0f0f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.Right-buttons {
  display: flex;
  gap: 0.5rem;
}

.center {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.logo-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: fill;
}


.welcome-message {
  width: 100%;
  text-align: center;
  margin: 1rem 0;
}

.property-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  width: 100%;
  justify-items: center;
}

.view-more {
  width: 100%;
  text-align: center;
  margin: 1rem 0;
}

.footer {
  width: 100%;
  text-align: center;
  padding: 1rem;
  background-color: #f0f0f0;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}
</style>
