<template>
  <div>
    <v-row dense>
      <v-col
          cols="auto"
      >
        총 {{updatedQuestionTotalCount}} 건
      </v-col>

      <v-spacer></v-spacer>

      <v-col
          cols="auto"
      >
        <v-btn icon v-bind:color="updatedListView ? 'secondary' : 'primary'" @click="clickGalleryView">
          <v-icon >mdi-view-grid-outline</v-icon>
        </v-btn>
      </v-col>

      <v-col
          cols="auto"
      >
        <v-btn icon v-bind:color="updatedListView ? 'primary' : 'secondary'" @click="clickListView">
          <v-icon>mdi-format-list-bulleted</v-icon>
        </v-btn>
      </v-col>
    </v-row>

    <template v-if="updatedListView">
      <v-row>
        <v-col
            cols="12"
        >
          <v-data-table
              dense
              :headers="tableHeaders"
              :items="fetchedQuestionList"
              hide-default-footer
              class="elevation-2"
          >
            <template v-slot:item.hasAttach="{item}">
              <v-icon
                  v-if="item.hasAttach === 1"
              >
                mdi-attachment
              </v-icon>

              <v-icon
                  v-else
              >
              </v-icon>
            </template>

            <template v-slot:item.title="{item}">

<!--              1. 공개글이면 다보임-->
              <template v-if="isPublicQuestion(item)">
                <span
                    @click="moveToQuestionDetail(item.questionId)"
                    v-bind:style="{cursor: 'pointer'}"
                    class="d-flex start"
                >
                  {{item.title | formatBoardTitle}}
                </span>
              </template>

<!--              TODO: computed로 -->
<!--              2. 비밀글이고, 내가 쓴 글이면 보임-->
              <template v-else-if="isMySecretQuestion(item)">
                <span
                    @click="moveToQuestionDetail(item.questionId)"
                    v-bind:style="{cursor: 'pointer'}"
                    class="d-flex start"
                >
<!--                  TODO: 이름바꾸기-->
                  {{item.title | formatBoardTitle}} <v-icon small>mdi-lock</v-icon>
                </span>
              </template>

<!--              3. 비밀글이고, 내가 쓴 글이 아니면 안보임-->
              <template v-else>
                <span
                    v-if="isOthersSecretQuestion(item)"
                    class="d-flex start"
                >
                  비공개 글 입니다. <v-icon small>mdi-lock</v-icon>
                </span>
              </template>

            </template>

            <template v-slot:item.memberNickname="{item}">
<!--              TODO: 이름바꾸기-->
              <span v-if="item.memberNickname !== null">
                {{ item.memberNickname | formatQuestionNickname }}
              </span>

              <span v-if="item.adminNickname !== null">
                {{ item.adminNickname | formatQuestionNickname }}
              </span>

            </template>

            <template v-slot:item.registerDate="{item}">
              {{item.registerDate | formatDate}}
            </template>

            <template v-slot:item.updateDate="{item}">
              {{formatUpdateDate(item)}}
            </template>

            <template v-slot:item.answerId="{item}">
              <span v-if="item.answerId === null">
                대기
              </span>

              <span v-if="item.answerId !== null" class="blue--text">
                처리완료
              </span>
            </template>
          </v-data-table>
        </v-col>
      </v-row>
    </template>

    <template v-else>
      <v-row>
        <v-col>
          <v-card outlined>
            <v-container fluid>
              <v-row dense>
                <v-col
                    v-for="question in fetchedQuestionList"
                    :key="question.questionId"
                    :cols="3"
                >
                  <v-card outlined>
                    <v-img
                        v-if="isMySecretQuestion(question)"
                        @click="moveToQuestionDetail(question.questionId)"
                        v-bind:style="{cursor: 'pointer'}"
                        :src="question.thumbnailUri"
                        class="white--text align-end"
                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                        height="200px"
                    >
                    </v-img>

                    <v-img
                        v-if="isOthersSecretQuestion(question)"
                        class="white--text align-end"
                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                        height="200px"
                    >
                    </v-img>

                    <v-img
                        v-if="isPublicQuestion(question)"
                        @click="moveToQuestionDetail(question.questionId)"
                        v-bind:style="{cursor: 'pointer'}"
                        :src="question.thumbnailUri"
                        class="white--text align-end"
                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                        height="200px"
                    >
                    </v-img>

                    <v-card-actions>
                      <v-col cols="auto">
                        <small class="font-weight-bold" v-if="isMySecretQuestion(question)">
                          [{{question.categoryName}}] {{question.title | formatBoardTitle}} <v-icon small>mdi-lock</v-icon>
                        </small>

                        <small class="font-weight-bold" v-if="isOthersSecretQuestion(question)">
                          [{{question.categoryName}}] 비공개 글 입니다. <v-icon small>mdi-lock</v-icon>
                        </small>

                        <small class="font-weight-bold" v-if="isPublicQuestion(question)">[{{question.categoryName}}] {{ question.title }}</small>

                        <br>

                        <small class="font-weight-bold" v-if="question.adminNickname !== null">{{question.adminNickname | formatQuestionNickname }}</small>
                        <small class="font-weight-bold" v-if="question.memberNickname !== null">{{question.memberNickname | formatQuestionNickname }}</small><br>

                        <small>조회수: {{question.viewCount}}</small><br>
                        <small>{{question.registerDate | formatDate}}</small><br>
                        <small class="font-weight-bold blue--text" v-if="question.answerId !== null">처리완료</small>
                        <small class="font-weight-bold" v-if="question.answerId === null">대기</small>
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


  </div>
</template>

<script>
import {formatDate} from "@/utils/filters";

export default {
  name: "QuestionList",
  props: ["updatedQuestionTotalCount", "updatedListView", "fetchedQuestionList"],
  data() {
    return {
      tableHeaders: [
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
          value: 'memberNickname',
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
        {
          text: '상태',
          align: 'center',
          sortable: false,
          value: 'answerId',
          width: '10%',
        },
      ],
    }
  },
  computed: {

  },
  methods: {
    isOthersSecretQuestion(question) {
      return question.isSecret === 1 && question.memberNickname !== this.$store.state.nickname;
    },
    isMySecretQuestion(question) {
      return question.isSecret === 1 && question.memberNickname === this.$store.state.nickname && this.$store.getters.loggedIn;
    },
    isPublicQuestion(question) {
      return question.isSecret === 0;
    },
    clickGalleryView() {
      this.$emit("GalleryViewBtnClick");
    },
    clickListView() {
      this.$emit("ListViewBtnClick");
    },
    moveToQuestionDetail(questionId) {
      this.$router.push({
        path: `/questions/${questionId}`,
        query: this.$route.query
      }).catch(()=>{});
    },
    formatUpdateDate(question) {
      return question.registerDate === question.updateDate ? '-' : formatDate(question.updateDate);
    }
  }
}
</script>

<style scoped>

</style>