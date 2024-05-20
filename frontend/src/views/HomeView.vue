<script setup>
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
  <main class="main-container">
    <div class="image-container">
      <img src="@/assets/banking_image.png" alt="Banking Image" />
    </div>
    <div class="form-container">
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
    </div>
  </main>
</template>


<style scoped>
.main-container {
  display: grid;
  grid-template-columns:1fr 1fr;
  align-items: center;
  height: 100vh;
}

.image-container {
  background-size: cover;
  background-position: center;
}

.image-container img {
  width: 100%;
  object-fit: cover;
  padding-left: 2em;
}

.form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

p {
  color: #3f51b5;
  text-decoration: underline;
  cursor: pointer;
}

p:hover {
  color: #2e89fe;
}

@media (max-width: 768px) {
  .main-container {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }
}
</style>
