<!-- File path: src/components/TransferFundsAdmin.vue -->
<script setup>
import { ref } from 'vue';
import { useTransactionStore } from '../../stores/transaction';

const transactionStore = useTransactionStore();

const ibanFrom = ref('');
const ibanTo = ref('');
const transferAmount = ref(0);
const errorMessage = ref('');
const successMessage = ref('');

const isValidAmount = ref(true);
const validateAmount = () => {
    if (transferAmount.value <= 0) {
        isValidAmount.value = false;
    } else {
        isValidAmount.value = true;
    }
};

const areIbansTheSame = ref(false);
const checkIbans = () => {
    if (ibanFrom.value === ibanTo.value){
      areIbansTheSame.value = true;
    } else {
      areIbansTheSame.value = false;
    }
};

const transferFunds = async () => {
  errorMessage.value = '';
  successMessage.value = '';  
  
  validateAmount();

    if (!isValidAmount.value) {
        errorMessage.value = 'Failed to transfer: Amount must be higher than €0.00';
        setTimeout(() => {
            errorMessage.value = '';
            transferAmount.value = 0;
        }, 3000);
        return;
    }

    checkIbans();

    if(areIbansTheSame.value) {
      errorMessage.value = 'Failed to transfer: IBAN of recipient and sender must be different ';
        setTimeout(() => {
            errorMessage.value = '';
            ibanTo.value = '';
        }, 3000);
        return;
    }

    try {
        const transactionDTO = {
            amount: transferAmount.value,
            ibanTo: ibanTo.value,
            ibanFrom: ibanFrom.value,
            type: "TRANSFER",
            message: "test"
        };
        const response = await transactionStore.transfer(transactionDTO);
        console.log(response);
        successMessage.value = 'Transfer successful!';
    } catch (error) {
        errorMessage.value = error.message;
    }

    setTimeout(() => {
        successMessage.value = '';
        errorMessage.value = '';
    }, 4000);
};
</script>

<template>
  <h1>Transfer Funds</h1>
  <div class="container text-center">
    <div class="row justify-content-center">
      <div class="col-6">
        <div class="card small-card">
          <div class="card-body">
            <div class="transfer-funds">
              <form method="POST">
                <div class="form-group">
                  <label for="ibanFrom">Sender's IBAN</label>
                  <input type="text" id="ibanFrom" v-model="ibanFrom" required placeholder="e.g. : NL01INHO1234567890"/>
                </div>
                <div class="form-group">
                  <label for="ibanTo">Recipient IBAN</label>
                  <input type="text" id="ibanTo" v-model="ibanTo" required placeholder="e.g. : NL01INHO0987654321"/>
                </div>
                <div class="form-group">
                  <label for="transferAmount">Amount (€)</label>
                  <input type="number" id="transferAmount" v-model="transferAmount" required placeholder="e.g. : 100"/>
                </div>
                <button type="submit" @click.prevent="transferFunds">Transfer</button>
                <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
                <p v-if="successMessage" class="success">{{ successMessage }}</p>
              </form>
            </div>
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

.small-card {
  width: 100%;
  max-width: 600px;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  font-size: 16px; 
  transition: background-color 0.3s; 
}

button:hover {
  background-color: #45A049; 
}

input {
  padding: 10px;
  margin: 10px 0;
  width: calc(100% - 20px); 
  margin-right: 20px; 
}

.form-group {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px; 
}

label {
  margin-right: 10px;
  width: 30%; 
}

.error {
  color: red;
}

.success {
  color: green;
}
</style>
