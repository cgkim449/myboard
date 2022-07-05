<template>
    <v-row>
        <v-col
            cols="12"
        >
            <v-data-table
                dense
                :headers="boardListTableHeaders"
                :items="fetchedBoardList"
                hide-default-footer
                class="elevation-2"
            >
                <template v-slot:item.hasAttach="{ item }">
                    <v-icon
                        v-if="item.hasAttach"
                    >
                        mdi-attachment
                    </v-icon>
                </template>

                <template v-slot:item.title="{ item }">
                            <span
                                @click="moveToBoardDetail(item.boardId)"
                                v-bind:style="{ cursor: 'pointer' }"
                                class="d-flex start"
                            >
                                {{ item.title | formatBoardTitle }}
                            </span>
                </template>

                <template v-slot:item.nickname="{ item }">

                            <span v-if="isMemberBoard(item)">
                                {{ item.memberNickname }}
                            </span>

                    <span v-if="isGuestBoard(item)">
                                {{ item.guestNickname }}
                            </span>

                    <span v-if="isAdminBoard(item)">
                                {{ item.adminNickname }}
                            </span>
                </template>

                <template v-slot:item.registerDate="{ item }">
                    {{ item.registerDate | formatDate }}
                </template>

                <template v-slot:item.updateDate="{ item }">
                    {{ formatUpdateDateOf(item) }}
                </template>
            </v-data-table>
        </v-col>
    </v-row>
</template>

<script>
import {formatDate} from "@/utils/filters";

export default {
    name: "TheBoardListTableListView",
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