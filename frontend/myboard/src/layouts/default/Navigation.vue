<template>
  <v-navigation-drawer
      v-bind="$attrs"
      app
  >
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title class="text-h6">
          Application
        </v-list-item-title>
        <v-list-item-subtitle>
          subtext
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>

    <v-divider></v-divider>

    <v-list
        dense
        nav
    >
      <v-list-item
          v-for="item in items"
          :key="item.title"
          link
          :to="item.to"
      >
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: "DefaultNavigation",
  data() {
    return {
      items: [
        { title: '자유게시판', icon: 'mdi-image', to: '/boards'},
        { title: '로그인', icon: 'mdi-account-arrow-left-outline', to: '/login'},
        { title: '회원가입', icon: 'mdi-view-dashboard', to: '/signUp'},
      ],
      right: null,
    }
  }
}
</script>

<style scoped>

</style>