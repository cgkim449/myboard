import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import boardServicePlugin from "@/plugins/services/boardServicePlugin";
import {
  formatBoardTitle,
  formatRegisterDate,
  formatQuestionNickname,
  formatUpdateDate,
  replaceCRLFWithBrTag,
} from "@/utils/filters";
import memberServicePlugin from "@/plugins/services/memberServicePlugin";
import questionServicePlugin from "@/plugins/services/questionServicePlugin";
import VueCookies from "vue-cookies";
import answerServicePlugin from "@/plugins/services/answerServicePlugin";
import faqServicePlugin from "@/plugins/services/faqServicePlugin";
import {ValidationObserver, ValidationProvider} from 'vee-validate';
import commonFormValidatorPlugin from "@/plugins/validators/commonFormValidatorPlugin";
import noticeServicePlugin from "@/plugins/services/noticeServicePlugin";
import routerPushPlugin from "@/plugins/routerPushPlugin";

Vue.config.productionTip = false

Vue.use(commonFormValidatorPlugin);

Vue.use(boardServicePlugin);
Vue.use(questionServicePlugin);
Vue.use(answerServicePlugin);
Vue.use(memberServicePlugin);
Vue.use(faqServicePlugin);
Vue.use(noticeServicePlugin);

Vue.use(routerPushPlugin);

Vue.use(VueCookies);
// Vue.$cookies.config("7d");

Vue.filter("formatRegisterDate", formatRegisterDate);
Vue.filter("formatUpdateDate", formatUpdateDate);
Vue.filter("formatBoardTitle", formatBoardTitle);
Vue.filter("formatQuestionNickname", formatQuestionNickname);
Vue.filter("replaceCRLFWithBrTag", replaceCRLFWithBrTag);

Vue.component('ValidationObserver', ValidationObserver);
Vue.component('ValidationProvider', ValidationProvider);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

//TODO: 로그인하고 내가 쓴 qna 상세 페이지에서 로그아웃 햇을시 처리
//TODO: js 리팩토링.
//  - 예외 처리
