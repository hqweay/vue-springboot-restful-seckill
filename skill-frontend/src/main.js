import Vue from 'vue'
import App from './App.vue'

import axios from "axios";

Vue.config.productionTip = false
Vue.prototype.$ajax = axios;

import { getCookie, setCookie, delCookie } from "@/utils/cookie";
Vue.prototype.$cookieStore = {
  getCookie,
  setCookie,
  delCookie
}

new Vue({
  render: h => h(App),
}).$mount('#app')
