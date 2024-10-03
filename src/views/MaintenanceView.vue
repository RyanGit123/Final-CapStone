<template>
  <div>
    <!-- Top Bar -->
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

    <!-- Main Content Splitter -->
    <Splitter style="height: calc(100vh - 50px);">
      <!-- Assigned Requests Table -->
      <SplitterPanel size="50" minSize="200">
        <div class="assigned-requests">
          <h3>Assigned Requests</h3>
          <DataTable :value="assignedRequests" paginator :rows="5" selectionMode="multiple" v-model:selection="selectedRequests">
            <Column selectionMode="multiple" style="width: 3em"></Column>
            <Column field="description" header="Description"></Column>
            <Column field="propertyId" header="Property"></Column>
            <Column field="dueDate" header="Due Date"></Column>
            <Column header="Status">
              <template #body="slotProps">
                <Tag :severity="getTagSeverity(slotProps.data)" rounded>
                  {{ slotProps.data.completed ? 'Complete' : getDueDateTag(slotProps.data.dueDate) }}
                </Tag>
              </template>
            </Column>
          </DataTable>
          <Button label="Mark as Complete" icon="pi pi-check" @click="markAsComplete" :disabled="!selectedRequests.length" />
        </div>
      </SplitterPanel>

      <!-- Completed Requests Table -->
      <SplitterPanel size="50" minSize="200">
        <div class="completed-requests">
          <h3>Completed Requests</h3>
          <DataTable :value="completedRequests" paginator :rows="5">
            <Column field="description" header="Description"></Column>
            <Column field="propertyId" header="Property"></Column>
            <Column field="completionDate" header="Completion Date"></Column>
          </DataTable>
        </div>
      </SplitterPanel>
    </Splitter>

    <footer class="footer">
      <p>© 2024 Company Name. All rights reserved.</p>
    </footer>
  </div>
</template>



<script>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Avatar from 'primevue/avatar';
import Splitter from 'primevue/splitter';
import SplitterPanel from 'primevue/splitterpanel';
import Tag from 'primevue/tag';  // Import PrimeVue Tag component
import requestService from '@/services/RequestService';
import Toast from 'primevue/toast';

export default {
  name: 'MaintenanceView',
  components: {
    DataTable,
    Column,
    Button,
    Avatar,
    Splitter,
    SplitterPanel,
    Tag,  // Register the Tag component
    Toast,
  },
  setup() {
    const assignedRequests = ref([]);
    const completedRequests = ref([]);
    const selectedRequests = ref([]);

    const markAsComplete = async () => {
      const now = new Date().toISOString().split('T')[0]; 
      try {
        for (const request of selectedRequests.value) {
          await requestService.markComplete(request.id);
          request.completionDate = now;
          completedRequests.value.push(request);
        }
        assignedRequests.value = assignedRequests.value.filter(
          (request) => !selectedRequests.value.includes(request)
        );
        selectedRequests.value = [];
      } catch (error) {
        console.error('Error marking requests as complete:', error);
      }
      
    };

    const getTagSeverity = (rowData) => {
      const daysLeft = Math.floor(
        (new Date(rowData.dueDate) - new Date()) / (1000 * 60 * 60 * 24)
      );

      if (rowData.completed) {
        return 'success';
      } else if (daysLeft <= 1) {
        return 'danger';
      } else if (daysLeft <= 3) {
        return 'warning';
      } else {
        return 'info';
      }
    };

    const getDueDateTag = (dueDate) => {
      const daysLeft = Math.floor(
        (new Date(dueDate) - new Date()) / (1000 * 60 * 60 * 24)
      );

      if (daysLeft <= 1) {
        return 'Urgent';
      } else if (daysLeft <= 3) {
        return 'Due Soon';
      } else {
        return 'Pending';
      }
    };

    const fetchRequests = async () => {
      try {
        const workerId = 5;
        const response = await requestService.getRequestsByWorkerId(workerId);
        const requests = response.data;
        assignedRequests.value = requests.filter((request) => !request.completed);
        completedRequests.value = requests.filter((request) => request.completed);
      } catch (error) {
        console.error('Error fetching requests:', error);
      }
    };

    onMounted(() => {
      fetchRequests();
    });

    return {
      assignedRequests,
      completedRequests,
      selectedRequests,
      markAsComplete,
      getTagSeverity,
      getDueDateTag,
    };
  },
  methods: {
    settings() {
      this.$refs.toast.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Settings',
        life: 3000,
      });
    },
  },
};

</script>

<style>
/* Top Bar Styles */
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

.logo {
  max-width: 100px;
  max-height: 100px;
  object-fit: fit;
}

/* Main Content Styles */
.assigned-requests, .completed-requests {
  padding: 20px;
  height: 100%;
  overflow: auto;
}

.assigned-requests {
  border-right: 1px solid #ccc;
}

/* Row Styling */
.row-red {
  background-color: #f8d7da !important;
}

.row-yellow {
  background-color: #fff3cd !important;
}

/* Footer Styles */
.footer {
  text-align: center;
  padding: 10px;
  border-top: 1px solid #ccc;
}
</style>
