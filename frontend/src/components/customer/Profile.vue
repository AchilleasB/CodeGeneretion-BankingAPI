<script setup>
import { useUserStore } from '../../stores/user';

const userStore = useUserStore();
</script>

<template>
  <div class="profile">
    <h2>My Profile</h2>
    <div class="profile-details">
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
  padding: 40px;
  margin: auto;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.profile h2 {
  color: #333;
  margin-bottom: 30px;
}

.profile-details {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.profile-item {
  display: flex;
  justify-content: space-between;
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-label {
  font-weight: bold;
  color: #555;
  margin-right: 20px;
}

.profile-value {
  color: #777;
  flex: 1;
  text-align: right;
}

.loading {
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
  color: #777;
}
</style>
