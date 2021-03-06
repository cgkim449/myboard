import Vue from 'vue'
import VueRouter from 'vue-router'
import DefaultLayout from "@/layouts/default/Index"
import AuthenticationLayout from "@/layouts/authentication/Index"
import PageNotFoundView from "@/views/PageNotFoundView";
import BoardListView from "@/views/board/BoardListView";
import LoginView from "@/views/authentication/LoginView";
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
import FAQWriteView from "@/views/faq/FAQWriteView";
import NoticeListView from "@/views/notice/NoticeListView";
import NoticeWriteView from "@/views/notice/NoticeWriteView";
import NoticeDetailView from "@/views/notice/NoticeDetailView";
import NoticeModifyView from "@/views/notice/NoticeModifyView";

Vue.use(VueRouter)

const routes = [

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
      {
        path: "faqs/new",
        name: "FAQWriteView",
        component: FAQWriteView,
      },
      {
        path: "notices",
        name: "NoticeListView",
        component: NoticeListView,
      },
      {
        path: "notices/new",
        name: "NoticeWriteView",
        component: NoticeWriteView,
      },
      {
        path: "notices/:noticeId",
        name: "NoticeDetailView",
        component: NoticeDetailView,
      },
      {
        path: "notices/:noticeId/modify",
        name: "NoticeModifyView",
        component: NoticeModifyView,
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
  {
    path: "/test",
    name: "TestView",
    component: TestView,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach(async (to, from, next)=>{

  let status;

  if(to.path !== "/admin/login") {

    try{

      const response = await adminCheck();
      status = response.status;

    } catch(error) {

      status = error.response.data.status;
    }
  }

  if(to.matched.some(record  => record.meta.requiresAuth) && status !== 200) {

    alert("????????? ??? ????????? ???????????????")

    next({path: '/admin/login', query: {toPath: to.path}});
    return;
  }

  next();

})

export default router
