<script setup>
import HomeBanner from '../components/account/HomeBanner.vue'
import RegistrationForm from '../components/account/RegistrationForm.vue'
import { ref } from 'vue';

</script>

<template>
  <main>
    <HomeBanner />
    <div class="registration-container">
      <RegistrationForm />
    </div>
  </main>
</template>
