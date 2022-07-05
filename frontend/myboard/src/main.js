import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import boardServicePlugin from "@/plugins/services/boardServicePlugin";
import {formatBoardTitle, formatDate, formatQuestionNickname,} from "@/utils/filters";
import MemberServicePlugin from "@/plugins/services/memberServicePlugin";
import QuestionServicePlugin from "@/plugins/services/questionServicePlugin";
import VueCookies from "vue-cookies";
import AnswerServicePlugin from "@/plugins/services/answerServicePlugin";
import FAQServicePlugin from "@/plugins/services/faqServicePlugin";
import {ValidationObserver, ValidationProvider} from 'vee-validate';
import commonFormValidatorPlugin from "@/plugins/validators/commonFormValidatorPlugin";
import NoticeServicePlugin from "@/plugins/services/noticeServicePlugin";

Vue.config.productionTip = false

Vue.use(commonFormValidatorPlugin);

Vue.use(boardServicePlugin);
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

//TODO: 로그인하고 내가 쓴 qna 상세 페이지에서 로그아웃 햇을시 처리
//TODO: js 리팩토링.
//  - 예외 처리
