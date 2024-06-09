import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useAuthStore = defineStore('authStore', {
    state: () => ({
        jwt: '',
        id: 0,
        firstName: '',
        role: '',
    }),

    getters: {
        isCustomer: (state) => state.role === 'Customer',
        isAdmin: (state) => state.role === 'Employee',
    },

    actions: {

        async register(firstName, lastName, dateOfBirth, phone, bsn, email, password) {
            try {
                const formatedDate = new Date(dateOfBirth).toISOString().split('T')[0];
                const response = await axios.post('auth/register', {
                    firstName: firstName,
                    lastName: lastName,
                    dateOfBirth: formatedDate,
                    phone: phone,
                    bsn: bsn,
                    email: email,
                    password: password
                });
                console.log(response);

                return response;
            } catch (error) {
                console.log(error);
                throw new Error(error.response.data.message);
            }
        },

        async login(email, password) {
            try {
                const response = await axios.post('auth/login', {
                    email: email,
                    password: password
                });
                console.log(response);

                this.jwt = response.data.jwtToken;
                const user = response.data.user;
                console.log(user);
                this.id = user.id;
                this.firstName = user.firstName;
                this.role = user.role;

                localStorage.setItem('jwt', this.jwt);
                localStorage.setItem('id', this.id);
                localStorage.setItem('firstName', this.firstName);
                localStorage.setItem('role', this.role);

                axios.defaults.headers.common['Authorization'] = 'Bearer ' + this.jwt;

                console.log(this.jwt, this.id, this.firstName, this.role);

                return response;
            } catch (error) {
                console.log(error);
                throw new Error(error.response.data.message);
            }
        },

        autoLogin() {
            const jwt = localStorage.getItem('jwt');
            const id = localStorage.getItem('id');
            const firstName = localStorage.getItem('firstName');
            const role = localStorage.getItem('role');

            console.log(jwt, id);

            if (jwt && id) {
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + jwt;
                this.jwt = jwt;
                this.id = id;
                this.firstName = firstName;
                this.role = role;

                return true;
            }

            return false;

        },

        async logout() {
            
            try {
                const response = await axios.post('auth/logout');
                console.log(response);
                this.$reset();
                localStorage.clear();
                axios.defaults.headers.common['Authorization'] = '';
                return response;
            } catch (error) {
                console.log(error);
                throw new Error(error.response.data.message);

            }
        },

    }

});