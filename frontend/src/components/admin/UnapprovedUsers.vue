<template>
    <div class="unapproved-users">
        <h2>Unapproved Users</h2>
        <div v-if="isLoading" class="loading">Loading...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <ul v-else class="user-list">
            <li v-for="user in unapprovedUsers" :key="user.id" class="user-item">
                {{ user.name }}
                <button @click="approveUser(user.id)" class="approve-button">Approve</button>
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

        return {
            unapprovedUsers: adminStore.unapprovedUsers,
            isLoading: adminStore.isLoading,
            error: adminStore.error,
            fetchUnapprovedUsers: adminStore.fetchUnapprovedUsers,
            approveUser: adminStore.approveUser
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

.approve-button {
    padding: 5px 10px;
    background-color: #28a745;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.approve-button:hover {
    background-color: #218838;
}
</style>
