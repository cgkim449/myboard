<template>
  <v-app>
    <v-app-bar
        app
        color="secondary"
        dark
    >
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

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

    <v-navigation-drawer
        v-model="drawer"
        app
    >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            게시판
          </v-list-item-title>
          <v-list-item-subtitle>
            myboard
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="menu in navigationMenus"
            :key="menu.title"
            link
            :to="menu.to"
        >
          <v-list-item-icon>
            <v-icon>{{ menu.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ menu.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

    </v-navigation-drawer>


    <DefaultView></DefaultView>
  </v-app>
</template>

<script>
import DefaultView from "@/layouts/default/View"

export default {
  name: "DefaultLayout",
  components: {
    DefaultView,
  },
  data() {
    return {
      drawer: false,
      navigationMenus: [
        { title: '자유게시판', icon: 'mdi-format-list-bulleted', to: '/admin/boards'},
        { title: 'Q&A', icon: 'mdi-alpha-q-box', to: '/admin/questions'},
        { title: 'FAQ', icon: 'mdi-alpha-f-box', to: '/admin/faqs'},
      ],
    }
  },
  methods: {
    switchDrawer() {
      this.drawer = !this.drawer;
    },
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