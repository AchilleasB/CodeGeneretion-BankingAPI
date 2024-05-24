<template>
  <div class="approved-users">
    <h2>Approved Users</h2>
    <div v-if="isLoading" class="loading">Loading...</div>
    <ul v-else class="user-list">
      <li v-for="user in approvedUsers" :key="user.id" class="user-item">
        <div class="user-details">
          <span class="user-name">Name: {{ user.firstName }} {{ user.lastName }}</span>
          <span class="user-email">Email: {{ user.email }}</span>
          <button
            v-if="!user.accountCreated && selectedUserId !== user.id"
            @click="showForm(user.id)"
            class="approve-button"
          >
            Create Account
          </button>
        </div>
        <AccountForm
          v-if="selectedUserId === user.id"
          :userId="user.id"
          :accountCreated="user.accountCreated"
          @accountCreated="handleAccountCreated"
        />
      </li>
    </ul>
  </div>
</template>

<script>
import { useAdminStore } from '@/stores/Admin';
import { computed, onMounted, ref } from 'vue';
import AccountForm from '../account/AccountForm.vue';

export default {
  name: 'ApprovedUsers',
  components: {
    AccountForm,
  },
  setup() {
    const adminStore = useAdminStore();
    const approvedUsers = computed(() => adminStore.approvedUsers);
    const isLoading = computed(() => adminStore.isLoading);
    const selectedUserId = ref(null);

    onMounted(async () => {
      await adminStore.fetchApprovedUsers();
    });

    const showForm = (userId) => {
      selectedUserId.value = userId;
    };

    const handleAccountCreated = (userId) => {
      adminStore.markAccountCreated(userId);
      selectedUserId.value = null; // Reset the form display
    };

    return {
      approvedUsers,
      isLoading,
      selectedUserId,
      showForm,
      handleAccountCreated,
    };
  },
};
</script>

<style scoped>
.approved-users {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.approved-users h2 {
  margin-bottom: 20px;
  color: #555;
}

.loading,
.error {
  font-size: 1.2em;
  color: #999;
}

.user-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.user-item {
  display: flex;
  flex-direction: column;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.user-item:last-child {
  border-bottom: none;
}

.user-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-name,
.user-email {
  margin: 0;
  padding: 0;
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
