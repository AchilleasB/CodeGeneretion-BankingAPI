import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useUserStore = defineStore('userStore', {
  state: () => ({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    email: "",
    bsn: "",
    phone: "",
    role: "",
    dailyLimit: "",
    approved: ""
  }),

  actions: {
    async loadUserDetails(userId) {
      try {
        const response = await axios.get(`/users/${userId}`);
        this.firstName = response.data.firstName;
        this.lastName = response.data.lastName;
        this.dateOfBirth = response.data.dateOfBirth;
        this.email = response.data.email;
        this.phone = response.data.phone;
        this.role = response.data.role;
        this.dailyLimit = response.data.dailyLimit;
        this.approved = response.data.approved;
        console.log(response.data);
      } catch (err) {
        console.error("Error loading user details:", err);
      }
    },
  },
});
