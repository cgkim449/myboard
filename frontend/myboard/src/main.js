import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import BoardServicePlugin from "@/plugins/BoardServicePlugin";
import {formatBoardTitle, formatDate, hasAttachIcon} from "@/utils/filters";
import MemberServicePlugin from "@/plugins/MemberServicePlugin";
import CookiePlugin from "@/plugins/CookiePlugin";

Vue.config.productionTip = false

Vue.use(BoardServicePlugin);
Vue.use(MemberServicePlugin);
Vue.use(CookiePlugin);

Vue.filter("formatDate", formatDate);
Vue.filter("formatBoardTitle", formatBoardTitle);
Vue.filter("hasAttachIcon", hasAttachIcon);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

