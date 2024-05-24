import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAdminStore = defineStore('adminStore', {
  state: () => ({
    approvedUsers: [],
    unapprovedUsers: [],
    usersWithCreatedAccounts: [],
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
        console.error('Failed to fetch unapproved users:', error);
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
        console.error('Failed to reject user:', error);
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
        console.error('Failed to approve user:', error);
      }
    },
    async fetchApprovedUsers() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.get('users/approved');
        this.approvedUsers = response.data.map(user => ({ ...user, accountCreated: false }));
      } catch (error) {
        this.error = error.message;
        console.error('Failed to fetch approved users:', error);
      } finally {
        this.isLoading = false;
      }
    },
    async markAccountCreated(userId) {
      const userIndex = this.approvedUsers.findIndex(user => user.id === userId);
      if (userIndex !== -1) {
        const user = this.approvedUsers.splice(userIndex, 1)[0];
        user.accountCreated = true;
        this.usersWithCreatedAccounts.push(user);
      }
    }
  }
});
