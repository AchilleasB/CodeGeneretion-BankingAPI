import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAdminStore = defineStore('adminStore', {
  state: () => ({
    approvedUsers: [],
    unapprovedUsers: [],
    isLoading: false,
    error: null,
  }),

  actions: {
    async fetchUnapprovedUsers() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.get('users/unapproved');
        this.unapprovedUsers = response.data;
      } catch (error) {
        this.error = error.message;
      } finally {
        this.isLoading = false;
      }
    },
    async declineUser(userId) {
      try {
        await axios.delete(`users/decline/${userId}`);
        this.unapprovedUsers = this.unapprovedUsers.filter(user => user.id !== userId);
      } catch (error) {
        this.error = error.message;
      }
    },
    async approveUser(userId) {
      try {
        const user = this.unapprovedUsers.find(user => user.id === userId);
        await axios.post(`users/approve/${userId}`);
        this.unapprovedUsers = this.unapprovedUsers.filter(user => user.id !== userId);
        this.approvedUsers.push({ ...user, accountCreated: false });
      } catch (error) {
        this.error = error.message;
      }
    },
    
    async fetchApprovedUsers() {
      this.isLoading = true;
      try {
        const response = await axios.get('users/approved');
        this.approvedUsers = response.data;
      } catch (error) {
        this.error = error.message;
      } finally {
        this.isLoading = false;
      }
    },
  }
});