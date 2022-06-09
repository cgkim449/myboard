<template>
  <v-app-bar
      app
      color="primary"
      dark
  >
    <v-app-bar-nav-icon @click="$emit('drawer')"></v-app-bar-nav-icon>
    <v-spacer></v-spacer>

      <template v-if="$cookies.get('token') !== null">
        <span>
          {{ $cookies.get('username') }}
        </span>

        <v-btn text @click="logoutMember">
          로그아웃
        </v-btn>
      </template>

      <template v-else>
        <v-divider vertical></v-divider>

        <v-btn text @click="loginMember">
          로그인
        </v-btn>

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
export default {
  name: "DefaultBar",
  methods: {
    logoutMember() {
      this.$cookies.remove("token");
      this.$cookies.remove("username");
      this.$cookies.remove("nickname");
      this.$cookies.remove("role");
      this.$router.go();
      alert("로그아웃 되셨습니다.");
    },
    loginMember() {
      this.$router.push({
        path: '/login'
        , query: {toPath:this.$route.path}
      });
    },
  },

}
</script>

<style scoped>

</style>