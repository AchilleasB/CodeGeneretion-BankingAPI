<script setup>
import HomeBanner from '../components/HomeBanner.vue'
import RegistrationForm from '../components/account/RegistrationForm.vue'
import LoginForm from '../components/account/LoginForm.vue'
import { ref } from 'vue';

const showLoginForm = ref(true);

const switchToRegistration = () => {
  showLoginForm.value = false;
}

const switchToLogin = () => {
  showLoginForm.value = true;
}
</script>

<template>
  <main>
    <!-- <HomeBanner /> -->
    <div class="login-container" v-if="showLoginForm">
      <LoginForm @switchToRegistration="switchToRegistration" />
      <p class="mt-3 text-center" @click="switchToRegistration">Don't have a bank account?
        Register here!</p>
    </div>
    <div class="registration-container" v-else>
      <RegistrationForm @switchToLogin="switchToLogin" />
      <p class="mt-3 text-center" @click="switchToLogin">Already have an account? Log in here!
      </p>
    </div>
  </main>
</template>

<style scoped>
main {
  display: flex;
  align-items: center;
}

p {
  color: #3f51b5;
  text-decoration: underline;
  cursor: pointer;
}

p:hover {
    color: #2e89fe;
}
</style>
