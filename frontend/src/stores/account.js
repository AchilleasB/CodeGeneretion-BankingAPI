import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAccountStore = defineStore('accountStore', {
    state: () => ({
        accounts : [],
    }),

    getters: {
        getCheckingAccount: (state) => state.accounts.filter(account => account.accountType === 'CHECKING'),
        getSavingsAccount: (state) => state.accounts.filter(account => account.accountType === 'SAVINGS'),
        getAccountByType: (state) => (type) => {
            return state.accounts.find(account => account.accountType === type);
        }
    },

    actions: {
        async getCustomerAccounts(userId) {
            try {
                const response = await axios.get(`/accounts/${userId}`);
                this.accounts = response.data;
            } catch (error) {
                console.error('Failed to fetch customer accounts:', error);
            }
        }
    
    }

});