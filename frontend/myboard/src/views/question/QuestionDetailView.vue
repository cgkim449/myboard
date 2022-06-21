<template>
  <v-container>
    <PageTitle>
      <h2 slot="title" @click="moveToQuestionList" v-bind:style="{ cursor: 'pointer' }">
        Q&A
      </h2>
    </PageTitle>

    <v-row justify="center">
      <v-col
      cols = "auto"
      >

        <h3>질문</h3>
      </v-col>

      <v-spacer></v-spacer>

      <v-col
        cols="12"
      >
        <ItemDetail
            v-bind:fetchedItemDetail="questionDetail"
        ></ItemDetail>

        <AttachList
            v-if="questionDetail.hasAttach"
            v-bind:fetchedAttachList="questionDetail.attachList"
            v-bind:attachOf="attachOf"
        ></AttachList>

      </v-col>
    </v-row>


    <v-row justify="center">
      <v-col
          cols = "auto"
      >
        <h3>답변</h3>
      </v-col>

      <v-spacer></v-spacer>

      <template v-if="questionDetail.answer === null">
        <v-col
            cols="12"
        >
          <v-card outlined  class="pa-1">
            <v-card-text class="mt-1">
              <v-row algin="center">
                <v-col
                    cols="auto"
                >
                답변 처리중입니다.
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>
      </template>
<!--      답변이 있으면 보여줌-->
      <template v-if="questionDetail.answer !== null">

        <v-col
            cols="12"
        >
          <ItemDetail
              v-bind:fetchedItemDetail="questionDetail.answer"
              v-bind:itemType="itemType"
          ></ItemDetail>
        </v-col>

      </template>



      <v-card elevation="0">
        <v-card-text>
          <v-row justify="center">
            <v-col
                cols="auto"

            >
              <router-link v-bind:to="{
                  path: `/questions`,
                  query: $route.query
                }">
                <v-btn
                    color="primary"
                >
                  목록
                </v-btn>
              </router-link>
            </v-col>

            <!-- 본인 글이고 답글 아직 안달려있으면 수정 삭제 버튼 보임. -->
            <template v-if="questionDetail.answer === null && $store.getters.loggedIn && ($store.state.username === questionDetail.memberUsername)">
              <v-col
                  cols="auto"
              >
                <v-btn
                    @click="moveToQuestionModify"
                    outlined
                    color="primary"
                >
                  수정
                </v-btn>
              </v-col>

              <v-col
                  cols="auto"
              >
                <v-btn
                    outlined
                    color="primary"
                    @click="removeQuestion()"
                >
                  삭제
                </v-btn>
              </v-col>

            </template>
          </v-row>
        </v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import ItemDetail from "@/components/common/ItemDetail";
import AttachList from "@/components/common/AttachList";
import PageTitle from "@/components/common/PageTitle";

export default {
  name: "QuestionDetailView",
  components: {
    AttachList,
    ItemDetail,
    PageTitle
  },
  data() {
    return {
      questionDetail: {},
      attachOf: "question",
      itemType: "answer",
    }
  },
  computed: {},
  async created() {
    let questionId = this.$route.params.questionId;

    const {data} = await this.$_QuestionService.fetchQuestion(questionId);

    this.questionDetail = data.questionDetail;
  },
  methods: {
    async removeQuestion() {
      try {
        await this.$_QuestionService.removeQuestion(this.questionDetail.questionId);

        alert("삭제되었습니다.")

        this.moveToQuestionList();
      } catch (error) {
        console.log(error)
      }

    },
    moveToQuestionList() {
      this.$router.push({
        path: '/questions'
        , query: this.$route.query
      });
    },
    moveToQuestionModify() {
      this.$router.push({
            name: "QuestionModifyView",
            params: {
              questionId: this.questionDetail.questionId
            },
            query: this.$route.query,
          }
      );
    }
  }
}
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