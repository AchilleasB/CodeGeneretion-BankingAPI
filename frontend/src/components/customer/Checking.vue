<script setup>
import { onMounted, ref } from 'vue';
import { useUserStore } from '../../stores/user';
import { useAccountStore } from '../../stores/account';

const userStore = useUserStore();
const accountStore = useAccountStore();

const iban = ref('');
const balance = ref(0);
const dailyLimit = ref(0);
const transactionLimit = ref(0);
const absoluteLimit = ref(0);

onMounted(async () => {
  const checkingAccount = accountStore.getCheckingAccount[0];
  
  if (checkingAccount) {
    iban.value = checkingAccount.iban;
    balance.value = checkingAccount.balance;
    dailyLimit.value = userStore.dailyLimit;
    transactionLimit.value = checkingAccount.transactionLimit;
    absoluteLimit.value = checkingAccount.absoluteLimit;
  }
});
</script>

<template>
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
      <div class="row justify-content-center">
        <div class="col-8">
          <div class="card">
            <div class="card-body">
              <p>Total balance</p>
              <h2>€ {{ balance }}</h2>
            </div>
          </div>
        </div>
      </div>
      <div class="row justify-content-center">
        <div class="col-4">
          <div class="card">
            <div class="card-body">
              <button>Send Payment</button>
            </div>
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Daily Limit</p>
            <h3>€ {{ dailyLimit }}</h3> 
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Transaction Limit</p>
            <h3>€ {{ transactionLimit }}</h3> 
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Absolute Limit</p>
            <h3>€ {{ absoluteLimit }}</h3> 
          </div>
        </div>
      </div>
    </div>
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
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
}
</style>
