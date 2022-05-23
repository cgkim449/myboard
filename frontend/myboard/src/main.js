import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import BoardServicePlugin from "@/plugins/BoardServicePlugin";

Vue.config.productionTip = false

Vue.use(BoardServicePlugin);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
