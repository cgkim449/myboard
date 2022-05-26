import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import BoardServicePlugin from "@/plugins/BoardServicePlugin";
import {formatBoardTitle, formatDate, hasAttachIcon} from "@/utils/filters";
import UserServicePlugin from "@/plugins/UserServicePlugin";

Vue.config.productionTip = false

Vue.use(BoardServicePlugin);
Vue.use(UserServicePlugin);

Vue.filter("formatDate", formatDate);
Vue.filter("formatBoardTitle", formatBoardTitle);
Vue.filter("hasAttachIcon", hasAttachIcon);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
