<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../../stores/auth';
import { useRouter } from 'vue-router'

const router = useRouter();
const authStore = useAuthStore();

const email = ref('');
const password = ref('');

const successMessage = ref('');
const errorMessage = ref('');

const login = async () => {
    try {
        const res = await authStore.login(email.value, password.value);

        if (res.data) {
            successMessage.value = `You have successfully logged in!`;
            errorMessage.value = '';

            setTimeout(() => {

                if (authStore.isCustomer){
                    router.push({ name: 'customer' });
                }
                if (authStore.isAdmin){
                    router.push({ name: 'admin' });
                }

            }, 2000);
        } else {
            errorMessage.value = res.response.data.message;
            successMessage.value = '';

            setTimeout(() => {
                successMessage.value = '';
                errorMessage.value = '';
                email.value = '';
                password.value = '';
            }, 3000);
        }
    } catch (error) {
        console.log(error);
    }
}
</script>

<template>
    <div class="container fluid mt-4 ">
        <h3 class="d-flex justify-content-center mb-5">Log in</h3>
        <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
        <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
        <form action="" method="POST" class="login-form row">
            <div class="col-12 mb-3">
                <label for="inputLoginEmail" class="form-label">Email</label>
                <input name="email" type="email" v-model="email" class="form-control" id="inputLoginEmail" required>
            </div>
            <div class="col-12 mb-3">
                <label for="inputLoginPassword" class="form-label">Password</label>
                <input name="password" type="password" v-model="password" class="form-control" id="inputLoginPassword" required>
            </div>
            <div class="col-12 d-flex justify-content-center">
                <button @click.prevent="login" type="submit" class="btn btn-primary mt-4">Submit</button>
            </div>
        </form>
    </div>
</template>

<style scoped>
.container {
    width: 100%;
    max-width: 500px;
     border-radius: 8px;
    padding: 20px;
    background: rgb(247, 247, 247);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.login-form {
    display: flex;
    margin: auto;
    justify-content: center;
    width: 75%;
}

h3 {
    color: #3f51b5;
    text-align: center;
}

input, button {
    width: calc(100% - 40px);
    padding: 10px 20px;
    margin-top: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-sizing: border-box;
}

input:focus {
    border-color: #5c67f2;
    outline: none;
}

button {
    background-color: #5c67f2;
    color: white;
    border: none;
    cursor: pointer;
    transition: background-color 0.3s;
}

button:hover {
    background-color: #6d78f3;
}

.alert {
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
    color: #fff;
    text-align: center;
}

.alert-success {
    background-color: #4CAF50;
}

.alert-danger {
    background-color: #f44336; 
}

</style>