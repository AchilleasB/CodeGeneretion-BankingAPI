<script setup>
import { useAuthStore } from '../stores/auth';

const authStore = useAuthStore();

const isCustomer = authStore.isCustomer;
const isAdmin = authStore.isAdmin;
</script>

<template>
    <div class="container">
        <footer class="d-flex flex-wrap justify-content-between align-items-center py-3 my-4 border-top">
            <p class="col-md-4 mb-0 text-body-secondary">© 2024 SuperBank, Inc</p>
            <ul class="nav col-md-4 justify-content-end">
                <li class="nav-item"><router-link to="/" class="nav-link px-2 text-body-secondary">Home</router-link></li>
                <li class="nav-item"><router-link to="/about" class="nav-link px-2 text-body-secondary">About us</router-link></li>
                <li v-if="isCustomer" class="nav-item"><router-link to="/customer" class="nav-link px-2 text-body-secondary">Customer</router-link></li>
                <li v-if="isAdmin" class="nav-item"><router-link to="/admin"
                        class="nav-link px-2 text-body-secondary">Admin</router-link></li>
            </ul>
        </footer>
    </div>
</template>