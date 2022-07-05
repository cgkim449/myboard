<template>
    <v-row>
        <v-spacer/>

        <v-col
            cols="auto"
        >
            <router-link
                style="text-decoration: none;"
                :to="{
                name: 'QuestionWriteView',
                query: this.$route.query
            }"
            >
                <v-btn
                    color="secondary"
                >
                    질문하기

                </v-btn>
            </router-link>
        </v-col>
    </v-row>
</template>

<script>
export default {
    name: "TheQuestionListBottomNavigation"
}
</script>

<style scoped>

</style>