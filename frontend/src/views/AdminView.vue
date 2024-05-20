<script setup>
import BankBanner from '../components/BankBanner.vue'
import NonregistedAccounts from '../components/account/NonregistedAccounts.vue'
import { ref } from 'vue';

</script>

<template>
    <main>
        <BankBanner />
        <div class="registration-container">
            <NonregistedAccounts />
        </div>
    </main>
</template>
