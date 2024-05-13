<script setup>
import { ref, defineAsyncComponent } from 'vue';
import HomeBanner from '../components/HomeBanner.vue';
const Checking = defineAsyncComponent(() => import('../components/customer/Checking.vue'));
const Savings = defineAsyncComponent(() => import('../components/customer/Savings.vue'));
const Transactions = defineAsyncComponent(() => import('../components/customer/Transactions.vue'));
const Profile = defineAsyncComponent(() => import('../components/customer/Profile.vue'));
import { useUserStore } from '../stores/user';

const userStore = useUserStore();

const selectedComponent = ref('checking');

const selectComponent = (component) => {
    selectedComponent.value = component;
}

const logout = () => {
  userStore.logout();
  router.push({ name: 'home' });
}

</script>

<template>
    <main>
        <div class="customer-container">
            <div class="side-menu">
                <HomeBanner />
                <ul class="nav-items">
                    <li id="accounts">Accounts
                        <ul>
                            <li class="nav-item" id="checkingAccount" @click="selectComponent('checking')">Checking</li>
                            <li class="nav-item" id="savingsAccount" @click="selectComponent('savings')">Savings</li>
                        </ul>
                    </li>
                    <li class="nav-item" id="transactions" @click="selectComponent('transactions')">Transactions</li>
                    <li class="nav-item" id="profile" @click="selectComponent('profile')">Profile</li>
                    <li class="nav-item" id="logout" @click="logout">Logout</li>
                </ul>
            </div>
            <div class="content-container">
                <Checking v-if="selectedComponent === 'checking'" />
                <Savings v-if="selectedComponent === 'savings'" />
                <Transactions v-if="selectedComponent === 'transactions'" />
                <Profile v-if="selectedComponent === 'profile'" />
            </div>
        </div>
    </main>
</template>

<style scoped>

.customer-container {
    display: flex;
    width: 100%;
    height: 100vh;
    margin: 0;
    padding: 0;
    font-family: 'Inter', sans-serif; 
}

.content-container {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
    box-shadow: inset 0 0 10px rgba(0,0,0,0.05);
}

.side-menu {
    background-color: #f4f5f7;
    box-shadow: 2px 0 5px rgba(0,0,0,0.1);
    font-size: 1.2em;
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

#checkingAccount:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#savingsAccount:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#transactions:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#profile:hover {
    background-color: hsla(160, 100%, 40%, 0.2);
}

#logout:hover {
    background-color: hsla(14, 100%, 40%, 0.2);
}

@media (max-width: 768px) {
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