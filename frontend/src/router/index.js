import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import CustomerView from '../views/CustomerView.vue'
import AdminView  from '@/views/AdminView.vue'


const routes = [
  { path: '/', component: HomeView, name: 'home'},
  { path: '/customer', component: CustomerView, name: 'customer'},
  { path: '/about', component: AboutView, name: 'about'},
  { path: '/:pathMatch(.*)*', name: 'NotFound', redirect: { name: 'home' } },
  { path: '/admin', component: AdminView, name: 'admin' },
  
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes
})

export default router
