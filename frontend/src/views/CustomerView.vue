<script setup>
import HomeBanner from '../components/HomeBanner.vue'
import Checking from '../components/customer/Checking.vue'
import Savings from '../components/customer/Savings.vue'
import Transactions from '../components/customer/Transactions.vue'
import Profile from '../components/customer/Profile.vue'
import { ref } from 'vue';

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
    margin: 0;
    padding: 0;
}

.content-container {
    width: 75%;
    height: 100%;
}

.side-menu {
    width: 25%;
    height: 95vh;
    background-color: #eff0f6;
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
</style>