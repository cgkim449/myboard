import Vue from 'vue'
import VueRouter from 'vue-router'
import DefaultLayout from "@/layouts/default/Index"
import AuthenticationLayout from "@/layouts/authentication/Index"
import PageNotFoundView from "@/views/PageNotFoundView";
import BoardListView from "@/views/board/BoardListView";
import LoginView from "@/views/authentication/LoginView";
import store from "@/store";
import BoardDetailView from "@/views/board/BoardDetailView";
import BoardWriteView from "@/views/board/BoardWriteView";
import BoardModifyView from "@/views/board/BoardModifyView";
import TestView from "@/views/TestView";

Vue.use(VueRouter)

const routes = [
  {
    path: "/test",
    name: "TestView",
    component: TestView,
  },
  {
    path: "/admin",
    redirect: {
      name: "BoardListView"
    }
  },
  {
    path: "/admin",
    component: DefaultLayout,
    meta: {
      requiresAuth: true,
    },
    children: [
      {
        path: "boards",
        name: "BoardListView",
        component: BoardListView,
      },
      {
        path: "boards/new",
        name: "BoardWriteView",
        component: BoardWriteView,
      },
      {
        path: "boards/:boardId",
        name: "BoardDetailView",
        component: BoardDetailView,
      },
      {
        path: "boards/:boardId/modify",
        name: "BoardModifyView",
        component: BoardModifyView,
      },
    ],
  },
  {
    path: "/admin",
    component: AuthenticationLayout,
    children: [
      {
        path: "login",
        name: "LoginView",
        component: LoginView,
      },
    ],
  },
  {
    path: "*",
    name: "PageNotFoundView",
    component: PageNotFoundView,
    meta: {
      requiresAuth: true,
    },
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next)=>{
  console.log("from: ",from.path);
  console.log("to: ",to.path);

  if(to.matched.some(record  => record.meta.requiresAuth) && !store.getters.loggedIn) {
    alert("로그인 후 이용이 가능합니다")
    next({path: '/admin/login', query: {toPath: to.path}});
    return;
  }
  next();
})

export default router
