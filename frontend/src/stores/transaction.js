import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useTransactionStore = defineStore('transactionStore', {
    state: () => ({

    }),

    getters: {

    },

    actions: {
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

    },

});