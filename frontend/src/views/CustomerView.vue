<script setup>
import { ref, defineAsyncComponent, onMounted, computed } from 'vue';
import BankBanner from '../components/BankBanner.vue';
const Checking = defineAsyncComponent(() => import('../components/customer/Checking.vue'));
const Savings = defineAsyncComponent(() => import('../components/customer/Savings.vue'));
const Transactions = defineAsyncComponent(() => import('../components/customer/Transactions.vue'));
const Profile = defineAsyncComponent(() => import('../components/customer/Profile.vue'));
const ATM = defineAsyncComponent(() => import('../components/customer/ATM.vue'));
import { useAuthStore } from '../stores/auth';
import { useAccountStore } from '../stores/account'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();
const accountStore = useAccountStore();
const userStore = useUserStore();

const selectedComponent = ref('checking');

const selectComponent = (component) => {
    selectedComponent.value = component;
}

const logout = async () => {
    await authStore.logout();
    router.push({ name: 'home' });
}

onMounted(async () => {
    const userId = authStore.id;
    await userStore.loadUserDetails(userId);
    await accountStore.getCustomerAccounts(userId);
    // console.log(accountStore.accounts);
})

</script>

<template>
    <main>
        <div class="customer-container">
            <div class="side-menu">
                <BankBanner />
                <div class="welcome">
                    <h3>Welcome, {{ authStore.firstName }}</h3>
                </div>
                <ul class="nav-items">
                    <li id="accounts">Accounts 
                    <ul>
                        <li class="nav-item" id="checkingAccount" @click="selectComponent('checking')">Checking</li>
                        <li class="nav-item" id="savingsAccount" @click="selectComponent('savings')">Savings</li>
                    </ul>
                    </li>
                    <li class="nav-item" id="transactions" @click="selectComponent('transactions')">Transactions</li>
                    <li class="nav-item" id="profile" @click="selectComponent('profile')">Profile</li>
                    <li class="nav-item" id="atm" @click="selectComponent('atm')">ATM</li>
                    <li class="nav-item" id="logout" @click="logout">Logout</li>
                </ul>
            </div>
            <div class="content-container">
                <Checking v-if="selectedComponent === 'checking'" />
                <Savings v-if="selectedComponent === 'savings'" />
                <Transactions v-if="selectedComponent === 'transactions'" />
                <Profile v-if="selectedComponent === 'profile'" />
                <ATM v-if="selectedComponent === 'atm'" />
            </div>
        </div>
    </main>
</template>

<style scoped>
.customer-container {
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

#accounts:hover {
    background-color: hsla(160, 100%, 90%, 0.2);
}

#checkingAccount:hover,
#savingsAccount:hover,
#transactions:hover,
#profile:hover,
#atm:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#logout:hover {
    background-color: hsla(14, 100%, 40%, 0.2);
}

@media (max-width: 790px) {
    .customer-container {
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