import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAccountStore = defineStore('accountStore', {
    state: () => ({
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
        }
    }
});