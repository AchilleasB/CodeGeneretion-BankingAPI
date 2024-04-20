<script setup>
import { ref } from 'vue';
import { useUserStore } from '../../stores/user';

const userStore = useUserStore();

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
    const res = await userStore.register(firstName.value, lastName.value, dateOfBirth.value, phone.value, bsn.value, email.value, password.value);

    if (res.data) {
      successMessage.value = `${res.data.firstName} has successfully signed up!`;
      errorMessage.value = '';
    } else {
      errorMessage.value = res.response.data.errorMessage;
      successMessage.value = '';
    }

    setTimeout(() => {
      successMessage.value = '';
      errorMessage.value = '';
      firstName.value = '';
      lastName.value = '';
      dateOfBirth.value = '';
      phone.value = '';
      bsn.value = '';
      email.value = '';
      password.value = '';
    }, 2000);
  }
  catch (error) {
    console.log(error);
  }
}

</script>

<template>
  <div class="container fluid mt-4">
    <h3 class="d-flex justify-content-left mb-5">Registration form</h3>
    <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <form action="" method="POST" class="registration-form row">
      <!-- First Row -->
      <div class="row">
        <div class="col-md-6">
          <div class="mb-3">
            <label for="firstName" class="form-label">First Name</label>
            <input v-model="firstName"  type="text" class="form-control" id="firstName"
              name="firstName">
          </div>
        </div>
        <div class="col-md-6">
          <div class="mb-3">
            <label for="lastName" class="form-label">Last Name</label>
            <input v-model="lastName"  type="text" class="form-control" id="lastName"
              name="lastName">
          </div>
        </div>
        <div class="col-md-6">
          <div class="mb-3">
            <label for="dateOfBirth" class="form-label">Date of Birth</label>
            <input v-model="dateOfBirth" type="date" class="form-control" id="dateOfBirth" name="dateOfBirth">
          </div>
        </div>
        <div class="col-md-6">
          <div class="mb-3">
            <label for="phone" class="form-label">Phone number</label>
            <input v-model="phone" type="text" class="form-control" id="phone" name="phone">
          </div>
        </div>
        <div class="col-md-6">
          <div class="mb-5">
            <label for="bsn" class="form-label">BSN</label>
            <input v-model="bsn" type="text" class="form-control" id="bsn" name="bsn">
          </div>
        </div>
      </div>

      <!-- Second Row -->
      <div class="row">
        <div class="col-md-6">
          <div class="mb-3">
            <label for="email" class="form-label">Email address</label>
            <input v-model="email" type="email" class="form-control" id="email" name="email"
              aria-describedby="emailHelp">
            <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input v-model="password" type="password" id="password" name="password" class="form-control"
              aria-describedby="passwordHelpBlock">
            <div id="passwordHelpBlock" class="form-text">
              Your password must be 8-20 characters long, contain letters and numbers, and must not
              contain spaces,
              special
              characters, or emoji.
            </div>
          </div>
        </div>
      </div>

      <!-- Submit Button -->
      <div class="col-12 d-flex justify-content-center">
        <button @click.prevent="register" type="submit" class="btn btn-primary mt-4">Submit</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.container {
  width: 75%;
  border: 1px solid black;
  border-radius: 5px;
  padding: 10px;
  background: rgb(247, 247, 247);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.registration-form {
  display: flex;
  margin: auto;
  justify-content: center;
  width: 75%;
}

h3 {
  color: #3f51b5;
}

input {
  border: 1px solid #ccc;
  border-radius: 3px;
  padding: 1em;
  width: 75%;
  height: 2.5em;
}

.btn-primary {
  background-color: #3f51b5;
  color: #fff;
  padding: 10px 20px;
  border-radius: 3px;
  cursor: pointer;
  font-size: 1.3em;
  width: 50%;
}

.btn-primary:hover {
  background-color: #2e89fe;
}

#emailHelp,
#passwordHelpBlock,
#firstNameHelp,
#lastNameHelp {
  font-size: 0.8em;
  font-style: italic;
  width: 75%;
}
</style>