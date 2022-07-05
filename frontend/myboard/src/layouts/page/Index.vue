<template>
  <v-app>
    <PageBar/>
    <PageView/>
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