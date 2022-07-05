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

    async created() {
        this.updateSearchConditionByQuery();
        this.updateIsListViewByQuery();

        await this.fetchBoardList(this.searchCondition);
    },

    computed: {},

    watch: {
        async "$route.query"() {
            this.updateSearchConditionByQuery();
            this.updateIsListViewByQuery();

            await this.fetchBoardList(this.searchCondition);
        },
    },

    methods: {
        async fetchNotice() {
            try {
                const {data} = await this.$_noticeService.fetchLatestNoticeDetail();

                this.noticeDetail = data.noticeDetail;

            } catch (error) {
                console.log(error.response.data.errorMessages)
            }
        },

        async fetchBoardList(searchCondition) {
            try {

                const {data} = await this.$_boardService.fetchBoardList(searchCondition);

                this.boardList = data.boardList;
                this.boardTotalCount = data.boardTotalCount;

            } catch (error) {
                console.log(error.response.data.errorMessages)
            }
        },

        async search(searchCondition) {
            this.updateQueryParameter(this.isListView, searchCondition);

            await this.fetchBoardList(searchCondition);
        },

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

        updateIsListViewByQuery() {
            this.isListView = this.$route.query.isListView === undefined ? true : this.$route.query.isListView === "true";
        },

        updateSearchConditionByQuery() {
            const updatedCategoryId = this.$route.query.categoryId;
            const updatedKeyword = this.$route.query.keyword;
            const updatedFromDate = this.$route.query.fromDate;
            const updatedToDate = this.$route.query.toDate;
            const updatedPage = this.$route.query.page;

            this.searchCondition.categoryId = updatedCategoryId === undefined ? "0" : updatedCategoryId;
            this.searchCondition.keyword = updatedKeyword === undefined ? "" : updatedKeyword;
            this.searchCondition.fromDate = updatedFromDate === undefined ? "" : updatedFromDate;
            this.searchCondition.toDate = updatedToDate === undefined ? "" : updatedToDate;
            this.searchCondition.page = updatedPage === undefined ? 1 : Number(updatedPage);
        },

        //TODO: 뷰 모드 바꾸는데 지금 api 호출할필요없는데 하고잇음. 수정해야됨.
        switchToListView() {
            this.isListView = true;
            this.updateQueryParameter(true, this.searchCondition);
        },

        switchToGridView() {
            this.isListView = false;
            this.updateQueryParameter(false, this.searchCondition);
        },

        movePage(page) {
            this.searchCondition.page = page;
            this.updateQueryParameter(this.isListView, this.searchCondition);
        },


        updateQueryParameter(isListView, searchCondition) {
            this.$router.push({
                path: 'boards',
                query: {
                    isListView: isListView,
                    ...searchCondition
                }
            }).catch(() => {
            });
        },
    },
};
</script>

<style scoped>

</style>

