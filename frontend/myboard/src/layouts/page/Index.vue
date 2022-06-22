<template>
  <v-app>
    <PageBar></PageBar>
    <PageView></PageView>
  </v-app>
</template>

<script>
import PageBar from "@/layouts/page/Bar"
import PageView from "@/layouts/page/View"

export default {
  name: "PageLayout",
  components: {
    PageBar,
    PageView
  },
}
</script>

<style scoped>

</style>