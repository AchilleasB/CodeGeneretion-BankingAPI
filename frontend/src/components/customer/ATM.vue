<script setup>
import { ref, computed } from 'vue';
import { useTransactionStore } from '@/stores/transaction';
import { useAccountStore } from '@/stores/account';

const transactionStore = useTransactionStore();
const accountStore = useAccountStore();

const accountType = ref('CHECKING');
const transactionType = ref('WITHDRAW');
const amount = ref(10);
const successMessage = ref('');
const errorMessage = ref('');

const isValidAmount = ref(true);
const validateAmount = () => {
    if (amount.value < 10) {
        isValidAmount.value = false;
    } else {
        isValidAmount.value = true;
    }
};

const selectTransactionType = (type) => {
    transactionType.value = type;
};

const submitATMTransaction = async () => {

    validateAmount();

    if (!isValidAmount.value) {
        errorMessage.value = 'Amount must be at least €10.00';
        setTimeout(() => {errorMessage.value = ''; amount.value = 10; }, 3000);
        return;
    }

    const selectedAccount = accountStore.getAccountByType(accountType.value);

    const transactionDTO = {
        accountId: selectedAccount.id,
        amount: amount.value,
        transactionType: transactionType.value
    };

    try {
        let response;
        if (transactionType.value === 'DEPOSIT') {
            response = await transactionStore.deposit(transactionDTO);
        } else if (transactionType.value === 'WITHDRAW') {
            response = await transactionStore.withdraw(transactionDTO);
        }
        console.log(response);
        successMessage.value = 'Transaction successful';
    } catch (error) {
        errorMessage.value = error.message;
    }


    setTimeout(() => {
        successMessage.value = '';
        errorMessage.value = '';
        amount.value = 10;
    }, 3000);

};
</script>

<template>
    <div class="atm-container">
        <h2>ATM Simulator</h2>
        <div class="row">
            <div class="col-12">
                <div class="card-row">
                    <div class="transaction-info"></div>
                    <div class="card button-card">
                        <label for="transactionType">Select Transaction Type:</label>
                        <button :class="{ active: transactionType === 'WITHDRAW' }"
                            @click="selectTransactionType('WITHDRAW')">
                            WITHDRAW
                        </button>
                        <button :class="{ active: transactionType === 'DEPOSIT' }"
                            @click="selectTransactionType('DEPOSIT')">
                            DEPOSIT
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-8">
                <div class="card-row">
                    <div class="card">
                        <label for="accountType">Select Account:</label>
                        <select v-model="accountType" id="accountType">
                            <option value="CHECKING">CHECKING</option>
                            <option value="SAVINGS" :disabled="transactionType === 'WITHDRAW'">SAVINGS</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="card-row">
                    <div class="card">
                        <label for="amount">Type Amount in €:</label>
                        <input type="number" v-model.number="amount" id="amount" min="10" required />
                    </div>
                    <div class="card">
                        <button class="submit-btn" @click="submitATMTransaction">Submit</button>
                    </div>
                </div>
            </div>
            <div v-if="successMessage" class="successMessage">{{ successMessage }}</div>
            <div v-if="errorMessage" class="errorMessage">{{ errorMessage }}</div>
        </div>
    </div>
</template>


<style scoped>
.atm-container {
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

/* h2 {
    margin-bottom: 2em;
} */

.row {
    gap: 1em;
    justify-content: center;
}

.card {
    padding: 1em;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
}

#accountType,
#amount {
    width: 100%;
    padding: 0.5em;
    border-radius: 4px;
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

.submit-btn {
    border-radius: 4px;
    background: linear-gradient(90deg, #4e54c8 20%, #8f94fb 40%, #FF7B83 90%);
    color: white;
    font-size: 1.5em;
    font-weight: bold;
    cursor: pointer;
}

button.active {
    background-color: green;
}

button:disabled {
    cursor: not-allowed;
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
</style>