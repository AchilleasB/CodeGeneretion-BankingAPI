import { defineStore } from 'pinia';
import axios from '../axios-auth';
import { useUserStore } from './user';

export const useTransactionStore = defineStore('transactionStore', {
    state: () => ({
        transactions: [],
        page: 0,
        size: 10,
        hasMore: true,
        filteredTransactions: []
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

        async fetchTransactions() {
            try {
                const response = await axios.get('/transactions');
                const transactions = response.data;

                const userStore = useUserStore();

                const transactionsWithUserDetails = await Promise.all(transactions.map(async (transaction) => {
                    await userStore.loadUserDetails(transaction.userId);
                    return {
                        ...transaction,
                        userName: `${userStore.firstName} ${userStore.lastName}`,

                    };
                }));

                this.transactions = transactionsWithUserDetails;
            } catch (error) {
                console.log('Failed to fetch transactions with user details', error);
            }
        },

        searchTransactions({ startDate, endDate, minAmount, maxAmount, ibanFrom, ibanTo }) {
            const parseDate = dateStr => dateStr ? new Date(dateStr.split('-').reverse().join('-')) : null;
            const start = parseDate(startDate);
            console.log(start);
            const end = parseDate(endDate) || new Date();
            console.log(end);   

            this.filteredTransactions = this.transactions.filter(transaction => {
                const transactionDate = new Date(transaction.timestamp.split(' ')[0].split('-').reverse().join('-'));

                const validDate = (!start || transactionDate >= start) &&
                    (!end || transactionDate <= end);

                const validAmount = (!minAmount || transaction.amount >= minAmount) &&
                    (!maxAmount || transaction.amount <= maxAmount);

                const validIbanFrom = !ibanFrom || transaction.ibanFrom?.includes(ibanFrom);
                const validIbanTo = !ibanTo || transaction.ibanTo?.includes(ibanTo);

                const validIban = (!ibanFrom && !ibanTo) || (validIbanFrom && validIbanTo);

                return validDate && validAmount && validIban; // need to fix the date functionality
            });
        },

        resetTransactions() {
            this.transactions = [];
            this.page = 0;
            this.hasMore = true;
            this.filteredTransactions = [];
        },

        resetSearch() {
            this.filteredTransactions = [];
        },

        async deposit(transactionDTO) {
            try {
                const response = await axios.post('/transactions/atm/deposit', transactionDTO);
                return response.data;
            } catch (error) {
                console.log(error);
                throw new Error('Failed to deposit: ' + error.response.data.message);
            }
        },
        async withdraw(transactionDTO) {
            try {
                const response = await axios.post('/transactions/atm/withdraw', transactionDTO);
                return response.data;
            } catch (error) {
                console.log(error);
                throw new Error('Failed to withdraw: ' + error.response.data.message);
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


    },

});