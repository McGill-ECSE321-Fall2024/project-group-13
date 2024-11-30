import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import BrowseView from '@/views/BrowseView.vue'
import LoginView from '@/views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import GameView from '@/views/GameView.vue'
import AccountView from '@/views/AccountView.vue'
import CartView from '@/views/CartView.vue'
import WishlistView from '@/views/WishlistView.vue'
import CheckoutView from '@/views/CheckoutView.vue'
import OwnerDashboardView from '@/views/OwnerDashboardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomeView,
    },
    {
      path: '/browse',
      name: 'Browse',
      component: BrowseView,
    },
    {
      path: '/game/:gameID', // Dynamic parameter
      name: 'Game',
      component: GameView,
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'Register',
      component: SignUpView,
    },
    {
      path: '/account',
      name: 'Account',
      component: AccountView,
    },
    {
      path: '/cart',
      name: 'Cart',
      component: CartView,
    },
    {
      path: '/wishlist',
      name: 'Wishlist',
      component: WishlistView,
    },
    {
      path: '/checkout',
      name: 'Checkout',
      component: CheckoutView,
    },
    {
      path: '/owner-dashboard',
      name: 'OwnerDashboard',
      component: OwnerDashboardView,
    },
    {
      path: '/:pathMatch(.*)*', // Catch all unmatched routes and redirect to home
      redirect: '/',
    },
  ],
})

export default router
