<template>
    <div class="page-container">
      <header class="header">
        <div class="logo">
          <img class="block xl:block mx-auto rounded w-full" src="/fakelogo1.jpg" alt="Company Logo" />
        </div>
        
        <div class="center">
          <h1>Company Name</h1>
          <IconField>
            <InputIcon class="pi pi-search" />
            <InputText v-model="value1" placeholder="Search" />
          </IconField>
        </div>
    
        <div class="Right-buttons">
        <Button label="Login" @click="goTOLogin" raised />
        <Button label="Register" @click= "register" raised />
        <Button v-if="$store.state.token != ''" label="Dashboard" @click="goToDashboard" raised />
      </div>
      </header>
    
      <h1 v-if="$store.state.token != ''" class="welcome-message">Welcome to Our Property Management App {{ $store.state.user.username }}</h1>
      <h1 v-else class="welcome-message">Welcome to Our Property Management App</h1>
    
      <div class="property-cards">
        <PropertyCard
          v-for="property in visibleProperties"
          :key="property.id"
          :property="property"
          class="property-card"
        />
      </div>
      
      <Toast ref="toast" />
    
      <div ref="loadMore" class="loading-indicator">
        <p v-if="loading">Loading more properties...</p>
      </div>
    
      <footer class="footer">
        <p>© 2024 Company Name. All rights reserved.</p>
      </footer>
    </div>
  </template>
  
  <script>
  import PropertyCard from '../components/PropertyCard.vue';
  import Button from 'primevue/button';
  import { useRouter } from 'vue-router';
  import IconField from 'primevue/iconfield';
  import InputIcon from 'primevue/inputicon';
  import Toast from 'primevue/toast';
  import { useToast } from "primevue/usetoast";
  import PropertyService from '../services/PropertyService'; // Import the PropertyService

  export default {
    name: 'HomeView',
    components: {
      PropertyCard,
      Button,
      IconField,
      InputIcon,
      Toast,
    }, 
    setup() {
      const router = useRouter();

      const goToExpandedView = () => {
        router.push({ name: 'expandedView' });
      };

      const register = () => {
        router.push({ name: 'register' });
      };

      const goTOLogin = () => {
        router.push({ name: 'login' });
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
        visibleCount: 12, // Number of properties to show initially
        loading: false,
        properties: [],
        filter: "",
      };
    },
    computed: {
      getUserRole() {
        return this.$store.state.user.role || '';
      },
      visibleProperties() {
        return this.properties.slice(0, this.visibleCount);
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
      loadMoreProperties() {
        if (this.loading) return;
        this.loading = true;

        setTimeout(() => {
          this.visibleCount += 12; // Load 12 more properties
          this.loading = false;
        }, 1000); // Simulate a network request delay
      },
      setupIntersectionObserver() {
        const observer = new IntersectionObserver(entries => {
          if (entries[0].isIntersecting) {
            this.loadMoreProperties();
          }
        }, { threshold: 1.0 });

        observer.observe(this.$refs.loadMore);
      },
      goToDashboard() {
        alert(this.$store.state.user.authorities[0].name)
        if (this.$store.state.user.authorities[0].name === 'ROLE_RENTER') {
          this.$router.push({ name: 'rentersDashboard' });
        } else if (this.$store.state.user.authorities[0].name === 'ROLE_LANDLORD') {
         this.$router.push({ name: 'landlord' });
        }else if(this.$store.state.user.authorities[0].name === 'ROLE_MAINTENANCE'){
          this.$router.push({ name: 'maintenance' });
        }
        else{
          this.$refs.toast.add({
            severity: 'error',
            summary: 'Access Denied',
            detail: 'Only renters can access this dashboard.',
            life: 3000,
          });
        }
      },
    },
    mounted() {
      this.fetchProperties(); // Fetch properties when the component is mounted
      this.setupIntersectionObserver();
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
    width: 100%;
    align-items: center;
    gap: 1rem;
    padding: 1rem;
    background-color:#456990;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .Right-buttons {
    display: flex;
    gap: 0.5rem;
    background-color: #456990;

  }
  
  .center {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    background-color: #456990;
  }
  .logo{
    background-color: #456990;

  }
  
  .logo img  {
  max-width: 100px;
  max-height: 100px;
  object-fit: fit;

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
  
  .property-card {
    width: 100%;
    height: 500px;
  }
  
  .loading-indicator {
    width: 100%;
    text-align: center;
    padding: 1rem;
  }
  
  .footer {
    width: 100%;
    text-align: center;
    padding: 1rem;
    background-color: #f0f0f0;
    box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  }
  </style>
  