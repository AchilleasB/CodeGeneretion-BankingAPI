<template>
  <div class="approved-users">
    <h2>Approved Users</h2>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <table class="user-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in approvedUsers" :key="user.id" class="user-item">
          <td>{{ user.firstName }} {{ user.lastName }}</td>
          <td>{{ user.email }}</td>
          <td>
            <button
              v-if="!user.accountCreated && selectedUserId !== user.id"
              @click="showForm(user.id)"
              class="approve-button"
            >
              Create Account
            </button>
            <AccountForm
              v-if="selectedUserId === user.id"
              :userId="user.id"
              @accountCreated="handleAccountCreated"
            />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { useAdminStore } from '@/stores/admin';
import { computed, onMounted, ref } from 'vue';
import AccountForm from './AccountForm.vue';

export default {
  name: 'ApprovedUsers',
  components: {
    AccountForm,
  },
  setup() {
    const adminStore = useAdminStore();
    const approvedUsers = computed(() => adminStore.approvedUsers);
    const selectedUserId = ref(null);
    const successMessage = ref('');

    onMounted(async () => {
      await adminStore.fetchApprovedUsers();
    });

    const showForm = (userId) => {
      selectedUserId.value = userId;
      successMessage.value = ''; // Clear success message when showing form
    };

    const handleAccountCreated = async () => {
      await adminStore.fetchApprovedUsers(); // Refresh the list after account creation
      selectedUserId.value = null; // Reset the selected user
      successMessage.value = 'Account successfully created!'; // Set success message
      setTimeout(() => {
        successMessage.value = ''; // Clear the message after a few seconds
      }, 3000); // Adjust the timeout duration as needed
    };

    return {
      approvedUsers,
      selectedUserId,
      successMessage,
      showForm,
      handleAccountCreated,
    };
  },
};
</script>

<style scoped>
.approved-users {
  padding: 20px;
}

.approved-users h2 {
  margin-bottom: 20px;
  color: #555;
}

.success-message {
  font-size: 1.2em;
  color: green;
  margin-bottom: 20px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  border: 1px solid #ddd;
  padding: 8px;
}

.user-table th {
  background-color: #f2f2f2;
  text-align: left;
}

.approve-button {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.approve-button:hover {
  background-color: #0056b3;
}
</style>
