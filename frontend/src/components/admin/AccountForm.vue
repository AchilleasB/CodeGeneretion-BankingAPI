<template>
  <div class="account-form">
    <h2>Create Account</h2>
    <form @submit.prevent="submitForm">
      <div>
        <label for="balance">Initial Balance:</label>
        <input type="number" id="balance" v-model="account.balance" required min="0" />
      </div>
      <div>
        <label for="absoluteLimit">Absolute Limit:</label>
        <input type="number" id="absoluteLimit" v-model="account.absoluteLimit" required min="0" />
      </div>
      
      <div>
        <label for="transactionLimit">Transaction Limit:</label>
        <input type="number" id="transactionLimit" v-model="account.transactionLimit" required min="0" />
      </div>
      <button v-if="!accountCreated" type="submit">Create Account</button>
    </form>
  </div>
</template>

<script>
import { useAccountStore } from '@/stores/account';

export default {
  name: 'AccountForm',
  props: {
    userId: {
      type: String,
      required: true,
    },
    accountCreated: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      account: {
        balance: 0,
        absoluteLimit: 0,
        transactionLimit: 0,
        active: true,
      },
    };
  },
  methods: {
    async submitForm() {
      const accountStore = useAccountStore();
      try {
        await accountStore.createAccounts({ ...this.account, userId: this.userId });
        this.$emit('accountCreated', this.userId); // Emit the event with userId
      } catch (error) {
        console.error('Failed to create account:', error);
      }
    },
  },
};
</script>

<style scoped>
.account-form {
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.account-form h2 {
  margin-bottom: 20px;
  color: #333;
}

.account-form form {
  display: flex;
  flex-direction: column;
}

.account-form div {
  margin-bottom: 10px;
}

.account-form label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}

.account-form input,
.account-form select {
  padding: 8px;
  width: 100%;
  box-sizing: border-box;
}

.account-form button {
  padding: 10px 15px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.account-form button:hover {
  background-color: #218838;
}
</style>
