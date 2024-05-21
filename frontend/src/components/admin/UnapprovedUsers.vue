<template>
  <div class="unapproved-users">
    <h2>Unapproved Users</h2>
    <div v-if="isLoading" class="loading">Loading...</div>
    <ul v-else class="user-list">
      <li v-for="user in unapprovedUsers" :key="user.id" class="user-item">
        <div class="user-details">
          <span class="user-name">Name: {{ user.firstName }} {{ user.lastName }}</span>
          <span class="user-email">Email: {{ user.email }}</span>
          <button @click="approveUser(user.id)" class="approve-button">Approve User</button>
          <button @click="declineUser(user.id)" class="decline-button">Decline User</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { useAdminStore } from '@/stores/Admin';

export default {
  name: 'UnapprovedUsers',
  setup() {
    const adminStore = useAdminStore();

    const approveUser = async (userId) => {
      await adminStore.approveUser(userId);
    };

    const declineUser = async (userId) => {
      await adminStore.declineUser(userId);
    };

    return {
      unapprovedUsers: adminStore.unapprovedUsers,
      isLoading: adminStore.isLoading,
      error: adminStore.error,
      fetchUnapprovedUsers: adminStore.fetchUnapprovedUsers,
      approveUser,
      declineUser,
    };
  },
  mounted() {
    this.fetchUnapprovedUsers();
  }
};
</script>

<style scoped>
.unapproved-users {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.unapproved-users h2 {
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

.approve-button {
  margin-right: 10px;
}

.decline-button {
  background-color: red;
  color: white;
}
</style>
