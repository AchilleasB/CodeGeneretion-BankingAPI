<script setup>
import { RouterView } from 'vue-router'
import Footer from './components/Footer.vue'
import { useUserStore } from './stores/user'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue';

const userStore = useUserStore()
const router = useRouter()

onMounted(() => {
  userStore.autoLogin();

  if (!userStore.isAuthenticated) {
    router.push({ name: 'home' })
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
