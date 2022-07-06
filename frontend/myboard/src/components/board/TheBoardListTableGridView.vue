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
                                    @click="$_routerPush.goToBoardDetail(board.boardId, $route.query)"
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

                                        <small class="font-weight-bold">
                                            <span v-if="board.adminNickname">{{ board.adminNickname }}</span>
                                            <span v-if="board.memberNickname">{{ board.memberNickname }}</span>
                                            <span v-if="board.guestNickname">{{ board.guestNickname }}</span>
                                        </small>

                                        <br>

                                        <small>조회수: {{ board.viewCount }}</small>

                                        <br>

                                        <small>{{ board.registerDate | formatRegisterDate }}</small>
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
import {formatRegisterDate} from "@/utils/filters";

/**
 * 자유게시판 테이블 그리드 모드
 */
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
}
</script>
