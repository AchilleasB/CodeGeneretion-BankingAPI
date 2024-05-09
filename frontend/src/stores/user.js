import { defineStore } from 'pinia';
import axios from '../axios-auth';

export const useUserStore = defineStore('userStore', {
    state: () => ({
        jwt: '',
        id: 0,
        // fullName: ''
    }),

    getters: {
        isAuthenticated: (state) => state.jwt !== '',
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
                return error;
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
                this.id = response.data.userId;
                this.fullName = response.data.firstName + ' ' + response.data.lastName;

                localStorage.setItem('jwt', this.jwt);
                localStorage.setItem('id', this.id);
                // localStorage.setItem('fullName', this.fullName);

                axios.defaults.headers.common['Authorization'] = 'Bearer ' + this.jwt;

                console.log(this.jwt, this.id);

                return response;
            } catch (error) {
                console.log(error);
                return error;
            }
        },

        autoLogin() {
            const jwt = localStorage.getItem('jwt');
            const id = localStorage.getItem('id');
            const fullName = localStorage.getItem('fullName');
            const email = localStorage.getItem('email');
            const role = localStorage.getItem('role');
            const dateOfBirth = localStorage.getItem('dateOfBirth');
            const phone = localStorage.getItem('phone');

            console.log(jwt, id);

            if (jwt && id) {
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + jwt;
                this.jwt = jwt;
                this.id = id;
                this.fullName = fullName;
                this.email = email;
                this.role = role;
                this.dateOfBirth = dateOfBirth;
                this.phone = phone;

                return true;
            }

            return false;

        },

        logout() {
            // clear all state fields
            this.$reset();
            // remove all from local storage
            localStorage.clear();
            axios.defaults.headers.common['Authorization'] = '';
        },

    }

});