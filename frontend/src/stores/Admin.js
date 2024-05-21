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
                console.error("Failed to fetch unapproved users:", error);
            } finally {
                this.isLoading = false;
            }
        },
        async declineUser(userId) {
            try {
                await axios.delete(`users/decline/${userId}`);
                // Remove the user from unapprovedUsers
                this.unapprovedUsers = this.unapprovedUsers.filter(user => user.id !== userId);
            } catch (error) {
                this.error = error.message;
                console.error('Failed to reject user:', error);
            }
        },
     
        async approveUser(userId) {
            try {
                // Find the user in unapprovedUsers
                const user = this.unapprovedUsers.find(user => user.id === userId);
                await axios.post(`users/approve/${userId}`);
                // Remove the user from unapprovedUsers
                this.unapprovedUsers = this.unapprovedUsers.filter(user => user.id !== userId);
                // Add the user to approvedUsers
                this.approvedUsers.push(user);
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
                this.approvedUsers = response.data;
            } catch (error) {
                this.error = error.message;
                console.error("Failed to fetch approved users:", error);
            } finally {
                this.isLoading = false;
            }
        },
        async createAccount(userId) {
            try {
                await axios.post(`users/create-account/${userId}`);
                // Handle success, e.g., show notification
            } catch (error) {
                this.error = error.message;
                console.error('Failed to create account:', error);
            }
        }
    }
});
