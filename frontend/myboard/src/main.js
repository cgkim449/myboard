import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import BoardServicePlugin from "@/plugins/BoardServicePlugin";
import {formatBoardTitle, formatDate, formatQuestionNickname, hasAttachIcon} from "@/utils/filters";
import MemberServicePlugin from "@/plugins/MemberServicePlugin";
import QuestionServicePlugin from "@/plugins/QuestionServicePlugin";
import VueCookies from "vue-cookies";
import AnswerServicePlugin from "@/plugins/AnswerServicePlugin";
import FAQServicePlugin from "@/plugins/FAQServicePlugin";

Vue.config.productionTip = false

Vue.use(BoardServicePlugin);
Vue.use(QuestionServicePlugin);
Vue.use(AnswerServicePlugin);
Vue.use(MemberServicePlugin);
Vue.use(FAQServicePlugin);

Vue.use(VueCookies);
Vue.$cookies.config("7d");

Vue.filter("formatDate", formatDate);
Vue.filter("formatBoardTitle", formatBoardTitle);
Vue.filter("formatQuestionNickname", formatQuestionNickname);
Vue.filter("hasAttachIcon", hasAttachIcon);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

//TODO: 로그인하고 내가 쓴 qna 상세 페이지에서 로그아웃 햇을시 처리
