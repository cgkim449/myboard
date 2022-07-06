<template>
    <div>
        <v-row dense>
            <v-col
                cols="auto"
            >
                총 {{ updatedBoardTotalCount }} 건
            </v-col>

            <v-spacer/>

            <v-col
                cols="auto"
            >
                <v-btn icon v-bind:color="updatedGridViewBtnColor" @click="clickGridViewBtn">
                    <v-icon>mdi-view-grid-outline</v-icon>
                </v-btn>
            </v-col>

            <v-col
                cols="auto"
            >
                <v-btn icon v-bind:color="updatedListViewBtnColor" @click="clickListViewBtn">
                    <v-icon>mdi-format-list-bulleted</v-icon>
                </v-btn>
            </v-col>
        </v-row>

        <template v-if="updatedIsListView">
            <TheBoardListTableListView
                v-bind:boardListTableHeaders="boardListTableHeaders"
                v-bind:fetchedBoardList="fetchedBoardList"
            ></TheBoardListTableListView>
        </template>

        <template v-else>
            <TheBoardListTableGridView
                v-bind:boardListTableHeaders="boardListTableHeaders"
                v-bind:fetchedBoardList="fetchedBoardList"
            ></TheBoardListTableGridView>
        </template>
    </div>
</template>

<script>
import TheBoardListTableListView from "@/components/board/TheBoardListTableListView";
import TheBoardListTableGridView from "@/components/board/TheBoardListTableGridView";

/**
 * 게시물 목록 영역
 */
export default {
    name: "TheBoardList",
    components: {TheBoardListTableGridView, TheBoardListTableListView},
    /**
     * prop 은 가능한 상세하게 정의되어야 한다(최소한 타입은 명시).
     * 이유: 2가지 이점을 갖는다
     *  1. 컴포넌트를 문서화했을때 컴포넌트의 사용 방법을 상세하게 알 수 있다.
     *  2. 개발 중에, Vue 는 타입이 잘못 지정된 props 를 전달하면 경고 메시지를 표시함
     *
     * 참고 : https://kr.vuejs.org/v2/style-guide/index.html#%EC%9A%B0%EC%84%A0%EC%88%9C%EC%9C%84-A-%ED%95%84%EC%88%98
     */
    props: {
        updatedBoardTotalCount: {
            type: Number,
        },

        updatedIsListView: {
            type: Boolean,
        },

        fetchedBoardList: {
            type: Array,
        },
    },

    data() {
        return {
            boardListTableHeaders: [
                {
                    text: '카테고리',
                    align: 'center',
                    sortable: false,
                    value: 'categoryName',
                    width: '10%',
                },
                {
                    text: '',
                    align: 'center',
                    sortable: false,
                    value: 'hasAttach',
                    width: '1%',
                },
                {
                    text: '제목',
                    align: 'center',
                    sortable: false,
                    value: 'title',
                },
                {
                    text: '작성자',
                    align: 'center',
                    sortable: false,
                    value: 'nickname',
                    width: '10%',
                },
                {
                    text: '조회수',
                    align: 'center',
                    sortable: false,
                    value: 'viewCount',
                    width: '7%',
                },
                {
                    text: '등록 일시',
                    align: 'center',
                    sortable: false,
                    value: 'registerDate',
                    width: '13%',
                },
                {
                    text: '수정 일시',
                    align: 'center',
                    sortable: false,
                    value: 'updateDate',
                    width: '13%',
                },
            ],
        }
    },

    computed: {
        /**
         * 그리드 모드 버튼 클릭시 바뀐 버튼 색
         *
         * @returns {string}
         */
        updatedGridViewBtnColor() {
            return this.updatedIsListView ? 'secondary' : 'primary';
        },

        /**
         * 리스트 모드 버튼 클릭시 바뀐 버튼 색
         *
         * @returns {string}
         */
        updatedListViewBtnColor() {
            return this.updatedIsListView ? 'primary' : 'secondary';
        }
    },

    methods: {
        /**
         * 그리드 모드 버튼 클릭
         */
        clickGridViewBtn() {
            this.$emit("GridViewBtnClick");
        },

        /**
         * 리스트 모드 버튼 클릭
         */
        clickListViewBtn() {
            this.$emit("ListViewBtnClick");
        },
    }
}
</script>
