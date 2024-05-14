<script setup>
import { ref } from 'vue';
import { useUserStore } from '../../stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

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
              <h3>INGB02NL 2340 5678 4321</h3> // This should be replaced with the actual balance
            </div>
          </div>
        </div>
      </div>
      <div class="col-8">
        <div class="card">
          <div class="card-body">
            <p>Total balance</p>
            <h2>$13,562</h2> // This should be replaced with the actual balance
          </div>
        </div>
      </div>
      <div class="col-4">
        <div class="card">
          <div class="card-body">
            <button> Send payment</button>
          </div>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <div class="card-body">
          <p>Daily limit</p>
          <h3>$1000</h3> // to be replaced with the actual balance
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <div class="card-body">
          <p>Transaction limit</p>
          <h3>$1000</h3> // to be replaced with the actual balance
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <div class="card-body">
          <p>Absolute limit</p>
          <h3>$100</h3> // to be replaced with the actual balance
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