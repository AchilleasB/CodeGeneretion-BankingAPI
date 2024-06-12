import axios from 'axios'
import { useAuthStore } from './stores/auth';

const instance = axios.create({
    baseURL: 'http://localhost:8080/'
    // baseURL: import.meta.env.VITE_API_BASE_URL // This for the deployed version
});

// Request interceptor to include the JWT token in the headers
instance.interceptors.request.use(config => {
    const authStore = useAuthStore();
    const token = authStore.token; 
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
}, error => {
    return Promise.reject(error);
});

export default instance;