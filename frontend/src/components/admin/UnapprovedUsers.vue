<template>
  <div class="unapproved-users">
    <h2>Unapproved Users</h2>
    <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <table class="user-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in unapprovedUsers" :key="user.id" class="user-item">
          <td>{{ user.firstName }} {{ user.lastName }}</td>
          <td>{{ user.email }}</td>
          <td>
            <button @click="approveUser(user.id)" class="approve-button">Approve</button>
            <button @click="declineUser(user.id)" class="decline-button">Decline</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { useAdminStore } from '@/stores/admin';
import { onMounted, ref, computed } from 'vue';

export default {
  name: 'UnapprovedUsers',
  setup() {
    const adminStore = useAdminStore();
    const successMessage = ref('');
    const errorMessage = ref('');

    const unapprovedUsers = computed(() => adminStore.unapprovedUsers);

    const approveUser = async (userId) => {
      try {
        await adminStore.approveUser(userId);
        successMessage.value = 'User approved successfully!';
        errorMessage.value = '';
        setTimeout(() => {
          successMessage.value = '';
        }, 3000); // Clear the message after 3 seconds
      } catch (error) {
        errorMessage.value = 'Failed to approve user.';
        successMessage.value = '';
        setTimeout(() => {
          errorMessage.value = '';
        }, 3000); // Clear the message after 3 seconds
      }
    };

    const declineUser = async (userId) => {
      try {
        await adminStore.declineUser(userId);
        successMessage.value = 'User declined successfully!';
        errorMessage.value = '';
        setTimeout(() => {
          successMessage.value = '';
        }, 3000); // Clear the message after 3 seconds
      } catch (error) {
        errorMessage.value = 'Failed to decline user.';
        successMessage.value = '';
        setTimeout(() => {
          errorMessage.value = '';
        }, 3000); // Clear the message after 3 seconds
      }
    };

    onMounted(async () => {
      await adminStore.fetchUnapprovedUsers();
    });

    return {
      unapprovedUsers,
      successMessage,
      errorMessage,
      approveUser,
      declineUser,
    };
  },
};
</script>

<style scoped>
.unapproved-users {
  padding: 20px;
}

.unapproved-users h2 {
  margin-bottom: 20px;
  color: #555;
}

.success-message {
  font-size: 1.2em;
  color: green;
  margin-bottom: 20px;
}

.error-message {
  font-size: 1.2em;
  color: red;
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

.approve-button,
.decline-button {
  margin-right: 5px;
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.approve-button {
  background-color: #45a049;
  color: white;
}

.decline-button {
  background-color: red;
  color: white;
}
</style>
