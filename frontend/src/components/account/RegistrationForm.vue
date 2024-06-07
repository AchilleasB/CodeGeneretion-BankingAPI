<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../../stores/auth';
import { useRouter } from 'vue-router'

const router = useRouter();
const authStore = useAuthStore();

const firstName = ref('');
const lastName = ref('');
const dateOfBirth = ref('');
const phone = ref('');
const bsn = ref('');
const email = ref('');
const password = ref('');
const successMessage = ref('');
const errorMessage = ref('');

const register = async () => {
  try {
    const res = await authStore.register(firstName.value, lastName.value, dateOfBirth.value, phone.value, bsn.value, email.value, password.value);

    if (res.data) {
      successMessage.value = `${res.data.firstName}, your registration request was sent successfully!\n
                              Please wait for approval.`;
    } else {
      errorMessage.value = res.response.data;
    }
    setTimeout(() => {
        successMessage.value = '';
        errorMessage.value = '';
        // firstName.value = '';
        // lastName.value = '';
        // dateOfBirth.value = '';
        // phone.value = '';
        // bsn.value = '';
        email.value = '';
        password.value = '';
        router.push({ name: 'home' });
      }, 5000);
  }
  catch (error) {
    console.log(error);
  }
}

</script>

<template>
  <div class="container mt-4">
    <h3>Registration</h3>
    <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <form @submit.prevent="register" class="registration-form">
      <!-- Form Fields in two-column layout -->
      <div class="form-row mt-5">
        <div class="form-group col-md-6">
          <label for="firstName">First Name<span class="required-star">*</span></label>
          <input v-model="firstName" type="text" class="form-control" id="firstName" required>
        </div>
        <div class="form-group col-md-6">
          <label for="lastName">Last Name<span class="required-star">*</span></label>
          <input v-model="lastName" type="text" class="form-control" id="lastName" required>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-6">
          <label for="dateOfBirth">Date of Birth<span class="required-star">*</span></label>
          <input v-model="dateOfBirth" type="date" class="form-control" id="dateOfBirth" required>
        </div>
        <div class="form-group col-md-6">
          <label for="phone">Phone number<span class="required-star">*</span></label>
          <input v-model="phone" type="text" class="form-control" id="phone" required>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-12">
          <label for="bsn">BSN<span class="required-star">*</span></label>
          <input v-model="bsn" type="text" class="form-control" id="bsn" required>
        </div>
        <div class="form-group col-md-12">
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-6">
          <label for="email">Email address<span class="required-star">*</span></label>
          <input v-model="email" type="email" class="form-control" id="email" required>
        </div>
        <div class="form-group col-md-6">
          <label for="password">Password<span class="required-star">*</span></label>
          <input v-model="password" type="password" class="form-control" id="password" required>
        </div>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Submit</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  /* Adjusted for better spacing */
  padding: 20px;
  margin: auto;
  background: #f7f7f7;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

h3 {
  text-align: center;
  color: #3f51b5;
}

.registration-form .form-group {
  padding: 10px;
}

.form-control {
  width: 100%;
  padding: .7em;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.form-row {
  display: flex;
  justify-content: space-between;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-right: 20px;
}

.form-group:last-child {
  margin-right: 0;
}

.btn-primary {
  width: 100%;
  background-color: #5c67f2;
  border: none;
  padding: 10px;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #6d78f3;
}

.alert {
  text-align: center;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 20px;
  font-size: larger;
}

.required-star {
  color: red;
  margin-left: 4px;
}

.h3 {
  text-align: center;
  color: #3f51b5;
}
</style>
