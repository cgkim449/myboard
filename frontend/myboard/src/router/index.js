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

export default router
