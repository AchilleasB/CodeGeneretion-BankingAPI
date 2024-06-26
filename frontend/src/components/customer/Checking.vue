<script setup>
import { onMounted, ref, computed } from 'vue';
import { useUserStore } from '../../stores/user';
import { useAccountStore } from '../../stores/account';
import { useAuthStore } from '../../stores/auth';
import TransferFunds from './TransferFunds.vue';
import { formatCurrency } from '@/utils/currencyFormatter'; 

const userStore = useUserStore();
const accountStore = useAccountStore();
const authStore = useAuthStore();

const iban = ref('');
const balance = ref(0);
const dailyLimit = ref(0);
const transactionLimit = ref(0);
const absoluteLimit = ref(0);
const showPaymentForm = ref(false);

const totalBalance = computed(() => {
  return formatCurrency(accountStore.getTotalBalance);
});

const togglePaymentForm = () => {
  showPaymentForm.value = !showPaymentForm.value;
};

const loadAccountDetails = async () => {
  await accountStore.getCustomerAccounts(authStore.id);
  const checkingAccount = accountStore.getCheckingAccount[0];

  if (checkingAccount) {
    iban.value = checkingAccount.iban;
    balance.value = formatCurrency(checkingAccount.balance);
    dailyLimit.value = formatCurrency(userStore.dailyLimit);
    transactionLimit.value = formatCurrency(checkingAccount.transactionLimit);
    absoluteLimit.value = formatCurrency(checkingAccount.absoluteLimit);
  }
};

onMounted(async () => {
  await loadAccountDetails();
});
</script>

<template>
  <p class="total-balance">Total Balance: {{ totalBalance }}</p>
  <h1>Checking Account</h1>
  <div class="container text-center">
    <div class="row">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <p>IBAN</p>
              <h3>{{ iban }}</h3>
            </div>
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Daily Limit</p>
            <h3>{{ dailyLimit }}</h3>
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Transaction Limit</p>
            <h3>{{ transactionLimit }}</h3>
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Absolute Limit</p>
            <h3>{{ absoluteLimit }}</h3>
          </div>
        </div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col-8">
        <div class="card">
          <div class="card-body">
            <p>Balance</p>
            <h2>{{ balance }}</h2>
          </div>
        </div>
      </div>
    </div>

    <!-- Show Payment Button -->
    <div class="row justify-content-center" v-if="!showPaymentForm">
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <button @click="togglePaymentForm">Send Payment</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Payment Form -->
    <TransferFunds v-if="showPaymentForm" :iban-from="iban" :totalBalance="totalBalance" @cancel="togglePaymentForm" @transactionSuccess="loadAccountDetails" />
  </div>
</template>

<style scoped>
h1 {
  text-align: center;
  margin: 1em 0;
}

.card {
  margin: 10px;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 2px 2px 5px #ccc;
}

button {
  padding: 10px 15px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  background-color: #28A745;
  color: white;
}

@media (min-width: 576px) {
  .form-group {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .form-group label {
    margin-right: 10px;
    width: 150px;
  }

  .button-group {
    justify-content: flex-end;
  }

  .button-group button {
    margin: 0 5px;
  }
}
</style>
