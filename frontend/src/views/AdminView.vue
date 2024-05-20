<script setup>
import HomeBanner from '../components/account/HomeBanner.vue'
import NonregistedAccounts from '../components/account/NonregistedAccounts.vue'
import { ref } from 'vue';

</script>

<template>
    <main>
        <HomeBanner />
        <div class="registration-container">
            <NonregistedAccounts />
        </div>
    </main>
</template>
