<template>
  <div>
    <h5 class="card-title mb-1">Search Account by IBAN:</h5>
    <div class="search-container mb-4">
      <input v-model="searchIban" id="searchIban" class="p-2" placeholder="e.g.: NL05INHO3456000021" />
      <button class="btn-style p-2" @click="searchIbans">Search</button>
      <button class="btn-style p-2 ms-2" @click="showAllAccounts">Show all</button>
    </div>
    <table v-if="filteredAccounts.length > 0">
      <thead>
        <tr>
          <th>User Name</th>
          <th>IBAN</th>
          <th>Account Type</th>
          <th>Transaction Limit</th>
          <th>Absolute Limit</th>
          <th>User Daily Limit</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="account in filteredAccounts" :key="account.id" :class="{ 'inactive-account': !account.active }">
          <td>{{ account.userName }}</td>
          <td>{{ account.iban }}</td>
          <td>{{ account.accountType }}</td>
          <td>{{ account.transactionLimit }}</td>
          <td>{{ account.absoluteLimit }}</td>
          <td>{{ account.userDailyLimit }}</td>
          <td>
            <button class="me-1 btn-style p-2" @click="editAccount(account)" :disabled="!account.active">Edit</button>
            <button class="btn-style p-2" @click="toggleAccountStatus(account)">{{ account.active ? 'Deactivate' : 'Activate' }}</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else>
      <p>Account not found</p>
    </div>

    <div v-if="isModalOpen" class="modal show d-block">
      <div class="modal-dialog">
        <div class="modal-content bg-light">
          <div class="modal-header">
            <h5 class="modal-title">Edit Account</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="confirmEdit">
              <div class="form-group">
                <label for="transactionLimit">Transaction Limit:</label>
                <input type="number" class="form-control" id="transactionLimit"
                  v-model="selectedAccount.transactionLimit" min="1" />
              </div>
              <div class="form-group">
                <label for="absoluteLimit">Absolute Limit:</label>
                <input type="number" class="form-control" id="absoluteLimit" v-model="selectedAccount.absoluteLimit"
                  min="1" />
              </div>
              <div class="form-group">
                <label for="userDailyLimit">Daily Limit:</label>
                <input type="number" class="form-control" id="userDailyLimit" v-model="selectedAccount.userDailyLimit"
                  min="1" />
              </div>
              <button type="submit" class="btn-style p-2 mt-3">Confirm</button>
              <button type="button" class="btn-style p-2 mt-3 ms-2" @click="closeModal">Cancel</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAccountStore } from '@/stores/account';
import { onMounted, ref } from 'vue';

const accountStore = useAccountStore();
const allAccounts = ref([]);
const searchIban = ref('');
const filteredAccounts = ref([]);
const isModalOpen = ref(false);
const selectedAccount = ref({
  userName: '',
  transactionLimit: 0,
  absoluteLimit: 0,
  userDailyLimit: 0
});

onMounted(async () => {
  await accountStore.fetchAllAccountsWithUserDetails();
  allAccounts.value = accountStore.allAccounts;
  filteredAccounts.value = allAccounts.value;
});

const editAccount = (account) => {
  selectedAccount.value = { ...account };
  isModalOpen.value = true;
};

const toggleAccountStatus = async (account) => {
  await accountStore.toggleAccountStatus(account);
  account.active = !account.active;
};

const confirmEdit = async () => {
  try {
    const updatedAccountData = await accountStore.updateAccount({
      ...selectedAccount.value,
      transactionLimit: selectedAccount.value.transactionLimit,
      absoluteLimit: selectedAccount.value.absoluteLimit,
      userDailyLimit: selectedAccount.value.userDailyLimit
    });

    const index = allAccounts.value.findIndex(a => a.id === selectedAccount.value.id);

    if (index !== -1) {
      allAccounts.value[index] = {
        ...allAccounts.value[index],
        ...updatedAccountData,
        transactionLimit: selectedAccount.value.transactionLimit,
        absoluteLimit: selectedAccount.value.absoluteLimit,
        userDailyLimit: selectedAccount.value.userDailyLimit,
      };
    }

    closeModal();
  } catch (error) {
    console.error('Failed to update account:', error);
  }
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedAccount.value = {
    userName: '',
    transactionLimit: 0,
    absoluteLimit: 0,
    userDailyLimit: 0
  };
};

const searchIbans = () => {
  const iban = searchIban.value.trim().toUpperCase();

  if (!iban) {
    filteredAccounts.value = allAccounts.value;
    return;
  }

  filteredAccounts.value = allAccounts.value.filter(account => account.iban.toUpperCase() === iban);

  if (filteredAccounts.value.length === 0) {
    console.log('Account not found');
  }
};
const showAllAccounts = () => {
  filteredAccounts.value = allAccounts.value;
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

.btn-style {
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45A049;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

.modal {
  position: fixed;
  top: 0;
  left: 50%;
  width: 25%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  padding: 20px;
  border-radius: 8px;
}

.inactive-account {
  background-color: lightgray;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
