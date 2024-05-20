<script setup>
import { ref, defineAsyncComponent, onMounted } from 'vue';
import AdminBanner from '../components/admin/AdminBanner.vue';
const UnapprovedUsers = defineAsyncComponent(() => import('../components/admin/UnapprovedUsers.vue'));
import { useAuthStore } from '../stores/auth';
import { useAdminStore } from '../stores/Admin';

const authStore = useAuthStore();
const adminStore = useAdminStore();

const selectedComponent = ref('unapprovedUsers');

const selectComponent = (component) => {
    selectedComponent.value = component;
}

const logout = () => {
    authStore.logout();
    router.push({ name: 'home' });
}

onMounted(async () => {
    await adminStore.fetchUnapprovedUsers();
})
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
                    <li id="unapprovedUsers" class="nav-item" @click="selectComponent('unapprovedUsers')">Unapproved Users</li>
                    <li id="logout" class="nav-item" @click="logout">Logout</li>
                </ul>
            </div>
            <div class="content-container">
                <UnapprovedUsers v-if="selectedComponent === 'unapprovedUsers'" />
                <ManageUsers v-if="selectedComponent === 'manageUsers'" />
                <Reports v-if="selectedComponent === 'reports'" />
                <Settings v-if="selectedComponent === 'settings'" />
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
    padding: .5em;
    margin: .5em 1em 0 1em;
    cursor: pointer;
}

#unapprovedUsers:hover,
#manageUsers:hover,
#reports:hover,
#settings:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#logout:hover {
    background-color: hsla(14, 100%, 40%, 0.2);
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
