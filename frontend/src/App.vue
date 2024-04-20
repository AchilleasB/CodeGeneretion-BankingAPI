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
    <div class="router-view-container">
      <RouterView />
    </div>
    <div class="footer">
    <Footer />
    </div>
  </div>
</template>

<style scoped>
.app-container {
  width: 85vw;
}

.router-view-container {
  display: flex; 
  flex-direction: column;
  align-items: center;
  justify-content: center;
  /* border: solid 2px aqua; */
  margin: auto;
}

.footer {
  margin-top: auto;
}
</style>
