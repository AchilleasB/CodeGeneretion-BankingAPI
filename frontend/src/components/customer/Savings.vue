<script setup>
import { onMounted, ref } from 'vue';
import { useUserStore } from '../../stores/user';
import { useAccountStore } from '../../stores/account';

const userStore = useUserStore();
const accountStore = useAccountStore();

const iban = ref('');
const balance = ref(0);

onMounted(async () => {
  const savingsAccount = accountStore.getSavingsAccount[0];

  if (savingsAccount) {
    iban.value = savingsAccount.iban;
    balance.value = savingsAccount.balance;
  }
});



</script>

<template>
  <h1>Savings account</h1>
  <div class="container text-center">
    <div class="row">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <p>IBAN</p>
              <h3>€ {{ iban }}</h3> 
            </div>
          </div>
        </div>
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <p>Total balance</p>
              <h2>€ {{ balance }}</h2>
            </div>
          </div>
        </div>
        <div class="col-6" id="handle-balance">
          <div class="card">
            <div class="card-body">
              <div class="from-checking">
                <p>Transfer FROM checking account</p>
                <input type="number" placeholder="Enter amount" />
                <button> Transfer IN</button>
              </div>
            </div>
          </div>
          <div class="col-6">
            <div class="card">
              <div class="card-body">
                <div class="to-checking">
                  <p>Transfer TO checking account</p>
                  <input type="number" placeholder="Enter amount" />
                  <button> Transfer OUT</button>
                </div>
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
  /* border: 1px solid #ccc; */
  border-radius: 5px;
  box-shadow: 2px 2px 5px #ccc;
}

#handle-balance {
  display: flex;
  justify-content: center;
  align-items: center;
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
</style>