<template>
  <v-app-bar
      app
      color="primary"
      dark
  >
    <v-app-bar-nav-icon @click="$emit('drawer')"></v-app-bar-nav-icon>
    <v-spacer></v-spacer>

      <template v-if="$store.getters.isLogin">
        <span>
          {{ $store.state.username }}
        </span>

        <v-btn text @click="logoutMember">
          로그아웃
        </v-btn>
      </template>

      <template v-else>
        <v-divider vertical></v-divider>

        <router-link v-bind:to="{path: `/login`}">
          <v-btn text>
            로그인
          </v-btn>
        </router-link>

        <v-divider vertical></v-divider>

        <router-link v-bind:to="{path: `/signUp`}">
          <v-btn text>
            회원가입
          </v-btn>
        </router-link>

        <v-divider vertical></v-divider>
      </template>


  </v-app-bar>
</template>

<script>
import {deleteCookie} from "@/utils/cookies";

export default {
  name: "DefaultBar",
  methods: {
    logoutMember() {
      this.$store.commit("clearToken");
      this.$store.commit("clearUsername");
      this.$store.commit("clearNickname");
      deleteCookie("token");
      deleteCookie("username");
      deleteCookie("nickname");
    },
  },
}
</script>

<style scoped>

</style>