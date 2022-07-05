<template>
    <v-row>
        <v-spacer/>
        <v-col
            cols="auto"
        >
            <v-btn
                @click="moveToBoardWrite"
                color="secondary"
            >
                글쓰기
            </v-btn>
        </v-col>
    </v-row>
</template>

<script>
export default {
    name: "TheBoardListBottomNavigation",

    methods: {
        moveToBoardWrite() {
            this.$router.push({
                name: "BoardWriteView",
                query: this.$route.query,
            });
        },
    }
}
</script>

<style scoped>

</style>