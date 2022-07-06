<template>
    <v-row>
        <v-spacer/>

        <v-col
            cols="auto"
        >
            <v-btn
                @click="$_routerPush.goToBoardWrite($route.query)"
                color="secondary"
            >
                글쓰기
            </v-btn>
        </v-col>
    </v-row>
</template>

<script>
/**
 * 자유게시판 하단 네비게이션
 */
export default {
    name: "TheBoardListBottomNavigation",
}
</script>
