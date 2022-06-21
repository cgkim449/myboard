import Vue from 'vue'
import VueRouter from 'vue-router'
import DefaultLayout from "@/layouts/default/Index"
import AuthenticationLayout from "@/layouts/authentication/Index"
import BoardListView from "@/views/board/BoardListView";
import LoginView from "@/views/authentication/LoginView";
import SignUpView from "@/views/authentication/SignUpView";
import PageNotFoundView from "@/views/PageNotFoundView";
import BoardDetailView from "@/views/board/BoardDetailView";
import BoardWriteView from "@/views/board/BoardWriteView";
import BoardModifyView from "@/views/board/BoardModifyView";
import QuestionListView from "@/views/question/QuestionListView";
import QuestionWriteView from "@/views/question/QuestionWriteView";
import QuestionDetailView from "@/views/question/QuestionDetailView";
import FAQListView from "@/views/faq/FAQListView";
import store from "@/store";
import QuestionModifyView from "@/views/question/QuestionModifyView";

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    redirect: {
      name: "BoardListView"
    }
  },
  {
    path: "/",
    component: DefaultLayout,
    children: [

      {
        path: "/boards",
        name: "BoardListView",
        component: BoardListView,
      },
      {
        path: "/boards/new",
        name: "BoardWriteView",
        component: BoardWriteView,
      },
      {
        path: "/boards/:boardId",
        name: "BoardDetailView",
        component: BoardDetailView,
      },
      {
        path: "/boards/:boardId/modify",
        name: "BoardModifyView",
        component: BoardModifyView,
      },
      {
        path: "/questions",
        name: "QuestionListView",
        component: QuestionListView,
      },
      {
        path: "/questions/new",
        name: "QuestionWriteView",
        component: QuestionWriteView,
        meta: {
          auth: true,
        },
      },
      {
        path: "/questions/:questionId",
        name: "QuestionDetailView",
        component: QuestionDetailView,
      },
      {
        path: "/questions/:questionId/modify",
        name: "QuestionModifyView",
        component: QuestionModifyView,
      },
      {
        path: "/faqs",
        name: "FAQListView",
        component: FAQListView,
      },
    ],
  },
  {
    path: "/",
    component: AuthenticationLayout,
    children: [
      {
        path: "/login",
        name: "LoginView",
        component: LoginView,
      },
      {
        path: "/signUp",
        name: "SignUpView",
        component: SignUpView,
      },
    ],
  },
  {
    path: "*",
    name: "PageNotFoundView",
    component: PageNotFoundView,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

router.beforeEach((to, from, next)=>{
  console.log("from: ",from.path);
  console.log("to: ",to.path);
  if(to.meta.auth && !store.getters.loggedIn) {
    alert("로그인 후 이용이 가능합니다")
    next({path: '/login', query: {toPath: to.path}});
    return;
  }
  next();
})

export default router
