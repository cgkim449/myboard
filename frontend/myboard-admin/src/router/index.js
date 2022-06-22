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
import QuestionListView from "@/views/question/QuestionListView";
import QuestionWriteView from "@/views/question/QuestionWriteView";
import QuestionDetailView from "@/views/question/QuestionDetailView";
import QuestionModifyView from "@/views/question/QuestionModifyView";
import FAQListView from "@/views/faq/FAQListView";
import {adminCheck} from "@/api/auth";

Vue.use(VueRouter)

const routes = [
  {
    path: "/test",
    name: "TestView",
    component: TestView,
  },
  {
    path: "/",
    redirect: {
      name: "BoardListView"
    }
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
      {
        path: "questions",
        name: "QuestionListView",
        component: QuestionListView,
      },
      {
        path: "questions/new",
        name: "QuestionWriteView",
        component: QuestionWriteView,
      },
      {
        path: "questions/:questionId",
        name: "QuestionDetailView",
        component: QuestionDetailView,
      },
      {
        path: "questions/:questionId/modify",
        name: "QuestionModifyView",
        component: QuestionModifyView,
      },
      {
        path: "faqs",
        name: "FAQListView",
        component: FAQListView,
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

router.beforeEach(async (to, from, next)=>{
  console.log("from: ",from.path);
  console.log("to: ",to.path);

  let status;

  try{
    const response = await adminCheck();
    status = response.status;
  } catch(error) {
    status = error.response.data.status;
  }

  if(to.matched.some(record  => record.meta.requiresAuth) && status !== 200) {
    alert("로그인 후 이용이 가능합니다")
    next({path: '/admin/login', query: {toPath: to.path}});
    return;
  }

  next();
})

export default router
