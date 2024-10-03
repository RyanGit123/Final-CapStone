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
      <div class="header-right">
        <Avatar @click="settings" icon="pi pi-user" class="mr-2" size="xlarge" shape="circle" />
      </div>
    </header>
    
    <div class="content">
      <aside class="property-info">
        <h2>Property Info</h2>
        <p><strong>Property Name:</strong> {{ property.name }}</p>
        <p><strong>Address:</strong> {{ property.address }}</p>
        <p><strong>Rent Amount:</strong> {{ property.rentAmount }}</p>
        <p><strong>Lease Start Date:</strong> {{ property.leaseStartDate }}</p>
        <p><strong>Lease End Date:</strong> {{ property.leaseEndDate }}</p>
      </aside>

      <main class="main-content">
        <div class="payment-history-section">
          <div class="section-header">
            <h2>Payment History</h2>
            <Button label="Make a Payment" @click="openDialog('make payment')" class="make-payment-button" raised />
          </div>
          <div class="payment-table-container">
          <DataTable :value="paymentHistory" :rowClass="paymentRowClass"  paginator :rows="5" class="payment-history-table">
            <Column field="paymentDate" header="Date Of Payment" />
            <Column field="dueDate" header="Payment Due Date" />
            <Column field="amountDue" header="Amount Due" />
            <Column field="amountPaid" header="Amount Paid" />
            <Column field="paymentMethod" header="Method of Payment" />
            <Column field="lateFee" header="Late Fee" />
          </DataTable>
          </div>
        </div>

        <div class="maintenance-history-section">
          <div class="section-header">
            <h2>Maintenance History</h2>
            <Button label="Make a Request" @click="openDialog('request')" class="make-request-button" />
          </div>
          <div class="maintenance-table-container">
            <DataTable :value="maintenanceHistory" paginator :rows="5" class="maintenance-history-table">
              <Column field="requestDate" header="Request Date" />
              <Column field="dueDate" header="Due Date" />
              <Column field="completionDate" header="Completion Date" />
              <Column field="worker" header="Worker" />
              <Column field="description" header="Description" />
            </DataTable>
          </div>
        </div>
      </main>
    </div>

    <footer class="footer">
      <p>© 2024 Company Name. All rights reserved.</p>
    </footer>
  </div>

  <div v-if="currentDialog === 'make payment'"  class="card flex justify-center">
    <Dialog 
    header="Payment Method" 
    v-model:visible="visible" 
    modal  
  >
    <div>
      <Select 
        v-model="selectedPaymentType" 
        :options="paymentTypes" 
        optionLabel="label" 
        placeholder="Select Payment Method" 
       
      />
    </div>
    <div>
      <label for="paymentAmount">Amount Due: ${{ amountDue }}</label>
    </div>
    <div>
      <label for="lateFeeAmount">Late Fee: ${{ lateFeeAmount }}</label>
    </div>
    <div>
      <label for="totalAmountDue">Total Amount Due: ${{ totalAmountDue }}</label>
    </div>
    <div>
      <label for="paymentAmount">Payment Amount</label>
      <InputNumber id="paymentAmount" v-model="paymentAmount" mode="currency" currency="USD" locale="en-US"  fluid/>
    </div>


    <!-- Display Bank Account Input -->
    <div v-if="selectedPaymentType.value === 'Bank Account'" class="p-field">
      <label for="bankAccount">Bank Account</label>
      <InputNumber id="bankAccount" v-model="bankAccount" placeholder="Enter Bank Account" :useGrouping="false"  :min="0" :max="99999999999999999"   fluid />
    </div>

    <!-- Display Credit Card Input with Mask -->
    <div v-else-if="selectedPaymentType.value === 'Credit Card'" class="p-field">
      <label for="creditCard">Credit Card</label>
        <InputMask 
          id="creditCard"
          v-model="creditCard" 
          :mask="'9999 9999 9999 9999'"
          :placeholder="'9999 9999 9999 9999'"
          fluid
        />
    </div>
    <div class="flex justify-end gap-2">
      <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
      <Button type="button" label="Save" @click="makePayment"></Button>
    </div>
  </Dialog>
</div>


 <div v-if="currentDialog === 'request'"  class="card flex justify-center">
        <Dialog v-model:visible="visible" modal header="Make a Request" :style="{ width: '25rem' }">
          <div class="flex items-center gap-4 mb-4">
            <label for="description" class="font-semibold w-24">Description of Request</label>
            <Textarea v-model="description" autoResize rows="5" cols="30" />
          </div>
          <div class="flex justify-end gap-2">
            <Button type="button" label="Cancel" severity="secondary" @click="visible = false"></Button>
            <Button type="button" label="Save" @click="makeRequest"></Button>
          </div>
        </Dialog>
      </div>
</template>


<script>
import { ref, computed } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Toast from 'primevue/toast';
import Avatar from 'primevue/avatar';
import Dialog from 'primevue/dialog';
import InputNumber from 'primevue/inputnumber';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import Select from 'primevue/select';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputMask from 'primevue/inputmask';
import PaymentHistoryService from '@/services/PaymentHistoryService';
import RequestService from '@/services/RequestService';

export default {
  components: {
    DataTable,
    Column,
    Button,
    Toast,
    Avatar,
    Dialog,
    InputNumber,
    InputText,
    Textarea,
    Select,
    InputGroup,
    InputGroupAddon,
    InputMask,
  },
  setup(){
    const currentDate = new Date();

   const firstOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);

   return {
     currentDate,
     firstOfMonth,
   };

  },
  data() {
    return {
      visible: false,
      selectedPaymentType: 'Bank Account',
      paymentTypes: [
        { label: 'Bank Account', value: 'Bank Account' },
        { label: 'Credit Card', value: 'Credit Card' },
      ],
      bankAccount: '',
      creditCard: '',
      property: {
        name: 'Sample Property',
        address: '123 Main St, Newark, DE 19702',
        rentAmount: '$1200',
        leaseStartDate: '2023-01-01',
        leaseEndDate: '2024-01-01',
      },
      paymentHistory: [],
      maintenanceHistory: [
       
      ],
      paymentAmount: null,
      accountNumber: '',
      currentDialog: null,
      description: '',
      renterId: this.$store.state.user.id,
      lateFeeAmount: 0, // Stores the calculated late fee
      totalAmountDue: 0, // Stores the total amount due including late fee
      amountDue: 1200, // Assuming this value for the sake of example
    };
  },
  methods: {
    loadPaymentHistory() {
  PaymentHistoryService.getPayHistoryByUserId(this.renterId)
    .then((response) => {
     this.paymentHistory = response.data;
    })
    .catch((error) => {
      console.error('Error loading payment history:', error);
      this.$refs.toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to load payment history. Please try again.',
        life: 3000,
      });
    });
},
makePayment() {
  // Hide the dialog
  this.visible = false;

  // Create the payment object
  const payment = {
    paymentDate: new Date().toISOString().split('T')[0],
    dueDate: this.currentDate.toISOString().split('T')[0],
    amountDue: this.amountDue,
    amountPaid: this.paymentAmount,
    paymentMethod: this.selectedPaymentType.value,
    lateFee: parseInt(this.lateFeeAmount),
    userId: this.renterId,
    propertyId: 1,
    rentersName: 'james gunn' // Make dynamic if needed
  };

  // Send the payment to the database and handle the response
  PaymentHistoryService.makePayment(payment)
    .then((response) => {
      // Handle the response from the server, if needed
      console.log('Payment response:', response);

      // Show a success toast after successful payment
      this.$refs.toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Payment made successfully!',
        life: 3000,
      });

      // Reload the payment history after the payment is successfully made
      return this.loadPaymentHistory();
    })
    .then(() => {
      // Reset the form fields after the payment history is reloaded
      this.paymentAmount = null;
      this.accountNumber = '';
      this.resetForm();
    })
    .catch((error) => {
      console.error('Error making payment:', error);

      // Show an error toast
      this.$refs.toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Payment failed. Please try again.',
        life: 3000,
      });
    });
},
    openDialog(pressed) {
      this.currentDialog = pressed;
      this.visible = true;
      if (pressed === 'make payment') {
        
        // Calculate the late fee for the current payment
        let lateFee = 0;
        const daysPastDue = Math.floor((this.currentDate - this.firstOfMonth) / (1000 * 60 * 60 * 24));

        if (daysPastDue >= 6) {
          lateFee = this.amountDue * 0.05; // 5% of the amount due as the late fee
        }

        // Set the values to be displayed in the payment dialog
        this.lateFeeAmount = lateFee.toFixed(2);
        this.totalAmountDue = (this.amountDue + lateFee).toFixed(2);
      }
    },
    makeRequest() {
      const currentDateMap = this.currentDate;
      currentDateMap.setDate(currentDateMap.getDate() + 10);
      const futureDate = currentDateMap.toISOString().split('T')[0];

  const newRequest = {
    requestDate: new Date().toISOString().split('T')[0],
    dueDate: futureDate,
    propertyId: 1,  // Make dynamic if needed
    completionDate: '',
    completed: false,
    assignedWorkerId: 0,
    description: this.description,
  };

  // Send the new maintenance request to the database using the makeRequest API call
  RequestService.makeRequest(newRequest)
    .then((response) => {
      // Add the new request to the maintenance history after it is successfully saved in the database
      this.maintenanceHistory.push(response.data);

      // Hide the dialog
      this.visible = false;

      // Clear the description
      this.description = '';

      // Show a success toast
      this.$refs.toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'New request made successfully!',
        life: 3000
      });
    })
    .catch((error) => {
      console.error('Error making request:', error);

      // Show an error toast if the request fails
      this.$refs.toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to make the request. Please try again.',
        life: 3000
      });
    });
},

    settings() {
      this.$refs.toast.add({ severity: 'success', summary: 'Success', detail: 'Settings', life: 3000 });
    },
    fetchMaintenanceRequests() {
  const propertyId = 1; // Replace with dynamic property ID if necessary

  RequestService.getRequestsByPropertyId(propertyId)
    .then((response) => {
      // Set the maintenance history with the data retrieved from the server
      this.maintenanceHistory = response.data;
    })
    .catch((error) => {
      console.error('Error fetching maintenance requests:', error);

      // Show an error toast if fetching requests fails
      this.$refs.toast.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to load maintenance requests. Please try again.',
        life: 3000,
      });
    });
},
  paymentRowClass(data) {
      const paymentDate = new Date(data.paymentMadeDate);
      const dueDate = new Date(data.paymentDueDate);
      const timeDiff = paymentDate - dueDate;
      const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));

      return dayDiff > 5 ? 'late-payment' : '';
    },
    resetForm() {
      // Reset form data when dialog is closed
      this.selectedPaymentType = '';
      this.paymentAmount = '';
      this.bankAccount = '';
      this.creditCard = '';
    },
  },
  created() {
    console.log('$store.state.user:', this.renterId);
    this.fetchMaintenanceRequests();
    this.loadPaymentHistory(); // Load payment history when the page loads
  },
};
</script>


<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  display: grid;
  grid-template-columns: 1fr 3fr 1fr;
  align-items: center;
  padding: 1rem;
  background-color: #f0f0f0;
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
  flex: 1;
  overflow-y: auto;
}

.property-info {
  width: 20%;
  background-color: #f0f0f0;
  padding: 1rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.main-content {
  flex: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  gap: 2rem; /* Gap between sections */
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.maintenance-history-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.maintenance-table-container, .payment-table-container {
  flex: 1;
  overflow-y: auto; /* Enable scrolling for the table */
  max-height: calc(100vh - 20rem); /* Adjust this to control the height */
  margin-bottom: 2rem; /* Maintain gap between the table and footer */
  border: 1px solid #ddd; /* Optional: Border for better separation */
}

.footer {
  text-align: center;
  padding: 1rem;
  background-color: #f0f0f0;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  height: 4rem; /* Ensure the footer has enough space */
}
</style>
