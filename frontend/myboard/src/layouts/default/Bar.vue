<template>
  <v-app-bar
      app
      color="primary"
      dark
  >
    <v-app-bar-nav-icon @click="$emit('drawer')"></v-app-bar-nav-icon>
    <v-spacer></v-spacer>

      <template v-if="$_Cookie.getValueFromCookie('token') !== ''">
        <span>
          {{ $_Cookie.getValueFromCookie('username') }}
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
export default {
  name: "DefaultBar",
  methods: {
    logoutMember() {
      this.$_Cookie.deleteCookie("token");
      this.$_Cookie.deleteCookie("username");
      this.$_Cookie.deleteCookie("nickname");
      alert("로그아웃 되셨습니다.");
      this.$router.go();
    },
  },
}
</script>

<style scoped>

</style>