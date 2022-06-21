<template>
  <v-container v-if="!isEmpty(boardDetail)">
    <v-row justify="center">
      <v-col
        cols="11"
      >
        <v-card outlined min-height="400" class="pa-1">
          <v-card-text class="mt-1">
            <v-row algin="center">
              <v-col
                  cols="auto"
              >
                <template v-if="boardDetail.guestNickname === null">
                  <span>{{ boardDetail.nickname }}</span>
                </template>
                <template v-else>
                  <span>{{ boardDetail.guestNickname }}</span>
                </template>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>등록일시 {{boardDetail.registerDate | formatDate}}</span>
                <v-divider
                    class="mx-4"
                    vertical
                ></v-divider>
                <span>수정일시 {{boardDetail.updateDate | formatDate}}</span>
              </v-col>
            </v-row>

            <v-row algin="center">
              <v-col
                  cols="auto"
              >
                <strong>[{{boardDetail.categoryName}}]</strong>
                <v-divider
                    class="mx-4"
                    vertical
                ></v-divider>
                <strong>{{boardDetail.title}}</strong>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>조회수: {{boardDetail.viewCount}}</span>
              </v-col>
            </v-row>
          </v-card-text>

          <v-divider  class="mx-4"></v-divider>

          <v-card-text class="fill-height">
            <v-row>
              <v-col>
                <p>{{boardDetail.content}}</p>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <template v-if="boardDetail.hasAttach">
          <v-card outlined class="px-1 pt-1 mt-3">
            <v-card-title class="text-subtitle-1 grey--text">
              첨부파일 {{ boardDetail.attachList.length }}개
            </v-card-title>
            <v-card-text>
              <p v-for="attach in boardDetail.attachList">
                <span v-on:click="$_BoardService.downloadAttach(attach.attachId)" v-bind:style="{cursor: 'pointer'}">
                  <v-icon>mdi-attachment</v-icon>
                  {{attach.name}}.{{attach.extension}}
                </span>
              </p>
            </v-card-text>
          </v-card>
        </template>

        <v-card outlined class="px-1 pt-1 mt-3">
          <v-card-title class="text-subtitle-1 grey--text">
            댓글 {{ boardDetail.commentList.length }}개
          </v-card-title>
          <v-card-text v-for="comment in boardDetail.commentList">
            <v-row dense>
                <v-col
                    cols="2"
                >
                  <template v-if="comment.guestNickname != null">
                    {{comment.guestNickname}}
                  </template>
                  <template v-else-if="comment.Nickname != null">
                    {{comment.nickname}}
                  </template>
                  <template v-else-if="comment.adminNickname != null">
                    {{comment.adminNickname}}
                  </template>
                </v-col>
              <v-col>
                {{comment.content}}
              </v-col>

              <v-col
                cols="auto"
              >
                <v-btn
                    icon
                    x-small
                    color="red lighten-2"
                    class="mb-1"
                    @click="removeComment(comment.commentId)"
                >
                  x
                </v-btn>
              </v-col>

              <v-col
                  cols="auto"
              >
                {{comment.registerDate}}
              </v-col>
            </v-row>
            <v-divider></v-divider>
          </v-card-text>


          <v-form
              ref="form"
              @submit.prevent="submit"
          >
            <v-card-text>
              <v-row dense>
                <v-col
                    cols="2"
                >
                  <v-text-field  disabled dense style="height: 48px !important; " outlined  v-model="$store.state.nickname">
                  </v-text-field>
                </v-col>
                <v-col>
                  <v-textarea
                      dense
                      v-model="comment.content"
                      outlined
                      rows="3"
                      label="
                          댓글을 입력해주세요.
                          "
                      v-on:keydown.enter.exact.prevent="writeComment"
                      v-on:keydown.enter.shift.exact.prevent="comment.content += '\n'"
                  ></v-textarea>
                </v-col>
                <v-col
                    cols="auto"
                >
                  <v-btn
                      outlined
                      height="90"
                      width="94"
                      color="secondary"
                      v-on:click="writeComment"
                  >
                    등록
                  </v-btn>
                </v-col>
              </v-row>
            </v-card-text>
          </v-form>
        </v-card>

        <v-card elevation="0">
          <v-card-text>
            <v-row justify="center">
              <v-col
                cols="auto"

              >
                <router-link v-bind:to="{
                  path: `/boards`,
                  query: this.searchCondition
                }">
                  <v-btn
                      color="secondary"
                  >
                    목록
                  </v-btn>
                </router-link>
              </v-col>

              <v-col
                cols="auto"
              >
                <router-link v-bind:to="{
                  path: `/boards/${boardDetail.boardId}/modify`,
                  query: this.searchCondition
                }">
                  <v-btn
                      outlined
                      color="secondary"
                  >
                    수정
                  </v-btn>
                </router-link>
              </v-col>

              <v-col
                cols="auto"
              >
                  <v-btn
                      outlined
                      color="secondary"
                      @click="removeBoard"
                  >
                    삭제
                  </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "BoardDetailView",
  data() {
    const defaultForm = Object.freeze({
      content: '',
    })

    return {
      defaultForm,
      boardDetail: {},
      comment: {
        boardId: 0,
        content: "",
      },
      searchCondition: {},
    }
  },

  computed: {},

  async created() {
    this.searchCondition = {...this.$route.query};
    let boardId = this.$route.params.id;
    const response = await this.$_BoardService.fetchBoard(boardId);
    this.boardDetail = response.data.boardDetail;
    console.log(this.boardDetail);
  },

  methods: {
    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit () {
      this.resetForm()
    },
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    async writeComment() {
      if(this.validateForm()) {
        try {
          this.comment.boardId = this.boardDetail.boardId;
          //TODO: 메서드명 바꾸기
          await this.$_BoardService.writeMemberComment(this.comment);
          const response = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);
          this.boardDetail.commentList = response.data.commentList;
          this.initComment();
        } catch (error) {
          //TODO: 프론트에서 유효성 검증
          alert(error.response.data.fieldErrorDetails[0].fieldErrorMessage)
        }
      }
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    initComment() {
      this.comment.content = "";
    },
    async removeComment(commentId) {
      try {
          await this.$_BoardService.removeComment(commentId);
          alert('삭제되었습니다.');
          const response = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);
          this.boardDetail.commentList = response.data.commentList;
      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },
    async removeBoard() {
      try {
          await this.$_BoardService.removeBoard(this.boardDetail.boardId);
          alert('삭제되었습니다.');
          this.goToBoardList();
      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },
    goToBoardList() {
      this.$router.push({
        path: '/boards'
        , query: this.searchCondition
      });
    },
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