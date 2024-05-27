<template>
  <div>
    <table>
      <thead>
        <tr>
          <th>User Name</th>
          <th>Transaction Limit</th>
          <th>Absolute Limit</th>
          <th>User Daily Limit</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="account in allAccounts" :key="account.id" :class="{ 'inactive-account': !account.active }">
          <td>{{ account.userName }}</td>
          <td>{{ account.transactionLimit }}</td>
          <td>{{ account.absoluteLimit }}</td>
          <td>{{ account.userDailyLimit }}</td>
          <td>
            <button @click="editAccount(account)" :disabled="!account.active">Edit</button>
            <button @click="deactivateAccount(account)" :disabled="!account.active">Deactivate</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <h2>Edit Account</h2>
        <form @submit.prevent="confirmEdit">
          <label>
            Transaction Limit:
            <input type="number" v-model="selectedAccount.transactionLimit" />
          </label>
          <br />
          <label>
            Absolute Limit:
            <input type="number" v-model="selectedAccount.absoluteLimit" />
          </label>
          <br />
          <label>
            Daily Limit:
            <input type="number" v-model="selectedAccount.userDailyLimit" />
          </label>
          <br />
          <button type="submit">Confirm</button>
          <button type="button" @click="closeModal">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAccountStore } from '@/stores/account';
import { onMounted, ref } from 'vue';

const accountStore = useAccountStore();
const allAccounts = ref([]);
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
});

const editAccount = (account) => {
  selectedAccount.value = { ...account };  // Initialize selectedAccount with account details
  isModalOpen.value = true;
};

const deactivateAccount = async (account) => {
  await accountStore.deactivateAccount(account);
  account.active = false; // Assuming the account status is updated after deactivation
}

const confirmEdit = async () => {
  try {
    // Update the account on the server
    const updatedAccountData = await accountStore.updateAccount({
      ...selectedAccount.value,
      // Only send the properties you want to update to the server
      transactionLimit: selectedAccount.value.transactionLimit,
      absoluteLimit: selectedAccount.value.absoluteLimit,
      userDailyLimit: selectedAccount.value.userDailyLimit
    });

    // Find the index of the updated account in the local list
    const index = allAccounts.value.findIndex(a => a.id === selectedAccount.value.id);

    // Preserve user details and update only the relevant properties
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
    // Optionally, handle error display to the user
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
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
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
  background: rgb(220, 190, 190);
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
