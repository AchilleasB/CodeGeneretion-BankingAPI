<template>
    <div>
        <h1>Unapproved Users</h1>
        <div v-if="isLoading">Loading...</div>
        <div v-if="error" class="error">{{ error }}</div>
        <ul v-if="!isLoading && !error">
            <li v-for="user in unapprovedUsers" :key="user.id">
                Name:{{ user.firstName }} {{ user.lastName }} - Email:{{ user.email }} <button
                    @click="approveUser(user.id)">Approve</button>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { useAccountStore } from '@/stores/account';
import { onMounted } from 'vue';

const store = useAccountStore();
const { unapprovedUsers, fetchUnapprovedUsers, isLoading, error } = store;

onMounted(() => {
    fetchUnapprovedUsers();
});
</script>

<style>
.error {
    color: red;
}
</style>
