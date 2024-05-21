<template>
    <div class="approved-users">
      <h2>Approved Users</h2>
      <div v-if="isLoading" class="loading">Loading...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <ul v-else class="user-list">
        <li v-for="user in approvedUsers" :key="user.id" class="user-item">
          <div class="user-details">
            <span class="user-name">Name: {{ user.firstName }} {{ user.lastName }}</span>
            <span class="user-email">Email: {{ user.email }}</span>
            <button @click="createAccount(user.id)" class="approve-button">Create Account</button>
          </div>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import { useAdminStore } from '@/stores/Admin';
  import { computed, onMounted } from 'vue';
  
  export default {
    name: 'ApprovedUsers',
    setup() {
      const adminStore = useAdminStore();
      const approvedUsers = computed(() => adminStore.approvedUsers);
      const isLoading = computed(() => adminStore.isLoading);
      const error = computed(() => adminStore.error);
  
      onMounted(() => {
        adminStore.fetchApprovedUsers();
      });
  
      return {
        approvedUsers,
        isLoading,
        error,
        createAccount: adminStore.createAccount,
      };
    }
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
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #eee;
  }
  
  .user-item:last-child {
    border-bottom: none;
  }
  
  .user-details {
    display: flex;
    flex-direction: column;
  }
  
  .user-name,
  .user-email {
    margin: 0;
    padding: 0;
  }
  </style>
  