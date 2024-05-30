<script setup>
import { onMounted, ref } from 'vue';
import { useUserStore } from '../../stores/user';
import { useAccountStore } from '../../stores/account';
import TransferFunds from './TransferFunds.vue';

const userStore = useUserStore();
const accountStore = useAccountStore();

const iban = ref('');
const balance = ref(0);
const dailyLimit = ref(0);
const transactionLimit = ref(0);
const absoluteLimit = ref(0);

const showPaymentForm = ref(false);

const togglePaymentForm = () => {
  showPaymentForm.value = !showPaymentForm.value;
};

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

const areIbansTheSame = ref(false);
const checkIbans = () => {
    if (iban.value === ibanTo.value){
      areIbansTheSame.value = true;
    } else {
      areIbansTheSame.value = false;
    }
};

const submitTransfer = async () => {
  errorMessage.value = '';
  successMessage.value = '';
  
  checkIbans();

  if(areIbansTheSame.value) {
    errorMessage.value = 'Failed to transfer: IBAN of recipient and sender must be different ';
      setTimeout(() => {
          errorMessage.value = '';
      }, 3000);
      return;
  }

  const transactionType = 'TRANSFER';
  try {
    const transactionDTO = {
      amount: transferAmount.value,
      ibanTo: ibanTo.value,
      ibanFrom: iban.value,
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
    errorMessage.value = '';
    successMessage.value = '';
  }, 4000);
};

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
    <!-- <div v-if="showPaymentForm" class="payment-form card">
      <h2>Transfer Funds</h2>
      <form method="POST">
        <div class="form-group">
          <label for="ibanTo">Recipient IBAN</label>
          <input type="text" id="ibanTo" v-model="ibanTo" required />
        </div>
        <div class="form-group">
          <label for="transferAmount">Amount (€)</label>
          <input type="number" id="transferAmount" v-model="transferAmount" required />
        </div>
        <div class="form-group">
          <label for="transferAmount">Description</label>
          <input type="text" id="description" v-model="message" required />
        </div>
        <div class="form-group button-group">
          <button type="submit" @click.prevent="submitTransfer">Submit</button>
          <button type="button" @click="togglePaymentForm">Cancel</button>
        </div>
      </form>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success">{{ successMessage }}</p>
    </div> -->

    <!-- Payment Form -->
    <TransferFunds v-if="showPaymentForm" :iban-from="iban" @cancel="togglePaymentForm" />
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
