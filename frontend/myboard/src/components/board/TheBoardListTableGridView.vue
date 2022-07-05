<template>
    <v-row>
        <v-col>
            <v-card outlined>
                <v-container fluid>
                    <v-row dense>
                        <v-col
                            v-for="board in fetchedBoardList"
                            :key="board.boardId"
                            :cols="3"
                        >
                            <v-card outlined>
                                <v-img
                                    @click="moveToBoardDetail(board.boardId)"
                                    v-bind:style="{ cursor: 'pointer'}"
                                    :src="board.thumbnailUri"
                                    class="white--text align-end"
                                    gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                    height="200px"
                                />

                                <v-card-actions>
                                    <v-col cols="auto">
                                        <small class="font-weight-bold">
                                            [{{ board.categoryName }}]
                                            {{ board.title }}
                                        </small>

                                        <br>

                                        <small
                                            class="font-weight-bold"
                                            v-if="isAdminBoard(board)"
                                        >
                                            {{ board.adminNickname }}
                                        </small>

                                        <small
                                            class="font-weight-bold"
                                            v-if="isMemberBoard(board)"
                                        >
                                            {{ board.memberNickname }}
                                        </small>

                                        <small
                                            class="font-weight-bold"
                                            v-if="isGuestBoard(board)"
                                        >
                                            {{ board.guestNickname }}
                                        </small>

                                        <br>

                                        <small>조회수: {{ board.viewCount }}</small>

                                        <br>

                                        <small>{{ board.registerDate | formatDate }}</small>

                                    </v-col>
                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import {formatDate} from "@/utils/filters";

export default {
    name: "TheBoardListTableGridView",

    props: {
        boardListTableHeaders: {
            type: Array,
        },

        fetchedBoardList: {
            type: Array,
        },
    },

    methods: {
        isMemberBoard(item) {
            return item.memberNickname !== null;
        },

        isGuestBoard(item) {
            return item.guestNickname !== null;
        },

        isAdminBoard(item) {
            return item.adminNickname !== null;
        },

        moveToBoardDetail(boardId) {

            this.$router.push({
                name: "BoardDetailView",
                params: {boardId},
                query: this.$route.query
            });
        },

        formatUpdateDateOf(board) {
            return board.registerDate === board.updateDate ? '-' : formatDate(board.updateDate);
        }
    }
}
</script>

<style scoped>

</style>