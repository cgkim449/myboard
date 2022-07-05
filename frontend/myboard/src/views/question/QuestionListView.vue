<template>
    <v-container>
        <CommonPageTitle>
            <h2 slot="title" @click="initSearchCondition" v-bind:style="{ cursor: 'pointer'}">
                Q&A
            </h2>
        </CommonPageTitle>

        <CommonSearchForm
            v-on:searchBtnClick="search"
            v-bind:updatedSearchCondition="searchCondition"
        ></CommonSearchForm>

        <TheQuestionList
            v-on:ListViewBtnClick="switchToListView"
            v-on:GridViewBtnClick="switchToGridView"
            v-bind:updatedQuestionTotalCount="questionTotalCount"
            v-bind:updatedIsListView="isListView"
            v-bind:fetchedQuestionList="questionList"
        ></TheQuestionList>

        <CommonPagination
            v-on:pageBtnClick="movePage"
            v-bind:updatedPage="searchCondition.page"
            v-bind:itemTotalCount="questionTotalCount"
        ></CommonPagination>

        <TheQuestionListBottomNavigation/>

    </v-container>
</template>

<script>
import CommonPageTitle from "@/components/common/CommonPageTitle";
import CommonSearchForm from "@/components/common/CommonSearchForm";
import TheQuestionList from "@/components/question/TheQuestionList";
import CommonPagination from "@/components/common/CommonPagination";
import TheQuestionListBottomNavigation from "@/components/question/TheQuestionListBottomNavigation";

export default {
    name: "QuestionListView",
    components: {
        TheQuestionListBottomNavigation,
        CommonPagination,
        TheQuestionList,
        CommonPageTitle,
        CommonSearchForm
    },
    data() {
        return {
            searchCondition: {
                categoryId: "0",
                keyword: "",
                fromDate: "",
                toDate: "",
                page: 1,
            },

            isListView: true,

            questionTotalCount: 0,
            questionList: [],
        };
    },
    async created() {

        this.updateSearchConditionByQuery();
        this.updateIsListViewByQuery();

        await this.fetchQuestionList(this.searchCondition);
    },
    computed: {},
    watch: {
        async "$route.query"() {

            this.updateSearchConditionByQuery();
            this.updateIsListViewByQuery();

            await this.fetchQuestionList(this.searchCondition);
        },
    },
    methods: {
        async fetchQuestionList(searchCondition) {

            try {

                const {data} = await this.$_questionService.fetchQuestionList(searchCondition);

                this.questionList = data.questionList;
                this.questionTotalCount = data.questionTotalCount;
            } catch (error) {

                console.log(error.response.data.errorMessages)
            }
        },

        async search(searchCondition) {

            this.updateQueryParameter(this.isListView, searchCondition);

            await this.fetchQuestionList(searchCondition);
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

        switchToListView() {
            this.isListView = true;
            this.updateQueryParameter(this.isListView, this.searchCondition);
        },

        switchToGridView() {
            this.isListView = false;
            this.updateQueryParameter(this.isListView, this.searchCondition);
        },

        movePage(page) {
            this.searchCondition.page = page;
            this.updateQueryParameter(this.isListView, this.searchCondition);
        },

        updateQueryParameter(isListView, searchCondition) {
            this.$router.push({
                path: "questions",
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

