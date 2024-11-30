import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import VueSweetalert2 from 'vue-sweetalert2';

// Import the styles
import '@sweetalert2/theme-dark/dark.min.css';

const app = createApp(App)

app.use(router)
app.use(VueSweetalert2);
app.mount('#app')
