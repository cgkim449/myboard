<template>
  <v-navigation-drawer
      v-bind="$attrs"
      app
  >
    <v-list-item>
      <v-list-item-content>
          <v-list-item-title class="text-h6">
            게시판
          </v-list-item-title>
          <v-list-item-subtitle>
            board
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
        { title: '자유게시판', icon: 'mdi-format-list-bulleted', to: '/boards'},
        { title: 'Q&A', icon: 'mdi-alpha-q-box', to: '/questions'},
        { title: 'FAQ', icon: 'mdi-alpha-f-box', to: '/faqs'},
      ],
      right: null,
    }
  },
}
</script>

<style scoped>

</style>