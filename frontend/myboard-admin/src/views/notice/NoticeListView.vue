<template>
    <v-container>
        <PageTitle>
            <h2 slot="title" @click="initSearchCondition" v-bind:style="{ cursor: 'pointer' }">
                공지사항
            </h2>
        </PageTitle>

        <!--    TODO: 카테고리 선택 없애야함-->
        <SearchForm
            v-on:searchBtnClick="search"
            v-bind:updatedSearchCondition="searchCondition"
        ></SearchForm>

        <NoticeList
            v-on:ListViewBtnClick="switchToListView"
            v-on:GalleryViewBtnClick="switchToGalleryView"
            v-bind:updatedNoticeTotalCount="noticeTotalCount"
            v-bind:updatedListView="listView"
            v-bind:fetchedNoticeList="noticeList"
        ></NoticeList>

        <Pagination
            v-on:pageBtnClick="movePage"
            v-bind:updatedPage="searchCondition.page"
            v-bind:itemTotalCount="noticeTotalCount"
        ></Pagination>

        <v-row>
            <v-spacer></v-spacer>
            <v-col
                cols="auto"
            >
                <v-btn
                    @click="moveToNoticeWrite"
                    color="secondary"
                >
                    공지사항 작성
                </v-btn>
            </v-col>
        </v-row>

        <v-row>
            <v-col>

            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import PageTitle from "@/components/common/PageTitle";
import SearchForm from "@/components/common/SearchForm";
import BoardList from "@/components/board/BoardList";
import Pagination from "@/components/common/Pagination";
import NoticeDialog from "@/components/common/NoticeDialog";
import NoticeList from "@/components/notice/NoticeList";

export default {
    name: "NoticeListView",
    components: {
        NoticeDialog,
        PageTitle,
        SearchForm,
        NoticeList,
        Pagination
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

            listView: true,

            noticeTotalCount: 0,
            noticeList: [],
        };
    },
    async created() {
        this.updateSearchConditionByQuery();
        this.updateListViewByQuery();

        await this.fetchNoticeList(this.searchCondition);
    },
    computed: {},
    watch: {
        async "$route.query"() {
            this.updateSearchConditionByQuery();
            this.updateListViewByQuery();

            await this.fetchNoticeList(this.searchCondition);
        },
    },
    methods: {
        async fetchNotice() {
            try {
                const {data} = await this.$_NoticeService.fetchNoticeDetail();

                this.noticeDetail = data.noticeDetail;

            } catch (error) {
                console.log(error.response.data.errorMessages)
            }
        },

        async fetchNoticeList(searchCondition) {
            try {

                const {data} = await this.$_NoticeService.fetchNoticeList(searchCondition);

                this.noticeList = data.noticeList;
                this.noticeTotalCount = data.noticeTotalCount;

            } catch (error) {
                console.log(error.response.data.errorMessages)
            }

        },

        async search(searchCondition) {
            this.updateQueryParameter(this.listView, searchCondition);

            await this.fetchNoticeList(searchCondition);
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

        updateListViewByQuery() {
            this.listView = this.$route.query.listView === undefined ? true : this.$route.query.listView === "true";
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
            this.listView = true;
            this.updateQueryParameter(true, this.searchCondition);
        },

        switchToGalleryView() {
            this.listView = false;
            this.updateQueryParameter(false, this.searchCondition);
        },

        movePage(page) {
            this.searchCondition.page = page;
            this.updateQueryParameter(this.listView, this.searchCondition);
        },

        moveToNoticeWrite() {
            this.$router.push({
                name: "NoticeWriteView",
                query: this.$route.query,
            });
        },

        updateQueryParameter(listView, searchCondition) {
            this.$router.push({
                path: 'notices',
                query: {
                    listView: listView,
                    ...searchCondition
                }
            }).catch(() => {
            });
        },
    },
};
</script>
<style scoped>
.v-text-field >>> input {
    font-size: 0.875em;
}

.v-text-field >>> label {
    font-size: 0.875em;
}

.v-text-field >>> button {
    font-size: 0.875em;
}

.v-text-field--outlined >>> fieldset {
    border-color: rgba(209, 209, 209, 1);
}
</style>

