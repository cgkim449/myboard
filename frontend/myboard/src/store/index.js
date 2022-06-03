import Vue from 'vue'
import Vuex from 'vuex'
import {
  getValueFromCookie,
  setValueToCookie,
} from "@/utils/cookies";
import {loginUser} from "@/api/auth";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: getValueFromCookie("token") || "",
    username: getValueFromCookie("username") || "",
    nickname: getValueFromCookie("nickname") || "",
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
    clearToken(state) {
      state.token = "";
    },
    setUsername(state, username) {
      state.username = username;
    },
    clearUsername(state) {
      state.username = "";
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
    clearNickname(state) {
      state.nickname = "";
    },
  },
  actions: {
    async LOGIN({ commit }, user) {
      const { data } = await loginUser(user);
      commit("setToken", data.token);
      commit("setUsername", data.username);
      commit("setNickname", data.nickname);
      setValueToCookie("token", data.token);
      setValueToCookie("username", data.username);
      setValueToCookie("nickname", data.nickname);
      return data;
    },
  },
  modules: {
  }
})
