import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';
import Cookies from "js-cookie";
import {memberLogin} from "@/api/auth";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: null,
    username: null,
    nickname: null,
  },
  getters: {
    loggedIn(state){
      return state.token != null;
    },
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setUsername(state, username) {
      state.username = username;
    },
    setNickname(state, nickname) {
      state.nickname = nickname;
    },
    clearToken(state) {
      state.token = null;
    },
    clearUsername(state) {
      state.username = null;
    },
    clearNickname(state) {
      state.nickname = null;
    },
  },
  actions: {
    async MEMBER_LOGIN({ commit }, member) {
      const { data } = await memberLogin(member);
      commit("setToken", data.token);
      commit("setUsername", data.username);
      commit("setNickname", data.nickname);
      return data;
    },
    MEMBER_LOGOUT({ commit }, member) {
      commit("clearToken");
      commit("clearUsername");
      commit("clearNickname");
    },
  },
  modules: {},
  plugins: [
    createPersistedState({
      storage: {
        getItem: (key) => Cookies.get(key),
        // Please see https://github.com/js-cookie/js-cookie#json, on how to handle JSON.
        setItem: (key, value) =>
            Cookies.set(key, value/*, {expires: 3, secure: true}*/),
        //TODO: 로그아웃시 쿠키 삭제
        removeItem: (key) => Cookies.remove(key),
      }
    })
  ],
})
