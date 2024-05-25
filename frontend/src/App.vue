<script setup>
import { RouterView } from 'vue-router'
import Footer from './components/Footer.vue'
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue';

const authStore = useAuthStore()
const router = useRouter()

onMounted(() => {
  authStore.autoLogin();

  if (authStore.isCustomer) {
    router.push({ name: 'customer' });
  } else if (authStore.isAdmin) {
    router.push({ name: 'admin' });
  }
})

</script>

<template>
  <div class="app-container">
    <RouterView />
    <div class="footer">
      <Footer />
    </div>
  </div>
</template>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.footer {
  margin-top: auto;
  width: 100vw;
}
</style>
