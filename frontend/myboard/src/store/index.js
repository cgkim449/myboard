import Vue from 'vue'
import Vuex from 'vuex'
import {getTokenFromCookie, getUserNameFromCookie, saveTokenToCookie, saveUserNameToCookie} from "@/utils/cookies";
import {loginUser} from "@/api/auth";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: getTokenFromCookie() || "",
    username: getUserNameFromCookie() || "",
  },
  getters: {
    isLogin(state) {
      return state.username !== "";
    },
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setUsername(state, username) {
      state.username = username;
    },
    clearUsername(state) {
      state.username = "";
    },
    clearToken(state) {
      state.token = "";
    },
  },
  actions: {
    async LOGIN({ commit }, user) {
      const { data } = await loginUser(user);
      commit("setToken", data.token);
      commit("setUsername", data.username);
      saveTokenToCookie(data.token);
      saveUserNameToCookie(data.username);
      return data;
    },
  },
  modules: {
  }
})
