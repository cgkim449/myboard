<template>
  <v-app-bar
      app
      color="primary"
      dark
  >
    <v-app-bar-nav-icon @click="$emit('drawer')"></v-app-bar-nav-icon>
    <v-spacer></v-spacer>

      <template v-if="$store.getters.loggedIn">
        <span>
          {{ $store.state.username }}
        </span>

        <v-btn text @click="logout">
          로그아웃
        </v-btn>
      </template>

      <template v-else>
        <v-divider vertical></v-divider>

        <v-btn text @click="moveToLoginPage">
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
    logout() {
      this.$store.dispatch('LOGOUT');

      this.$router.go();

      alert("로그아웃 되셨습니다.");
    },
    moveToLoginPage() {
      this.$router.push({
        path: '/login'
        , query: {
          toPath: this.$route.path
        }
      });
    },
  },

}
</script>

<style scoped>

</style>