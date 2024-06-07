import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useTransactionStore = defineStore('transactionStore', {
    state: () => ({
        transactions: [],
        page: 0,
        size: 10,
        hasMore: true
    }),

    getters: {

    },

    actions: {
        async fetchCustomerTransactions(userId) {
            try {
                const response = await axios.get(`/transactions/${userId}`, {
                  params: {
                    page: this.page,
                    size: this.size
                  }
                });
                
                if (response.data.length < this.size) {
                  this.hasMore = false;
                }
                this.transactions = [...this.transactions, ...response.data];
                this.page += 1;

              } catch (error) {
                console.error('Failed to fetch transactions:', error);
              }

        },
        async deposit(transactionDTO) {
            try {
                const response = await axios.post('/transactions/atm/deposit', transactionDTO);
                return response.data;
            } catch (error) {
                throw new Error('Failed to deposit: ' + error.response.data.message);
            }
        },
        async withdraw(transactionDTO) {
            try {
                const response = await axios.post('/transactions/atm/withdraw', transactionDTO);
                return response.data;
            } catch (error) {
                throw new Error('Failed to withdraw: ' + error.response.message);
            }
        },
        async transfer(transactionDTO) {
            try {
                const response = await axios.post('/transactions/transfer', transactionDTO);
                return response.data;
            } catch (error) {
                throw new Error('Failed to transfer: ' + error.response.data.message);
            }
        },

        resetTransactions() {
            this.transactions = [];
            this.page = 0;
            this.hasMore = true;
          },

    },

});