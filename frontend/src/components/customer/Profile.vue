<script>
import { useUserStore } from '../../stores/user';
import { onMounted, computed } from 'vue';

export default {
    setup() {
        const userStore = useUserStore();

        onMounted(async () => {
            if (!userStore.user) {
                await userStore.getLoggedInUser();
            }
        });

        const user = computed(() => userStore.user);

        return {
            user
        };
    }
};
</script>


<template>
  <div class="profile">
    <h1>Profile Page</h1>
    <div v-if="user" class="profile-card">
      <div class="profile-item">
        <span class="profile-label">Full Name:</span> 
        <span class="profile-value">{{ user.firstName }} {{ user.lastName }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Email:</span> 
        <span class="profile-value">{{ user.email }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Role:</span> 
        <span class="profile-value">{{ user.role }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Date of Birth:</span> 
        <span class="profile-value">{{ user.dateOfBirth }}</span>
      </div>
      <div class="profile-item">
        <span class="profile-label">Phone:</span> 
        <span class="profile-value">{{ user.phone }}</span>
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

