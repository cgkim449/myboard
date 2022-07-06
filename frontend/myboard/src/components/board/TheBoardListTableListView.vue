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
                        @click="$_routerPush.goToBoardDetail(item.boardId, $route.query)"
                        v-bind:style="{ cursor: 'pointer' }"
                        class="d-flex start"
                    >
                        {{ item.title | formatBoardTitle }}
                    </span>
                </template>

                <template v-slot:item.nickname="{ item }">
                    <span v-if="item.memberNickname">{{ item.memberNickname }}</span>
                    <span v-if="item.guestNickname">{{ item.guestNickname }}</span>
                    <span v-if="item.adminNickname">{{ item.adminNickname }}</span>
                </template>

                <template v-slot:item.registerDate="{ item }">
                    {{ item.registerDate | formatRegisterDate }}
                </template>

                <template v-slot:item.updateDate="{ item }">
                    {{ item.updateDate | formatUpdateDate(item.registerDate) }}
                </template>
            </v-data-table>
        </v-col>
    </v-row>
</template>

<script>
import {formatRegisterDate} from "@/utils/filters";

/**
 * 자유게시판 테이블 리스트 모드
 */
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
}
</script>
