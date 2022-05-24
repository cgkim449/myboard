<template>
  <v-app>
    <DefaultBar @drawer="drawer = !drawer"></DefaultBar>
    <DefaultNavigation v-model="drawer"></DefaultNavigation>
    <DefaultView></DefaultView>
  </v-app>
</template>

<script>
import DefaultBar from "@/layouts/default/Bar"
import DefaultNavigation from "@/layouts/default/Navigation"
import DefaultView from "@/layouts/default/View"

export default {
  name: "DefaultLayout",
  components: {
    DefaultBar,
    DefaultNavigation,
    DefaultView,
  },
  data() {
    return {
      drawer: false,
    }
  }
}
</script>

<style scoped>

</style>