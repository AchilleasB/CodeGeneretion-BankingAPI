<script setup>
import { onMounted, ref } from 'vue';
import { useUserStore } from '../../stores/user'
import { useAccountStore } from '../../stores/useAccountStore';

import { useRouter } from 'vue-router'

const userStore = useUserStore()

const router = useRouter()

const accountStore = useAccountStore();
const iban = ref(null);
const balance = ref(0);
const dailyLimit = ref(0);
const transactionLimit = ref(0);
const absoluteLimit = ref(0);

onMounted(async () => {
  await accountStore.loadAccounts();
  if (accountStore.checkingAccount) {
    const account = accountStore.checkingAccount;
    iban.value = account.iban;
    balance.value = account.balance;
    dailyLimit.value = account.dailyLimit;
    transactionLimit.value = account.transactionLimit;
    absoluteLimit.value = account.absoluteLimit;
  }
});

</script>

<template>
  <h1> Checking account</h1>
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
              <button> Send payment</button>
            </div>
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Daily limit</p>
            <h3>€ {{ dailyLimit }}</h3> 
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Transaction limit</p>
            <h3>€ {{ transactionLimit }}</h3> 
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <p>Absolute limit</p>
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
  /* border: 1px solid #ccc; */
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