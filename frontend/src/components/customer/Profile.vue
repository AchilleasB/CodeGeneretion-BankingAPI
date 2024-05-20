<script setup>
import { useAuthStore } from '@/stores/auth';
import { useUserStore } from '../../stores/user';
import { onMounted} from 'vue';

const authStore = useAuthStore();
const userStore = useUserStore();

onMounted(async()=>{
  const userId = authStore.id;
  await userStore.loadUserDetails(userId);

})


</script>


<template>
  <div class="profile">
    <h1>Profile Page</h1>
    <div class="profile-card">
      <div class="profile-item">
        <span class="profile-label">Full Name:</span> 
        <span class="profile-value">{{ userStore.firstName }} {{ userStore.lastName }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Email:</span> 
        <span class="profile-value">{{ userStore.email }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Role:</span> 
        <span class="profile-value">{{ userStore.role }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Date of Birth:</span> 
        <span class="profile-value">{{ userStore.dateOfBirth }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Phone:</span> 
        <span class="profile-value">{{ userStore.phone }}</span>
      </div>
    </div>
  
  </div>
</template>


<style scoped>
.profile {
  padding: 20px;
  max-width: 600px;
  margin: auto;
  font-family: Arial, sans-serif;
}

.profile h1 {
  text-align: center;
  color: #333;
}

.profile-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-top: 20px;
}

.profile-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.profile-label {
  font-weight: bold;
  color: #555;
}

.profile-value {
  color: #777;
}

.loading {
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
  color: #777;
}
</style>

