import Vue from 'vue'
import VueRouter from 'vue-router'
import BoardListView from "@/views/BoardListView";

Vue.use(VueRouter)

const routes = [
  {
    path: '/boards',
    name: 'boardList',

    component: BoardListView,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
