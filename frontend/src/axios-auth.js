import axios from 'axios'
import { useAuthStore } from './stores/auth';

const instance = axios.create({
    baseURL: 'http://localhost:8080/'
});

// Add a request interceptor to include the JWT token in the headers
instance.interceptors.request.use(config => {
    const authStore = useAuthStore();
    const token = authStore.token; // Adjust according to how you store the token

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
}, error => {
    return Promise.reject(error);
});

export default instance;