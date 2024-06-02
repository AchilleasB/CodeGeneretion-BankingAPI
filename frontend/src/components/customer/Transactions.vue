<script setup>
import { ref, onMounted, computed } from 'vue';
import { useTransactionStore } from '../../stores/transaction';
import { useAuthStore } from '../../stores/auth';

const transactionStore = useTransactionStore();
const authStore = useAuthStore();
const isLoading = ref(false);

const startDate = ref('');
const endDate = ref('');
const minAmount = ref(null);
const maxAmount = ref(null);
const ibanFrom = ref('');
const ibanTo = ref('');
const searchApplied = ref(false);

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

const searchTransactions = () => {
  searchApplied.value = true;
  const today = new Date().toISOString().split('T')[0];
  transactionStore.searchTransactions({
    startDate: startDate.value,
    endDate: endDate.value || today, //Set end date to today if not specified
    minAmount: minAmount.value,
    maxAmount: maxAmount.value,
    ibanFrom: ibanFrom.value,
    ibanTo: ibanTo.value
  });
};

const resetSearch = () => {
  startDate.value = '';
  endDate.value = '';
  minAmount.value = null;
  maxAmount.value = null;
  ibanFrom.value = '';
  ibanTo.value = '';
  searchApplied.value = false;
  transactionStore.resetSearch();
};

const displayedTransactions = computed(() => {
  if (searchApplied.value) {
    return transactionStore.filteredTransactions;
  } else {
    return transactionStore.transactions;
  }
});

onMounted(async () => {
  transactionStore.resetTransactions();
  await fetchMoreTransactions();
});
</script>


<template>
  <div class="transactions-container">
    <h2>Transactions</h2>

    <div class="search-fields">
      <div class="search-dates">
        <div>
          <label for="start-date">Start Date:</label>
          <input type="date" id="start-date" v-model="startDate" />
        </div>
        <div>
          <label for="end-date">End Date:</label>
          <input type="date" id="end-date" v-model="endDate" />
        </div>
      </div>
      <div class="search-amounts">
        <div>
          <label for="min-amount">Min Amount:</label>
          <input type="number" id="min-amount" v-model="minAmount" />
        </div>
        <div>
          <label for="max-amount">Max Amount:</label>
          <input type="number" id="max-amount" v-model="maxAmount" />
        </div>
      </div>
      <div class="search-ibans">
        <div>
          <label for="iban-from">IBAN From:</label>
          <input type="text" id="iban-from" v-model="ibanFrom" />
        </div>
        <div>
          <label for="iban-to">IBAN To:</label>
          <input type="text" id="iban-to" v-model="ibanTo" />
        </div>
      </div>
      <div class="button-container">
        <button class="search-btn" @click="searchTransactions">Search</button>
        <button class="reset-btn" @click="resetSearch">Reset</button>
      </div>
    </div>

    <div class="transactions-grid transactions-header">
      <div>Date & Time</div>
      <div>Type</div>
      <div>Amount</div>
      <div>Description</div>
      <div>IBAN From</div>
      <div>IBAN To</div>
    </div>
    <div class="transactions-list">
      <div v-if="searchApplied && displayedTransactions.length === 0">No transactions found.</div>
      <div v-else v-for="transaction in displayedTransactions" :key="transaction.id"
        class="transactions-grid transaction-card">
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
    <button class="showMore-btn" v-if="transactionStore.hasMore" @click="fetchMoreTransactions">&#8595 Show More
      &#8595</button>
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
  grid-template-columns: 1.4fr 1.4fr 1.4fr 1.4fr 1.4fr 1fr;
  gap: 10px;
  width: 100%;
}

.transactions-header {
  font-weight: bold;
  margin-bottom: 10px;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
}

.transaction-card,
.search-fields {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 2px 2px 5px #ccc;
  margin: 10px 0;
  padding: 10px;
}

.transaction-card>div {
  padding: 5px 0;
}

button {
  color: black;
  padding: 14px 20px;
  /* margin: 20px 0; */
  border: none;
  cursor: pointer;
  width: 100%;
  font-size: large;
  border-radius: .5em;
}

.search-btn:hover {
  background-color: #28a745;
  color: white;
  transition: background-color 0.5s;
}

.reset-btn:hover {
  background-color: #dc3545;
  color: white;
  transition: background-color 0.5s;

}

.showMore-btn:hover {
  background-color: #f9e1bf;
  transition: background-color 0.3s;

}

.iban-from {
  color: #ff6b6b;
}

.iban-to {
  color: green;
}

.search-fields {
  display: flex;
  gap: 20px;
  margin: 2em 2em;
  flex-wrap: wrap;
}

.search-fields>div {
  flex: 1;
  min-width: 200px;
}

.search-fields label {
  display: block;
  margin-bottom: 5px;
}

.search-fields input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: inset 1px 1px 3px #ccc;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.search-fields input:focus {
  border-color: #28a745;
  box-shadow: inset 1px 1px 5px #aaa;
}

.button-container {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 10px;
}

@media (max-width: 1390px) {
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

  .transaction-card>div::before {
    content: attr(data-label);
    font-weight: bold;
    margin-right: 10px;
  }
}
</style>
