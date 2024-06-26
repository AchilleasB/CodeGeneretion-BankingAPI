<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAccountStore } from '../../stores/account';
import { useAuthStore } from '../../stores/auth';
import { useTransactionStore } from '@/stores/transaction';
import { formatCurrency } from '@/utils/currencyFormatter';
import loadingGif from '../../assets/loading-gif.gif';

const accountStore = useAccountStore();
const transactionStore = useTransactionStore();
const authStore = useAuthStore();

const iban = ref('');
const balance = ref(0);
const successMessage = ref('');
const errorMessage = ref('');
const isLoading = ref(false);

const totalBalance = computed(() => {
  return formatCurrency(accountStore.getTotalBalance);
});

const checkingIban = ref('');
const savingsIban = ref('');
//const depositAmount = ref(0); // Independent deposit amount state
//const withdrawAmount = ref(0); // Independent withdraw amount state

onMounted(async () => {
  try {
    isLoading.value = true;
    const userId = authStore.id;
    await accountStore.getCustomerAccounts(userId);

    const savingsAccount = accountStore.getSavingsAccount[0];
    const checkingAccount = accountStore.getCheckingAccount[0];

    if (savingsAccount) {
      iban.value = savingsAccount.iban;
      savingsIban.value = savingsAccount.iban;
      balance.value = formatCurrency(savingsAccount.balance);
    }
    if (checkingAccount) {
      checkingIban.value = checkingAccount.iban;
    }
  } catch (error) {
    console.error('Error fetching accounts:', error);
    errorMessage.value = 'Failed to load accounts.';
  } finally {
    isLoading.value = false;
  }
});

const validateAmount = (amount) => {
  return !isNaN(amount) && amount > 0;
};

const resetMessages = () => {
  setTimeout(() => {
    successMessage.value = '';
    errorMessage.value = '';
  }, 3000);
};

const transferFunds = async (amount, fromCheckingToSavings) => {
  errorMessage.value = '';
  successMessage.value = '';
  isLoading.value = true;

  amount = parseFloat(amount); // Ensure amount is a number

  if (!validateAmount(amount)) {
    errorMessage.value = 'Failed to transfer: Amount must be higher than €0.00';
    isLoading.value = false;
    depositAmount.value = 0;
    withdrawAmount.value = 0;
    resetMessages();
    return;
  }

  const transactionDTO = {
    amount: amount,
    ibanFrom: fromCheckingToSavings ? checkingIban.value : savingsIban.value,
    ibanTo: fromCheckingToSavings ? savingsIban.value : checkingIban.value,
    type: 'TRANSFER',
    message: fromCheckingToSavings
      ? 'Deposit money from Checking to Saving account'
      : 'Withdraw money from Saving to Checking account',
  };

  try {
    const response = await transactionStore.transfer(transactionDTO);
    console.log(response);
    successMessage.value = 'Transfer successful!';

    // Update the local balance
    const savingsAccount = accountStore.getSavingsAccount[0];
    if (fromCheckingToSavings) {
      savingsAccount.balance += amount;
    } else {
      savingsAccount.balance -= amount;
    }

    balance.value = formatCurrency(savingsAccount.balance);
  } catch (error) {
    errorMessage.value = 'Transaction failed: ' + error.message;
  } finally {
    isLoading.value = false;
    resetMessages();
  }
};
</script>

<template>
    <p>Total Balance: {{ totalBalance }}</p>
    <h1>Savings account</h1>

    <div class="container text-center">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <p>IBAN</p>
              <h3>{{ iban }}</h3>
            </div>
          </div>
        </div>
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <p>Balance</p>
              <h2>{{ balance }}</h2>
            </div>
          </div>
        </div>
        <div class="col-6" id="transfer-forms">
          <div class="card">
            <div class="card-body from-checking">
              <p>Deposit money (CHECKING -> SAVINGS)</p>
              <input type="number" v-model.number="depositAmount" placeholder="Enter amount" />
              <button @click.prevent="transferFunds(depositAmount, true)">DEPOSIT</button>
            </div>
          </div>
          <div class="card">
            <div class="card-body to-checking">
              <p>Withdraw money (SAVINGS -> CHECKING)</p>
              <input type="number" v-model.number="withdrawAmount" placeholder="Enter amount" />
              <button @click.prevent="transferFunds(withdrawAmount, false)">WITHDRAW</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="successMessage" class="successMessage">{{ successMessage }}</div>
    <div v-if="errorMessage" class="errorMessage">{{ errorMessage }}</div>

    <!-- Loading GIF Display -->
    <img v-if="isLoading" :src="loadingGif" alt="Loading" class="loading-gif">
</template>

<style scoped>
.savings-container {
  padding: 2rem;
  background-color: #f0f0f0;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.total-balance {
  text-align: center;
  font-size: 1.2em;
  font-weight: bold;
}

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

#transfer-forms {
  display: flex;
  align-items: center;
  width: fit-content;
  margin: 0 auto;
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

input {
  padding: 10px;
  margin: 10px 0;
  width: 100%;
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

.loading-gif {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 50px;
  height: 50px;
  z-index: 1000;
}

/* @media screen and (max-width: 768px) {
  #transfer-forms {
    flex-direction: wrap;
  }
} */
</style>