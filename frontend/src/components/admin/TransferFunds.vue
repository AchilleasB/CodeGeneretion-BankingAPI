<script setup>
import { ref, watch } from 'vue';
import { useTransactionStore } from '../../stores/transaction';
import { useAccountStore } from '../../stores/account';
import loadingGif from '../../assets/loading-gif.gif';

const transactionStore = useTransactionStore();
const accountStore = useAccountStore();

const ibanFrom = ref('');
const ibanTo = ref('');
const transferAmount = ref(0);
const description = ref('');
const errorMessage = ref('');
const successMessage = ref('');
const isLoading = ref(false);

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
  isLoading.value = true;
  
  validateAmount();

    if (!isValidAmount.value) {
        errorMessage.value = 'Failed to transfer: Amount must be higher than €0.00';
        isLoading.value = false;
        transferAmount.value = 0;
        setTimeout(() => {
            errorMessage.value = '';
        }, 3000);
        return;
    }

    checkIbans();

    if(areIbansTheSame.value) {
      errorMessage.value = 'Failed to transfer: IBAN of recipient and sender must be different ';
      isLoading.value = false;
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
            message: description.value
        };
        const response = await transactionStore.transfer(transactionDTO);
        console.log(response);
        successMessage.value = 'Transfer successful!';
    } catch (error) {
      console.log(error);
      errorMessage.value = error.message;
    }

    isLoading.value = false;

    setTimeout(() => {
        successMessage.value = '';
        errorMessage.value = '';
    }, 4000);
};

// Сode for searching IBANs
const searchUsername = ref('');
const ibanResults = ref([]);
const ibanField = ref('');

const searchIbans = async () => {
  if (searchUsername.value.trim() === '') {
    alert('Please enter a valid username.');
    return;
  }

  const [firstName, lastName] = searchUsername.value.split(' ');
  isLoading.value = true;
  try {
    console.log(accountStore)
    const response = await accountStore.searchIbansByUsername(firstName, lastName);
    ibanResults.value = response.data;
  } catch (error) {
    console.error('Error fetching IBANs 2:', error);
  }
  isLoading.value = false;
};

const populateRecipientIban = (iban) => {
  if (ibanField.value === 'sender') {
    ibanFrom.value = iban;
  } else if (ibanField.value === 'recipient') {
    ibanTo.value = iban;
  }
};

// watch for changes in searchUsername and clear ibanResults
watch(searchUsername, () => {
  ibanResults.value = [];
});
</script>

<template>
  <h1>Transfer Funds Menu</h1>
  <div class="container">
    <div class="row">
      <!-- Left column: Transfer funds form -->
      <div class="col-12 col-md-6">
        <div class="card small-card">
          <div class="card-body pt-0">
            <h5 class="card-title">Form to transfer funds</h5>
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
                <div class="form-group">
                  <label for="description">Description</label>
                  <input type="text" id="description" v-model="description" placeholder="e.g. : Business expenses"/>
                </div>
                <button type="submit" class="wide-button" @click.prevent="transferFunds">Transfer</button>
                <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
                <p v-if="successMessage" class="success">{{ successMessage }}</p>
              </form>
              <img v-if="isLoading" :src="loadingGif" alt="Loading" class="loading-gif">
            </div>
          </div>
        </div>
      </div>

      <!-- Right column: Search IBANs by Username -->
      <div class="col-12 col-md-6">
        <div class="card small-card">
          <h5 class="card-title">Search IBANs by customer name</h5>
          <div class="search-container">
            <input v-model="searchUsername" id="searchUsername" class="no-right-padding" placeholder="e.g. : John Doe" />
            <button class="btn-blue" @click="searchIbans">Search</button>
          </div>

          <!-- Display IBANs -->
          <div v-if="ibanResults.length > 0" class="iban-results">
            <h4 class="iban-results-title">IBANs for {{ searchUsername }}</h4>
            <div v-for="iban in ibanResults" :key="iban.iban" class="iban-card">
              <p class="account-type">{{ iban.accountType }}</p>
              <p class="iban">{{ iban.iban }}</p>
              <div class="iban-buttons">
                <button @click="ibanField = 'sender'; populateRecipientIban(iban.iban)">Sender's IBAN</button>
                <button @click="ibanField = 'recipient'; populateRecipientIban(iban.iban)">Recipient's IBAN</button>
              </div>
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
  transition: background-color 0.3s; 
}

button:hover {
  background-color: #45A049; 
}

.wide-button {
  width: 100%;
  font-size: 16px;
}

input {
  padding: 10px;
  margin: 10px 0;
  width: calc(100% - 20px); 
  margin-right: 20px; 
}

.no-right-padding {
  margin-right: 0;
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

.card-title {
  text-align: center;
  font-weight: bold;
  margin-bottom:15px;
  margin-top: 5px;
}

.error {
  color: red;
}

.success {
  color: green;
}

.loading-gif {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 50px;
  height: 50px;
  z-index: 1000;
}

.search-container {
  display: flex;
  gap: 1em;
}

.iban-results {
  display: flex;
  flex-direction: column;
}

.iban-card {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: .5em 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 2px 2px 5px #ccc;
}

.iban-results-title {
  padding-top: 10px;
}

.account-type {
  font-weight: bold;
}

.iban-buttons {
  display: flex;
  gap: 0.5em;
}

button {
  margin-top: 10px;
  width: auto;
  padding: 10px;
}

#searchUsername {
  width: calc(100% - 20px); 
  padding: 10px;
}

@media (max-width: 768px) {
  .row {
    flex-direction: column-reverse;
  }
}
</style>
