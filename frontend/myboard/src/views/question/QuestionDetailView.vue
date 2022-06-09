<template>
  <v-container v-if="!isEmpty(itemDetail)">
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
        <v-card outlined min-height="400" class="pa-1">
          <v-card-text class="mt-1">
            <v-row algin="center">
              <v-col
                  cols="auto"
              >
                <span>{{ itemDetail.nickname }}</span>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>등록일시 {{itemDetail.registerDate | formatDate}}</span>
                <v-divider
                    class="mx-4"
                    vertical
                ></v-divider>
                <span>수정일시 {{itemDetail.updateDate | formatDate}}</span>
              </v-col>
            </v-row>

            <v-row algin="center">
              <v-col
                  cols="auto"
              >
                <strong>[{{itemDetail.categoryName}}]</strong>
                <v-divider
                    class="mx-4"
                    vertical
                ></v-divider>
                <strong>{{itemDetail.title}}</strong>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>조회수: {{itemDetail.viewCount}}</span>
              </v-col>
            </v-row>
          </v-card-text>

          <v-divider  class="mx-4"></v-divider>

          <v-card-text class="fill-height">
            <v-row>
              <v-col>
                <p>{{itemDetail.content}}</p>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <template v-if="itemDetail.hasAttach">
          <v-card outlined class="px-1 pt-1 mt-3">
            <v-card-title class="text-subtitle-1 grey--text">
              첨부파일 {{ itemDetail.attachList.length }}개
            </v-card-title>
            <v-card-text>
              <p v-for="attach in itemDetail.attachList">
                <span v-on:click="$_QuestionService.downloadAttach(attach.attachId)" v-bind:style="{cursor: 'pointer'}">
                  <v-icon>mdi-attachment</v-icon>
                  {{attach.name}}.{{attach.extension}}
                </span>
              </p>
            </v-card-text>
          </v-card>
        </template>
      </v-col>
    </v-row>






    <v-row justify="center">
      <v-col
          cols = "auto"
      >
        <h3>답변</h3>
      </v-col>

      <v-spacer></v-spacer>





      <template v-if="itemDetail.answer === null && viewAnswerForm === false">
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

      <template v-if="itemDetail.answer === null && viewAnswerForm === true">
        <v-col
            cols="12"
        >




          <v-card flat>
            <v-form
                ref="form"
                @submit.prevent="submit"
            >
              <v-container fluid>
                <v-row>
                  <v-col
                      cols="12"
                  >
                    <v-text-field
                        label="제목"
                        v-model="form.title"
                        :rules="rules.title"
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-textarea
                        v-model="form.content"
                        :rules="rules.content"
                        color="teal"
                        label="내용"
                    >
                    </v-textarea>
                  </v-col>
                </v-row>


                <v-row>
                  <v-col
                      cols="auto"
                  >

                      <v-btn
                          outlined
                          text
                          @click="viewAnswerForm = !viewAnswerForm"
                      >
                        취소
                      </v-btn>


                  </v-col>
                  <v-spacer></v-spacer>
                  <v-col
                      cols="auto"
                  >
                    <v-btn
                        outlined
                        text
                        @click="resetForm"
                    >
                      초기화
                    </v-btn>
                  </v-col>


                  <v-col
                      cols="auto"
                  >
                    <v-btn
                        outlined
                        text
                        color="primary"
                        @click="writeAnswer"
                    >
                      저장
                    </v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card>













        </v-col>
      </template>


      <template v-if="itemDetail.answer !== null">
        <v-col
            cols="12"
        >
          <v-card outlined min-height="400" class="pa-1">
            <v-card-text class="mt-1">
              <v-row algin="center">
                <v-col
                    cols="auto"
                >
                  <span>{{ itemDetail.answer.nickname }}</span>
                </v-col>

                <v-spacer></v-spacer>

                <v-col
                    cols="auto"
                >
                  <span>등록일시 {{itemDetail.answer.registerDate | formatDate }}</span>
                  <v-divider
                      class="mx-4"
                      vertical
                  ></v-divider>
                  <span>수정일시 {{itemDetail.answer.updateDate | formatDate }}</span>
                </v-col>
              </v-row>

              <v-row algin="center">
                <v-col
                    cols="auto"
                >
                  <strong>[처리완료] {{itemDetail.answer.title}}</strong>
                </v-col>

                <v-spacer></v-spacer>
              </v-row>
            </v-card-text>

            <v-divider  class="mx-4"></v-divider>

            <v-card-text class="fill-height">
              <v-row>
                <v-col>
                  <p>{{itemDetail.answer.content}}</p>
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>


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
                  query: this.searchCondition
                }">
                <v-btn
                    color="primary"
                >
                  목록
                </v-btn>
              </router-link>
            </v-col>

            <!--              1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임. TODO: 답변 달리면 수정 삭제 버튼 안보이게-->
            <template v-if="$cookies.get('role') === 'admin' && itemDetail.answer === null">
<!--              <v-col-->
<!--                  cols="auto"-->
<!--              >-->
<!--                <router-link v-bind:to="{-->
<!--                      path: `/questions/${itemDetail.questionId}/modify`,-->
<!--                      query: this.searchCondition-->
<!--                    }">-->
<!--                  <v-btn-->
<!--                      outlined-->
<!--                      color="primary"-->
<!--                  >-->
<!--                    수정-->
<!--                  </v-btn>-->
<!--                </router-link>-->
<!--              </v-col>-->

<!--              <v-col-->
<!--                  cols="auto"-->
<!--              >-->
<!--                <v-btn-->
<!--                    outlined-->
<!--                    color="primary"-->
<!--                    @click="pwCheck('delete')"-->
<!--                >-->
<!--                  삭제-->
<!--                </v-btn>-->
<!--              </v-col>-->

              <v-col
                  cols="auto"
              >
                <v-btn
                    color="primary"
                    @click="showAnswerForm()"
                    v-if="!viewAnswerForm"
                >
                  답변 작성
                </v-btn>
              </v-col>
            </template>
            <!--              2.(모든 사용자)다른 회원 글이면 수정 삭제 버튼안보임.-->
            <template v-else-if="itemDetail.username !== null">

            </template>

          </v-row>
        </v-card-text>
      </v-card>
    </v-row>





  </v-container>
</template>

<script>
export default {
  name: "QuestionDetailView",
  data() {
    const defaultForm = Object.freeze({
      categoryId: '',
      title: '',
      content: '',
      multipartFiles: [],
    })
    return {
      viewAnswerForm: false,
      itemDetail: {},
      searchCondition: {},

      form: Object.assign({}, defaultForm),
      rules: {
        title: [val => (4 <= (val || '').length && (val || '').length < 20) || '제목은 4글자 이상, 20글자 미만입니다.'],
        content: [val => (4 <= (val || '').length && (val || '').length < 2000) || '내용은 4글자 이상, 2000글자 미만입니다.'],
      },
      defaultForm,
    }
  },
  computed: {},
  async created() {
    this.searchCondition = {...this.$route.query};
    let id = this.$route.params.id;
    const response = await this.$_QuestionService.fetchItem(id);
    this.itemDetail = response.data.itemDetail;
    console.log(this.itemDetail);
  },
  methods: {
    async writeAnswer() {
      if(this.validateForm()) {
        console.log("writeanswerda")
        let formData = this.prepareFormData();
        let response;
        response = await this.$_AnswerService.writeAnswer(formData);
        console.log("writeanswerhatda")
        this.$router.go();
      }
    },
    prepareFormData() {
      let formData = new FormData();

      formData.append("questionId", this.itemDetail.questionId);
      formData.append("title", this.form.title);
      formData.append("content", this.form.content);

      return formData;
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit () {
      this.resetForm()
    },
    showAnswerForm() {
      this.viewAnswerForm = true;
    },
//TODO: 댓글 작성 유효성 검증 만들어야됨. 중복 코드임.
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    goToQuestionList() {
      this.$router.push({
        path: '/questions'
        , query: this.searchCondition
      });
    },
    goToQuestionModify() {
      this.$router.push({
        path: `/questions/${this.itemDetail.questionId}/modify`
        , query: this.searchCondition});
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