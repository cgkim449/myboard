<template>
    <v-container>
        <PageTitle>
            <h2 slot="title" @click="initSearchCondition" v-bind:style="{ cursor: 'pointer'}">
                Q&A
            </h2>
        </PageTitle>

        <SearchForm
            v-on:searchBtnClick="search"
            v-bind:updatedSearchCondition="searchCondition"
        ></SearchForm>

        <QuestionList
            v-on:ListViewBtnClick="switchToListView"
            v-on:GalleryViewBtnClick="switchToGalleryView"
            v-bind:updatedQuestionTotalCount="questionTotalCount"
            v-bind:updatedListView="listView"
            v-bind:fetchedQuestionList="questionList"
        ></QuestionList>

        <Pagination
            v-on:pageBtnClick="movePage"
            v-bind:updatedPage="searchCondition.page"
            v-bind:itemTotalCount="questionTotalCount"
        ></Pagination>

        <v-row>
            <v-spacer></v-spacer>

            <v-col
                cols="auto"
            >
                <router-link
                    style="text-decoration: none;"
                    :to="{
              name: 'QuestionWriteView',
              query: this.$route.query
            }"
                >
                    <v-btn
                        color="secondary"
                    >
                        질문하기

                    </v-btn>
                </router-link>
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
import QuestionList from "@/components/question/QuestionList";
import Pagination from "@/components/common/Pagination";

export default {
    name: "QuestionListView",
    components: {
        Pagination,
        QuestionList,
        PageTitle,
        SearchForm
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

            listView: true,

            questionTotalCount: 0,
            questionList: [],
        };
    },
    async created() {

        this.updateSearchConditionByQuery();
        this.updateListViewByQuery();

        await this.fetchQuestionList(this.searchCondition);
    },
    computed: {},
    watch: {
        async "$route.query"() {

            this.updateSearchConditionByQuery();
            this.updateListViewByQuery();

            await this.fetchQuestionList(this.searchCondition);
        },
    },
    methods: {
        async fetchQuestionList(searchCondition) {

            try {

                const {data} = await this.$_QuestionService.fetchQuestionList(searchCondition);

                this.questionList = data.questionList;
                this.questionTotalCount = data.questionTotalCount;
            } catch (error) {

                console.log(error.response.data.errorMessages)
            }
        },

        async search(searchCondition) {

            this.updateQueryParameter(this.listView, searchCondition);

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

        switchToListView() {
            this.listView = true;
            this.updateQueryParameter(this.listView, this.searchCondition);
        },

        switchToGalleryView() {
            this.listView = false;
            this.updateQueryParameter(this.listView, this.searchCondition);
        },

        movePage(page) {
            this.searchCondition.page = page;
            this.updateQueryParameter(this.listView, this.searchCondition);
        },

        updateQueryParameter(listView, searchCondition) {
            this.$router.push({
                path: "questions",
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

