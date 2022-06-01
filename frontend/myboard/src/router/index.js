import Vue from 'vue'
import VueRouter from 'vue-router'
import DefaultLayout from "@/layouts/default/Index"
import AuthenticationLayout from "@/layouts/authentication/Index"
import BoardListView from "@/views/BoardListView";
import LoginView from "@/views/authentication/LoginView";
import SignUpView from "@/views/authentication/SignUpView";
import PageNotFoundView from "@/views/PageNotFoundView";
import BoardDetailView from "@/views/BoardDetailView";
import BoardWriteView from "@/views/BoardWriteView";
import BoardPwCheck from "@/views/BoardPwCheck";
import BoardModifyView from "@/views/BoardModifyView";
import RouterTestView from "@/views/RouterTestView";
import store from "@/store";

Vue.use(VueRouter)

const routes = [
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
        path: "/boards/:id",
        name: "BoardDetailView",
        component: BoardDetailView,
      },
      {
        path: "/boards/:id/pwCheck",
        name: "BoardPwCheck",
        component: BoardPwCheck,
      },
      {
        path: "/boards/:id/modify",
        name: "BoardModifyView",
        component: BoardModifyView,
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
    path: "/test",
    name: "RouterTestView",
    component: RouterTestView,
    meta: {
      auth: true,
    },
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
  routes
})

router.beforeEach((to, from, next)=>{
  console.log("from: ",from);
  console.log("to: ",to);
  if(to.meta.auth && !store.getters.isLogin) {
    console.log("인증이 필요합니다.")
    next('/login');
    return;
  }
  next();
})

export default router
