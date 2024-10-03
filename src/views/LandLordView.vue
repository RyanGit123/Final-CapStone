<template>
    <div class="dashboard">
      <Toast ref="toast" />
      <header class="header">
        <div class="header-left">
          <img src="/fakelogo1.jpg" alt="App Logo" class="logo" />
        </div>
        <div class="header-center">
          <h1>Company Name</h1>
        </div>
        <div class="header-right" >
          <Avatar @click="settings" icon="pi pi-user" class="mr-2" size="xlarge" shape="circle" />      </div>
      </header>
      
      <div class="content">
        <aside class="property-info">
          <h2>More Tools</h2>
        <Button label="Add Property" @click="openDialog('add property')" class="card-flex-buttons" raised />
      </aside>
        

    <!-- profit & loss line chart -->

        <main class="main-content">
          <div class="payment-history-section">
            <div class="section-header">
              <h2>Profit & Loss</h2>
            </div>
            <div class="card">
            <Chart type="line" :data="lineChartData" :options="lineChartOptions" class="h-[30rem]" />
          </div>
          </div>

    <!-- Maintenance Request Table -->

          <div class="payment-history-section">
            <div class="section-header">
        <h2>Maintenance Request</h2>
        <Button label="Assign Worker" @click="openDialog('assign worker')" class="card-flex-buttons" raised />
      </div>
      <DataTable 
  v-model:selection="selectedRequests" 
  :value="maintenanceRequest" 
  paginator 
  :rows="5" 
  tableStyle="min-width: 50rem">
  <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
  <Column field="propertyId" header="Property"></Column>
  <Column field="description" header="Description"></Column>
  <Column field="requestDate" header="Request Date"></Column>
  <Column field="assignedWorkerId" header="Assigned Worker"></Column>
</DataTable>
          </div>

    <!-- Rental Applications -->

          <div class="maintenance-history-section">
            <div class="section-header">
              <h2>Rental Applications</h2>
              <Button label="Assign Tenant" @click="openDialog('assign tenant')" class="card-flex-buttons" raised />
            </div>
            <DataTable :value="rentalApplications" class="maintenance-history-table">
              <Column field="potentialRenter" header="Name" />
              <Column field="creditScore" header="Credit Score" />
              <Column field="address" header="Address" />
              <Column field="decision" header="Accept/Deny"  />
            </DataTable>
          </div>

        </main>
    <!-- Pending Payments -->



    <!-- Payments Received -->

        <div class="main-content">
          <div class="payment-history-section">
            <div class="section-header">
              <h2>Payment Received</h2>
            </div>
            <DataTable :value="paymentReceived" :rowClass="paymentRowClass" class="payment-history-table">
              <Column field="renter" header="Renter" />
              <Column field="dueDate" header="Rent paid date" />
              <Column field="amountPaid" header="Amount paid" />
            </DataTable>
          </div>

    <!-- Overdue Renters -->
  
          <div class="maintenance-history-section">
            <div class="section-header">
              <h2>Overdue Renters</h2>
            </div>
            <DataTable :value="overDueRenters" class="maintenance-history-table">
              <Column field="renter" header="Renter" />
              <Column field="monthsOverDue" header="Month(s) over due" />
              <Column field="amountOwed" header="Amount owed" />
            </DataTable>
          </div>

    <!-- Occupancy Rate Pie Chart -->

          <div class="maintenance-history-section">
            <div class="section-header">
              <h2>Occupancy Rate</h2>
            </div>
            <div class="card-flex">
            <Chart type="pie" :data="pieChartData" :options="pieChartOptions" class="w-full md:w-[30rem]" />
          </div>
          </div>

        </div>
      </div>
  
      <footer class="footer">
        <p>© 2024 Company Name. All rights reserved.</p>
      </footer>

<!-- Add Property Dialog -->
<div v-if="currentDialog === 'add property'" class="card flex justify-center">
    <Dialog v-model:visible="visible" modal header="Add Property" :style="{ width: '25rem' }">
      <div class="flex flex-column gap-4 mb-4">
        <label for="propertyStreet" class="font-semibold">Street</label>
        <InputText v-model="newProperty.street" inputId="propertyStreet" fluid />

        <label for="propertyCity" class="font-semibold">City</label>
        <InputText v-model="newProperty.city" inputId="propertyCity" fluid />

        <label for="propertyState" class="font-semibold">State Abbrev ex."CA"</label>
        <InputText 
  v-model="newProperty.state" 
  inputId="propertyState" 
  fluid 
  maxlength="2" 
  @input="validateStateInput" 
  placeholder="Enter state abbreviation"
/>
        <label for="propertyZipCode" class="font-semibold">Zipcode</label>
        <InputText v-model="newProperty.zip" inputId="propertyZipCode" fluid />

        <label for="propertyDescription" class="font-semibold">Description</label>
        <InputText v-model="newProperty.description" inputId="propertyDescription" fluid />

        <label for="propertyBedrooms" class="font-semibold">Bedrooms</label>
        <InputText v-model="newProperty.roomCount" inputId="propertyBedrooms" fluid />

        <label for="propertyBathrooms" class="font-semibold">Bathrooms</label>
        <InputText v-model="newProperty.bathroomCount" inputId="propertyBathrooms" fluid />

        <label for="propertyRent" class="font-semibold">Rent Amount</label>
        <InputNumber v-model="newProperty.price" inputId="propertyRent" showButtons mode="currency" currency="USD" :min="0" fluid />
      </div>
      <div class="flex justify-end gap-2">
        <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
        <Button type="button" label="Save" @click="addProperty"></Button>
      </div>
    </Dialog>
</div>


<!-- Assign Worker Dialog -->
<div v-if="currentDialog === 'assign worker'" class="card flex justify-center">
      <Dialog v-model:visible="visible" modal header="Assign Worker" :style="{ width: '25rem' }">
        <div class="flex flex-column gap-4 mb-4">
          <label for="workerSelect" class="font-semibold"></label>
          <Select v-model="selectedWorker" :options="workers" optionLabel="name" inputId="workerSelect" placeholder="Select a Worker" class="w-full md:w-56" />
        </div>
        <div class="flex justify-end gap-2">
          <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
          <Button type="button" label="Save" @click="assignWorker"></Button>
        </div>
      </Dialog>
    </div>


  <!-- Assign Tenant Dialog -->
  <div v-if="currentDialog === 'assign tenant'" class="card flex justify-center">
    <Dialog v-model:visible=" visible" modal header="Assign Tenant" :style="{ width: '25rem' }">
      <div class="flex flex-column gap-4 mb-4">
        <label for="tenantName" class="font-semibold">Tenant Name</label>
        <InputText v-model="tenantName" inputId="tenantName" fluid />
        <label for="tenantAddress" class="font-semibold">Tenant Address</label>
        <InputText v-model="tenantAddress" inputId="tenantAddress" fluid />
      </div>
      <div class="flex justify-end gap-2">
        <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
        <Button type="button" label="Save" @click="assignTenant"></Button>
      </div>
    </Dialog>
  </div>


    </div>
  </template>
  
  <script>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Toast from 'primevue/toast';
import Avatar from 'primevue/avatar';
import Chart from 'primevue/chart';
import Dialog from 'primevue/dialog';
import InputNumber from 'primevue/inputnumber';
import InputText from 'primevue/inputtext';
import Select from 'primevue/select';
import RequestService from '@/services/RequestService';
import PropertyService from '../services/PropertyService';


export default {
  components: {
    DataTable,
    Column,
    Button,
    Toast,
    Avatar,
    Chart,
    Dialog,
    InputNumber,
    InputText,
    Select,
  },

  setup() {
    const workers = ref([
  {
    id: 5,
  username: 'manhunter',
  passwordHash: '$2a$10$opxo44/s0n1FMAkPIc2UBOP/4iLHS4m2YeEdqi20lAZyNktziB5d.',
  email: 'email12@email.com',
  name: 'jon jons',
  propertyId: 0,
  apartmentId: 0,
  role: 'ROLE_MAINTENANCE'
}
    ]);


    const newProperty = ref({
      street: '',
      city: '',
      state: '',
      zip: '',
      description: '',
      bathroomCount: '',
      roomCount: '',
      price: '',
      fullAddress: ``
    });

    const validateStateInput = (event) => {
    const input = event.target.value;

    // Allow only letters and truncate to two characters
    event.target.value = input.replace(/[^a-zA-Z]/g, '').toUpperCase().slice(0, 2);
    newProperty.value.state = event.target.value;
  };

    const selectedTenant = ref ({
      tenantName: '',
      tenantAddress: ''
    });

    const selectedRequests = ref ([]);

    const maintenanceRequest = ref([]);
    const selectedWorker = ref(null);

    const rentalApplications = ref([
      { potentialRenter: "John Doe", creditScore: "700", address: "100 Apple Lane", worker: "John Doe", decision: "Under Review" },
    ]);

    const paymentReceived = ref([
      { renter: "renter1", dueDate: "01/01/2000", amountPaid: "$12.00" },
    ]);

    const overDueRenters = ref([
      { renter: "renter2", monthsOverDue: "1", amountOwed: "$0.10" },
    ]);

    const lineChartData = ref(null);
    const lineChartOptions = ref(null);
    const pieChartData = ref(null);
    const pieChartOptions = ref(null);
    const visible = ref(false);
    const currentDialog = ref(null);
    const workerAddress = ref('');
    const isAddPropertyVisible = ref(false);
    const isAssignTenantVisible = ref(false);


    // Methods
    const openDialog = (pressed) => {
      currentDialog.value = pressed;
      visible.value = true;
    };

    const addProperty = async () => {

      visible.value = false;
      const propertyToSave = newProperty.value;
      propertyToSave.bathroomCount = parseFloat(propertyToSave.bathroomCount);
      propertyToSave.roomCount = parseInt(propertyToSave.roomCount);
      propertyToSave.price = parseFloat(propertyToSave.price);
      propertyToSave.fullAddress = `${propertyToSave.street}, ${propertyToSave.city}, ${propertyToSave.state} ${propertyToSave.zip}`;

      try {
        await PropertyService.addProperty(propertyToSave);
        this.refs.toast.add({ severity: 'success', summary: 'Success', detail: 'Property added', life: 3000 });
        isAddPropertyVisible.value = false;
      } catch (error) {
        console.error('Error adding property:', error);
        this.$refs.toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to add property', life: 3000 });
      }
    };

   

    const assignWorker = async () => {
  
  try {
    const worker = workers.value.find(worker => worker.name === selectedWorker.value.name);
    if (!worker) {
      this.$refs.toast.add({ severity: 'error', summary: 'Worker Not Found', detail: 'Please select a valid worker', life: 3000 });
      return;
    }

    // Update each selected request with the worker's details
    for (const request of selectedRequests.value) {
      await RequestService.assignWorker(request.id, worker.id);
      request.assignedWorkerId = worker.id;
    }

    visible.value = false;
    this.refs.toast.add({ severity: 'success', summary: 'Success', detail: 'Worker(s) assigned', life: 3000 });
  } catch (error) {
    console.error('Error assigning worker:', error);
    this.refs.toast.add({ severity: 'error', summary: 'Error', detail: 'Failed to assign worker(s)', life: 3000 });
  }
};


    const assignTenant = () => {
      visible.value = false;
      this.$refs.toast.add({ severity: 'success', summary: 'Success', detail: 'Tenant assigned', life: 3000 });
    };

    

    // Line chart setup
    const setupLineChart = () => {
      const documentStyle = getComputedStyle(document.documentElement);

      lineChartData.value = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
          {
            label: 'Gross profit per month',
            data: [65, 59, 80, 81, 56, 55, 40],
            fill: false,
            borderColor: documentStyle.getPropertyValue('--bluegray-700'),
            tension: 0.4
          },
          {
            label: 'Net profit per month',
            data: [28, 48, 40, 19, 86, 27, 90],
            fill: false,
            borderColor: documentStyle.getPropertyValue('--green-500'),
            tension: 0.4
          }
        ]
      };

      lineChartOptions.value = {
        maintainAspectRatio: false,
        aspectRatio: 1.5,
        plugins: {
          legend: {
            labels: {
              color: documentStyle.getPropertyValue('--text-color'),
            },
          },
        },
        scales: {
          x: {
            ticks: {
              color: documentStyle.getPropertyValue('--text-color-secondary'),
            },
            grid: {
              color: documentStyle.getPropertyValue('--surface-border'),
            },
          },
          y: {
            ticks: {
              color: documentStyle.getPropertyValue('--text-color-secondary'),
            },
            grid: {
              color: documentStyle.getPropertyValue('--surface-border'),
            },
          },
        },
      };
    };

    // Pie chart setup
    const setupPieChart = () => {
      pieChartData.value = {
        labels: ['Occupied', 'Vacant'],
        datasets: [
          {
            data: [80, 20],
            backgroundColor: ['#2196f3', '#f44336'],
            hoverBackgroundColor: ['#42a5f5', '#ef5350'],
          },
        ],
      };

      pieChartOptions.value = {
        plugins: {
          legend: {
            labels: {
              color: '#000000', // Default text color
            },
          },
        },
      };
      
    };
    const fetchRequests = async () => {
      try {
        const response = await RequestService.getAllRequests();
        maintenanceRequest.value = response.data;  // Assign the response data to the reactive variable
        
      } catch (error) {
        console.error('Error fetching requests:', error);
      }
    };

    // Lifecycle hooks
    onMounted(() => {
      setupLineChart();
      setupPieChart();
      fetchRequests();
    });

    return {
      workers,
      maintenanceRequest,
      rentalApplications,
      paymentReceived,
      overDueRenters,
      lineChartData,
      lineChartOptions,
      pieChartData,
      pieChartOptions,
      visible,
      currentDialog,
      selectedWorker,
      workerAddress,
      openDialog,
      addProperty,
      assignWorker,
      assignTenant,
      newProperty,
      validateStateInput,
      selectedRequests,
    };
  },
};
</script>


  <style scoped>

.card-flex {
  --red-500: #f44336; /* Red */
  --red-400: #ef5350; /* Lighter Red */
  --blue-500: #2196f3; /* Blue */
  --blue-400: #42a5f5; /* Lighter Blue */
}

  .dashboard {
    display: flex;
    flex-direction: column;
    height: 100vh;
  }

  aside.property-info{
    background-color: #456990;
    
  }
  
  .header {
    display: grid;
    grid-template-columns: 1fr 3fr 1fr;
    align-items: center;
    padding: 1rem;
   background-color: #456990;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .header-left {
    display: flex;
    justify-content: flex-start;
  }
  
  .header-center {
    display: flex;
    justify-content: center;
  }
  
  .header-right {
    display: flex;
    justify-content: flex-end;
  }
  
  .logo  {
    max-width: 100px;
    max-height: 100px;
    object-fit: fit;
  
  }
  
  .content {
    display: flex;
    height: calc(100vh - 60px); /* Adjust header height if needed */
  }
  
  .property-info {
    width: 20%;
    background-color: #f0f0f0;
    padding: 1rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow-y: auto;
  }
  
  .main-content {
   
    width: 40%;
    padding: 1rem;
    overflow-y: auto;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }
  
  .payment-history-table,
  .maintenance-history-table {
    width: 100%;
    margin-bottom: 2rem;
  }
  
  .footer {
    text-align: center;
    padding: 1rem;
    background-color: #456990;
    box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .late-payment {
    background-color: #ffcccc;
  }

  .card-flex-buttons {
    margin-top: 1rem;
  }
  </style>
  