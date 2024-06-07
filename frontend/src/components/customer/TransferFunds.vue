<script setup>
import { ref, watch } from 'vue';
import { useAccountStore } from '../../stores/account';
import { useTransactionStore } from '../../stores/transaction';
import router from '@/router';

const accountStore = useAccountStore();
const transactionStore = useTransactionStore();

const props = defineProps({
  ibanFrom: String,
  totalBalance: Number,
});

const searchUsername = ref('');
const ibanResults = ref([]);
const ibanTo = ref('');
const transferAmount = ref(0);
const message = ref('');
const errorMessage = ref('');
const successMessage = ref('');

const searchIbans = async () => {
  if (searchUsername.value.trim() === '') {
    alert('Please enter a valid username.');
    return;
  }

  const [firstName, lastName] = searchUsername.value.split(' ');

  try {
    const response = await accountStore.searchIbansByUsername(firstName, lastName);
    ibanResults.value = response.data;
  } catch (error) {
    console.error('Error fetching IBANs:', error);
  }
};

const populateRecipientIban = (iban) => {
  ibanTo.value = iban;
};

// watch for changes in searchUsername and clear ibanResults
watch(searchUsername, () => {
  ibanResults.value = [];
});

const submitTransfer = async () => {
  const transactionType = 'TRANSFER';
  try {
    const transactionDTO = {
      amount: transferAmount.value,
      ibanTo: ibanTo.value,
      ibanFrom: props.ibanFrom,
      type: transactionType,
      message: message.value,
    };

    const response = await transactionStore.transfer(transactionDTO);
    console.log(response);
    successMessage.value = 'Transfer successful!';
  } catch (error) {
    errorMessage.value = 'Transaction failed: ' + error.message;
  }

  setTimeout(() => {
    successMessage.value = '';
    errorMessage.value = '';
    transferAmount.value = 0;
    ibanTo.value = '';
    message.value = '';
    searchUsername.value = '';
    ibanResults.value = [];
    router.push({ name: 'customer' })
  }, 3000);
};
</script>

<template>
  <div class="transfer-funds-container">
    <h2 class="transfer-title">Transfer Funds</h2>

    <!-- Search IBANs by Username -->
    <div class="card">
      <label for="searchUsername">Search IBANs by customer name</label>
      <div class="search-container">
        <input v-model="searchUsername" id="searchUsername" placeholder="Enter full name" />
        <button class="btn-blue" @click="searchIbans">Search</button>
      </div>
    </div>

    <!-- Display IBANs -->
    <div v-if="ibanResults.length > 0" class="iban-results">
      <h4>IBANs for {{ searchUsername }}</h4>
      <div v-for="iban in ibanResults" :key="iban.iban" @click="populateRecipientIban(iban.iban)"
        class="iban-card clickable">
        <p class="account-type">{{ iban.accountType }}</p>
        <p class="iban">{{ iban.iban }}</p>
      </div>
    </div>

    <form method="POST" class="form-container">
      <div class="form-group-row">
        <div class="card">
          <label for="ibanTo">Recipient IBAN</label>
          <input type="text" id="ibanTo" v-model="ibanTo" required />
        </div>
        <div class="card">
          <label for="transferAmount">Amount in €</label>
          <input type="number" id="transferAmount" v-model="transferAmount" required />
        </div>
      </div>
      <div class="card">
        <label for="description">Description</label>
        <textarea id="description" v-model="message" maxlength="150"
          placeholder="Enter description (max 150 characters)"></textarea>
      </div>
      <div class="card button-card">
        <button type="submit" class="btn-green" @click.prevent="submitTransfer">Submit</button>
        <button type="button" class="btn-red" @click="$emit('cancel')">Cancel</button>
      </div>
    </form>

    <div v-if="successMessage" class="successMessage">{{ successMessage }}</div>
    <div v-if="errorMessage" class="errorMessage">{{ errorMessage }}</div>
  </div>
</template>

<style scoped>
.transfer-funds-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1em;
  padding: 2rem;
  background-color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.transfer-title {
  margin-bottom: .6em;
}

.card {
  padding: 1em;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
  width: 100%;
}

.search-container {
  display: flex;
  gap: 1em;
  width: 50%;
  margin: auto;
}

.form-group-row {
  display: flex;
  gap: 1em;
}

.form-group {
  width: 100%;
}

input,
textarea {
  width: 100%;
  padding: 0.5em;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.button-card {
  display: flex;
  gap: 1rem;
}

button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background-color: gray;
  color: white;
  cursor: pointer;
}

.btn-blue {
  background-color: #007bff;
  color: white;
}

.btn-red {
  background-color: #dc3545;
  color: white;
}

.btn-green {
  border-radius: 4px;
  background-color: green;
  color: white;
  /* font-size: 1.5em; */
  /* font-weight: bold; */
  cursor: pointer;
}

.successMessage {
  color: green;
  background-color: #e6ffed;
  padding: 0.5rem;
  text-align: center;
  font-size: larger;
}

.errorMessage {
  color: red;
  background-color: #ffe6e6;
  padding: 0.5rem;
  text-align: center;
  font-size: larger;
}

.clickable {
  cursor: pointer;
  transition: background-color 0.3s;
}

.clickable:hover {
  background-color: #f0f0f0;
}

.iban-card {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: .5em .5em;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 2px 2px 5px #ccc;
}

.iban-card:hover {
  background-color: green;
  color: #f0f0f0;
}

.iban-results {
  width: 50%;
  display: flex;
  flex-direction: column;
}

.account-type {
  font-weight: bold;
}
</style>
