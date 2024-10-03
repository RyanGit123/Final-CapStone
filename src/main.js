import { createApp } from 'vue'
import CapstoneApp from './App.vue'
import { createStore } from './store'
import router from './router'
import axios from 'axios'
import PrimeVue from "primevue/config";
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';
import ColumnGroup from 'primevue/columngroup';   
import Row from 'primevue/row';                 

import Password from 'primevue/password';

import 'primeicons/primeicons.css'
import Aura from '@primevue/themes/aura';
import Button from 'primevue/button';
import Card from 'primevue/card';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Ripple from 'primevue/ripple';
import  DataTable from 'primevue/datatable';
import Column from 'primevue/column';
  

/* sets the base url for server API communication with axios */
axios.defaults.baseURL = import.meta.env.VITE_REMOTE_API;

/*
 * The authorization header is set for axios when you login but what happens when 
 * you come back or the page is refreshed. When that happens you need to check 
 * for the token in local storage and if it exists you should set the header 
 * so that it will be attached to each request.
 */
let currentToken = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken) {
  // Set token axios requests
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

// Create the Vuex store passing in the stored credentials
const store = createStore(currentToken, currentUser);

const app = createApp(CapstoneApp);

app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});


app.directive('ripple', Ripple);
app.component('Button', Button);
app.component('Card', Card);
app.component('IconField', IconField);
app.component('InputIcon', InputIcon);
app.component('InputText', InputText);
app.component('DataTable', DataTable);
app.component('Column', Column);
app.component('Password', Password);
app.component('Toast', Toast);
app.use(ToastService);
app.use(PrimeVue, { ripple: true });
app.component('ColumnGroup', ColumnGroup);
app.component('Row', Row);
app.use(store);
app.use(router);
app.mount('#app');