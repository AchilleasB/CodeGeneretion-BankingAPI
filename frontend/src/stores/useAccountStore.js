import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAccountStore = defineStore('accountStore', {
  state: () => ({
    checkingAccount: null,
    savingsAccount: null,
    error: null,
  }),
  actions: {
    async loadAccounts() {
      this.error = null;
      const userId = localStorage.getItem('id');
      try {
        const response = await axios.get(`/accounts/${userId}`);
        const accounts = response.data;
        this.checkingAccount = accounts.find(account => account.accountType === 'CHECKING');
        this.savingsAccount = accounts.find(account => account.accountType === 'SAVINGS');
      } catch (err) {
        this.error = err.message;
      }
    },
  },
});
