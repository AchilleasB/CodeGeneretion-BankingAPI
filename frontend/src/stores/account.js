import { defineStore } from 'pinia';
import axios from '../axios-auth';
import { useUserStore } from './user';

export const useAccountStore = defineStore('accountStore', {
  state: () => ({
    accounts: [],
    allAccounts: [],
    error: null,
  }),

  getters: {
    getCheckingAccount: (state) => state.accounts.filter(account => account.accountType === 'CHECKING'),
    getSavingsAccount: (state) => state.accounts.filter(account => account.accountType === 'SAVINGS'),
    getAccountByType: (state) => (type) => state.accounts.find(account => account.accountType === type),
  },

  actions: {
    async getCustomerAccounts(userId) {
      this.error = null;
      try {
        const response = await axios.get(`/accounts/${userId}`);
        this.accounts = response.data;
      } catch (error) {
        this.error = error.message;
        console.error('Failed to fetch customer accounts:', error);
      }
    },

    async fetchAllAccounts() {
      try {
        const response = await axios.get('/accounts');
        this.allAccounts = await response.data;
        return this.allAccounts;

      } catch (error) {
        console.log('Failed to fetch accounts', error);

      }

    },
    
    async fetchAllAccountsWithUserDetails() {
      try {
        const response = await axios.get('/accounts');
        const accounts = response.data;

        const userStore = useUserStore();

        const accountsWithUserDetails = await Promise.all(accounts.map(async (account) => {
          await userStore.loadUserDetails(account.userId);
          return {
            ...account,
            userName: `${userStore.firstName} ${userStore.lastName}`,
            userDailyLimit: userStore.dailyLimit,
            userRole: userStore.role,
          };
        }));

        this.allAccounts = accountsWithUserDetails;
      } catch (error) {
        console.log('Failed to fetch accounts with user details', error);
      }
    },

    async createAccounts(accountData) {
      this.error = null;
      try {
        const response = await axios.post(`/accounts/${accountData.userId}`, accountData);
        this.accounts.push(response.data);
      } catch (error) {
        this.error = error.message;
        console.error('Failed to create account:', error);
      }
    },

    async updateAccount(account) {
      try {
        // Update the account limits
        await axios.put(`/accounts/${account.id}`, {
          id: account.id,
          transactionLimit: account.transactionLimit,
          absoluteLimit: account.absoluteLimit,
        });

        // Update the user's daily limit
        const userStore = useUserStore();
        await userStore.updateUserDailyLimit(account.userId, account.userDailyLimit);

        // Return only the properties you updated
        return {
          id: account.id,
          transactionLimit: account.transactionLimit,
          absoluteLimit: account.absoluteLimit,
          userDailyLimit: account.userDailyLimit,
        };
      } catch (error) {
        console.error('Error updating account:', error);
        throw error;
      }
    },

    async deactivateAccount(account) {
      try {
        await axios.put(`/accounts/deactivate/${account.id}`);
      } catch (error) {
        console.error('Error deactivating account:', error);
        throw error;
      }

    },

    async searchIbansByUsername(firstName, lastName) {
      try {
        const response = await axios.get('accounts/ibans', {
          params: {
            firstName,
            lastName
          }
        });
  
        console.log(response);
        return response;
      } catch (error) {
        console.error('Error fetching IBANs:', error);
        return error;
      }
    },

  }
  

});