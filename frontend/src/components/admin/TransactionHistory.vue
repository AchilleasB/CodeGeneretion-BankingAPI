<template>
    <div>
      <h1>Transaction History</h1>
      <table>
        <thead>
          <tr>
            <th>User Name</th>
            <th>Date</th>
            <th>Amount</th>
            <th>IBAN From</th>
            <th>IBAN To</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="transaction in transactions" :key="transaction.id">
            <td>{{ transaction.userName }}</td>
            <td>{{ transaction.timestamp }}</td>
            <td>{{ transaction.amount }}</td>
            <td>{{ transaction.ibanFrom }}</td>
            <td>{{ transaction.ibanTo }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import { useTransactionStore } from '@/stores/transaction';
  import { computed, onMounted } from 'vue';
  
  export default {
    name: 'TransactionHistory',
    setup() {
      const transactionStore = useTransactionStore();
  
      onMounted(() => {
        transactionStore.fetchTransactions();
      });
  
      return {
        transactions: computed(() => transactionStore.transactions),
      };
    },
  };
  </script>
  
  <style scoped>
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 8px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f2f2f2;
  }
  
  button {
    margin-top: 10px;
    padding: 10px 15px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #45a049;
  }
  </style>
  