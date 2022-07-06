<template>
    <v-container>
        <CommonNoticeDialog
            v-on:blockNoticeCookieNotFound="fetchNotice"
            v-bind:fetchedNoticeDetail="noticeDetail"
        ></CommonNoticeDialog>

        <CommonPageTitle>
            <h2 slot="title" @click="initSearchCondition" v-bind:style="{ cursor: 'pointer' }">
                자유게시판
            </h2>
        </CommonPageTitle>

        <CommonSearchForm
            v-on:searchBtnClick="search"
            v-bind:updatedSearchCondition="searchCondition"
        ></CommonSearchForm>

        <TheBoardList
            v-on:ListViewBtnClick="switchToListView"
            v-on:GridViewBtnClick="switchToGridView"
            v-bind:updatedBoardTotalCount="boardTotalCount"
            v-bind:updatedIsListView="isListView"
            v-bind:fetchedBoardList="boardList"
        ></TheBoardList>

        <CommonPagination
            v-on:pageBtnClick="movePage"
            v-bind:updatedPage="searchCondition.page"
            v-bind:itemTotalCount="boardTotalCount"
        ></CommonPagination>

        <TheBoardListBottomNavigation/>

    </v-container>
</template>

<script>
import CommonPageTitle from "@/components/common/CommonPageTitle";
import CommonSearchForm from "@/components/common/CommonSearchForm";
import TheBoardList from "@/components/board/TheBoardList";
import CommonPagination from "@/components/common/CommonPagination";
import CommonNoticeDialog from "@/components/common/CommonNoticeDialog";
import TheBoardListBottomNavigation from "@/components/board/TheBoardListBottomNavigation";

/**
 * 게시물 목록 조회 페이지
 */
export default {
    name: "BoardListView",

    components: {
        TheBoardListBottomNavigation,
        CommonNoticeDialog,
        CommonPageTitle,
        CommonSearchForm,
        TheBoardList,
        CommonPagination
    },

    data() {
        return {
            noticeDetail: {},

            searchCondition: {
                categoryId: "0",
                keyword: "",
                fromDate: "",
                toDate: "",
                page: 1,
            },

            isListView: true,

            boardTotalCount: 0,
            boardList: [],
        };
    },

    /**
     * 게시물 목록 조회 api 호출
     *
     * @returns {Promise<void>}
     */
    async created() {
        this.updateDataByQueryString();

        await this.fetchBoardList(this.searchCondition);
    },

    watch: {
        /**
         * 쿼리스트링 변할때마다 api 호출
         * 브라우저 뒤로가기, 앞으로가기 할때 api 호출해야돼서 필요
         *
         * @returns {Promise<void>}
         */
        async "$route.query"() {
            this.updateDataByQueryString();

            await this.fetchBoardList(this.searchCondition);
        },
    },

    methods: {
        /**
         * 검색조건, 보기모드 타입 값을 쿼리스트링에서 추출
         */
        updateDataByQueryString() {
            this.updateSearchConditionByQueryString();
            this.updateTableTypeByQueryString();
        },

        /**
         * 공지사항 상세 조회 api 호출
         *
         * @returns {Promise<void>}
         */
        async fetchNotice() {
            const {data} = await this.$_noticeService.fetchLatestNoticeDetail();

            this.noticeDetail = data.noticeDetail;
        },

        /**
         * 게시물 목록 조회 api 호출
         *
         * @param searchCondition
         * @returns {Promise<void>}
         */
        async fetchBoardList(searchCondition) {
            const {data} = await this.$_boardService.fetchBoardList(searchCondition);

            this.boardList = data.boardList;
            this.boardTotalCount = data.boardTotalCount;
        },

        /**
         * 검색
         *
         * @param searchCondition
         * @returns {Promise<void>}
         */
        async search(searchCondition) {
            const newQueryString = {
                isListView : this.isListView,
                searchCondition: searchCondition,
            };

            this.updateQueryString(newQueryString);
        },

        /**
         * 검색조건 초기화
         */
        initSearchCondition() {
            const searchCondition = {
                categoryId: "0",
                keyword: "",
                fromDate: "",
                toDate: "",
                page: 1,
            };

            this.search(searchCondition);
        },

        /**
         * 보기모드 타입 값을 쿼리스트링에서 추출
         */
        updateTableTypeByQueryString() {
            this.isListView = this.$route.query.isListView ? this.$route.query.isListView === "true" : true;
        },

        /**
         * 검색조건 값을 쿼리스트링에서 추출
         */
        updateSearchConditionByQueryString() {
            const updatedCategoryId = this.$route.query.categoryId;
            const updatedKeyword = this.$route.query.keyword;
            const updatedFromDate = this.$route.query.fromDate;
            const updatedToDate = this.$route.query.toDate;
            const updatedPage = this.$route.query.page;

            this.searchCondition.categoryId = updatedCategoryId ? updatedCategoryId : "0";
            this.searchCondition.keyword = updatedKeyword ? updatedKeyword : "";
            this.searchCondition.fromDate = updatedFromDate ? updatedFromDate : "";
            this.searchCondition.toDate = updatedToDate ? updatedToDate : "";
            this.searchCondition.page = updatedPage ? Number(updatedPage) : 1;
        },

        /**
         * 보기모드 변경(리스트 모드)
         */
        switchToListView() {
            this.isListView = true;

            const newQueryString = {
                isListView : this.isListView,
                searchCondition: this.searchCondition,
            };

            this.updateQueryString(newQueryString);
        },

        /**
         * 보기 모드 변경(그리드 모드)
         */
        switchToGridView() {
            this.isListView = false;

            const newQueryString = {
                isListView : this.isListView,
                searchCondition: this.searchCondition,
            };

            this.updateQueryString(newQueryString);
        },

        /**
         * page 이동
         *
         * @param page
         */
        movePage(page) {
            this.searchCondition.page = page;

            const newQueryString = {
                isListView : this.isListView,
                searchCondition: this.searchCondition,
            };

            this.updateQueryString(newQueryString);
        },

        /**
         * 쿼리스트링 변경.
         * 변경되면 watch 에서 api 호출함
         *
         * @param newQueryString
         */
        updateQueryString(newQueryString) {
            this.$router.push({
                path: 'boards',
                query: {
                    isListView: newQueryString.isListView,
                    ...newQueryString.searchCondition
                }
            }).catch(()=>{});
        },
    },
};
</script>

