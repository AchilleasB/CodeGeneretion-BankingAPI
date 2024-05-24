<script setup>
import { ref, defineAsyncComponent, onMounted, watch } from 'vue';
import AdminBanner from '../components/admin/AdminBanner.vue';
import { useRouter } from 'vue-router';
const UnapprovedUsers = defineAsyncComponent(() => import('../components/admin/UnapprovedUsers.vue'));
const ApprovedUsers = defineAsyncComponent(() => import('../components/admin/ApprovedUsers.vue'));
import { useAuthStore } from '../stores/auth';
import { useAdminStore } from '../stores/Admin';

const router = useRouter();
const authStore = useAuthStore();
const adminStore = useAdminStore();

const selectedComponent = ref('unapprovedUsers');

const selectComponent = (component) => {
  selectedComponent.value = component;
  if (component === 'approvedUsers') {
    adminStore.fetchApprovedUsers();
  }
};

const logout = () => {
  authStore.logout();
  router.push('/');
};

watch(selectedComponent, () => {
  if (selectedComponent.value === 'approvedUsers') {
    adminStore.selectedUserId = null;
  }
});

onMounted(async () => {
  await adminStore.fetchApprovedUsers();
  await adminStore.fetchUnapprovedUsers();
});
</script>

<template>
  <main>
    <div class="admin-container">
      <div class="side-menu">
        <AdminBanner />
        <div class="welcome">
          <h3>Welcome, {{ authStore.firstName }}</h3>
        </div>
        <ul class="nav-items">
          <li id="unapprovedUsers" class="nav-item" @click="selectComponent('unapprovedUsers')">Users Request</li>
          <li id="approvedUsers" class="nav-item" @click="selectComponent('approvedUsers')">Create Account</li>
          <li id="logout" class="nav-item" @click="logout">Logout</li>
        </ul>
      </div>
      <div class="content-container">
        <UnapprovedUsers v-if="selectedComponent === 'unapprovedUsers'" />
        <ApprovedUsers v-if="selectedComponent === 'approvedUsers'" />
      </div>
    </div>
  </main>
</template>

<style scoped>
.admin-container {
  display: flex;
  flex-wrap: wrap;
  min-height: 100vh;
}

.content-container {
  flex: 1;
  padding: 20px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.side-menu {
  background-color: #f4f5f7;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  font-size: 1.2em;
}

.welcome {
  padding: 1em;
  text-align: center;
  background-color: #f4f5f7;
  border-bottom: 1px solid #ccc;
  display: flex;
  flex-wrap: wrap;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 0.5em;
  margin: 0.5em 1em 0 1em;
  cursor: pointer;
}

#unapprovedUsers:hover,
#approvedUsers:hover,
#logout:hover {
  background-color: hsla(160, 100%, 40%, 0.2);
}

@media (max-width: 768px) {
  .admin-container {
    flex-direction: column;
  }

  .side-menu {
    width: 100%;
    height: auto;
    order: 1;
  }

  .content-container {
    order: 2;
    width: 100%;
    flex: none;
  }
}
</style>
