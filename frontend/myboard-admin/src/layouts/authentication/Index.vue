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
}
</script>

<style scoped>

</style>