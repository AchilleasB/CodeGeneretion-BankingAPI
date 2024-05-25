<script setup>
import { ref, onMounted } from 'vue';
import { useTransactionStore } from '../../stores/transaction';
import { useAuthStore } from '../../stores/auth';

const transactionStore = useTransactionStore();
const authStore = useAuthStore();
const isLoading = ref(false);

const fetchMoreTransactions = async () => {
  const userId = authStore.id;
  console.log(userId);  
  if (transactionStore.hasMore && !isLoading.value) {
    isLoading.value = true;
    await transactionStore.fetchCustomerTransactions(userId);
    isLoading.value = false;
  }
};

function splitTimestamp(timestamp) {
  const [date, time] = timestamp.split(' ');
  return { date, time };
}

onMounted(async () => {
  transactionStore.resetTransactions();
  await fetchMoreTransactions();
});
</script>


<template>
  <div class="transactions-container">
    <h2>Transactions</h2>
    <div class="transactions-grid transactions-header">
      <div>Date & Time</div>
      <div>Type</div>
      <div>Amount</div>
      <div>Description</div>
      <div>IBAN From</div>
      <div>IBAN To</div>
    </div>
    <div class="transactions-list">
      <div v-for="transaction in transactionStore.transactions" :key="transaction.id" class="transactions-grid transaction-card">
        <div>
          <div>{{ splitTimestamp(transaction.timestamp).date }}</div>
          <div>{{ splitTimestamp(transaction.timestamp).time }}</div>
        </div>
        <div>{{ transaction.type }}</div>
        <div>€{{ transaction.amount }}</div>
        <div>{{ transaction.message }}</div>
        <div class="iban-from">{{ transaction.ibanFrom }}</div>
        <div class="iban-to">{{ transaction.ibanTo }}</div>
      </div>
    </div>
    <button v-if="transactionStore.hasMore" @click="fetchMoreTransactions">&#8595 Show More &#8595</button>
    <p v-if="!transactionStore.hasMore && transactionStore.transactions.length > 0">No more transactions to load.</p>
  </div>
</template>


<style scoped>
.transactions-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
}

.transactions-grid {
  display: grid;
  grid-template-columns: 1fr 0.8fr 0.8fr 2fr 1fr 1fr;
  gap: 10px;
  width: 100%;
}

.transactions-header {
  font-weight: bold;
  margin-bottom: 10px;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
}

.transaction-card {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 2px 2px 5px #ccc;
  margin: 10px 0;
  padding: 10px;
}

.transaction-card > div {
  padding: 5px 0;
}

button {
  background-color: #f9e1bf;
  color: black;
  padding: 14px 20px;
  margin: 20px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  font-size:large;
}

button:hover {
  background-color: #f9e1bf;
}

.iban-from {
  color: #ff6b6b;
}

.iban-to {
  color: green;
}

@media (max-width: 1110px) {
  .transactions-grid {
    grid-template-columns: 1fr;
  }

  .transactions-header {
    display: none;
  }

  .transaction-card {
    display: grid;
    grid-template-columns: 1fr 1fr;
  }

  .transaction-card > div::before {
    content: attr(data-label);
    font-weight: bold;
    margin-right: 10px;
  }
}
</style>
