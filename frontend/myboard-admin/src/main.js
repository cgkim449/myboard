import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import {formatBoardTitle, formatDate, formatQuestionNickname} from "@/utils/filters";
import BoardServicePlugin from "@/plugins/services/BoardServicePlugin";
import QuestionServicePlugin from "@/plugins/services/QuestionServicePlugin";
import AnswerServicePlugin from "@/plugins/services/AnswerServicePlugin";
import MemberServicePlugin from "@/plugins/services/MemberServicePlugin";
import FAQServicePlugin from "@/plugins/services/FAQServicePlugin";
import NoticeServicePlugin from "@/plugins/services/NoticeServicePlugin";
import ItemFormValidatorPlugin from "@/plugins/validators/ItemFormValidatorPlugin";
import {ValidationObserver, ValidationProvider} from "vee-validate";
import VueCookies from "vue-cookies";

Vue.config.productionTip = false

Vue.use(ItemFormValidatorPlugin);

Vue.use(BoardServicePlugin);
Vue.use(QuestionServicePlugin);
Vue.use(AnswerServicePlugin);
Vue.use(MemberServicePlugin);
Vue.use(FAQServicePlugin);
Vue.use(NoticeServicePlugin);

Vue.use(VueCookies);
// Vue.$cookies.config("7d");

Vue.filter("formatDate", formatDate);
Vue.filter("formatBoardTitle", formatBoardTitle);
Vue.filter("formatQuestionNickname", formatQuestionNickname);

Vue.component('ValidationObserver', ValidationObserver);
Vue.component('ValidationProvider', ValidationProvider);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
