<template>
  <v-app>
    <AuthenticationView></AuthenticationView>
  </v-app>
</template>

<script>
import AuthenticationView from "@/layouts/authentication/View"

export default {
  name: "AuthenticationLayout",
  components: {
    AuthenticationView
  },
  data() {
    return {
      previousPath: null
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.previousPath = from
      console.log(from);
    });
  }
}
</script>

<style scoped>

</style>