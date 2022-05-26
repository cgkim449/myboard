<template>
  <v-container v-if="!isEmpty(boardDetail)">
    <v-row>
      <v-col>
        <v-card outlined min-height="400">
          <v-card-text>
            <v-row algin="center">
              <v-col
                  cols="auto"
              >
                <span>{{boardDetail.boardWriter}}</span>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>등록일시 {{boardDetail.boardRegisterDate | formatDate}}</span>
                <v-divider
                    class="mx-4"
                    vertical
                ></v-divider>
                <span>수정일시 {{boardDetail.boardUpdateDate | formatDate}}</span>
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
                <strong>{{boardDetail.boardTitle}}</strong>
              </v-col>

              <v-spacer></v-spacer>

              <v-col
                  cols="auto"
              >
                <span>조회수: {{boardDetail.boardViewCount}}</span>
              </v-col>
            </v-row>
          </v-card-text>

          <v-divider  class="mx-4"></v-divider>

          <v-card-text class="fill-height">
            <v-row>
              <v-col>
                <p>{{boardDetail.boardContent}}</p>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <v-card outlined class="pt-4 mt-3">
          <v-card-text v-for="comment in boardDetail.commentList">
            <v-row>
              <v-col
                  cols="1"
              >
                {{comment.commentNickname}}
              </v-col>
              <v-col>
                {{comment.commentContent}}
              </v-col>
              <v-col
                  cols="auto"
              >
                {{comment.commentRegisterDate}}
              </v-col>
            </v-row>
            <v-divider></v-divider>
          </v-card-text>

          <v-card-text>
            <v-row dense>
              <v-col
                  cols="2"
              >
                <v-text-field label="닉네임" dense  outlined style="height: 48px !important;" v-model="comment.commentNickname">
                </v-text-field>
                <v-text-field label="비밀번호" dense  outlined style="height: 48px !important;" v-model="comment.commentPassword">
                </v-text-field>
              </v-col>
              <v-col>
                <v-textarea
                    v-model="comment.commentContent"
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
                    height="94"
                    width="94"
                    color="primary"
                    v-on:click="writeComment"
                >
                  등록
                </v-btn>
              </v-col>

            </v-row>
          </v-card-text>
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
                      color="primary"
                  >
                    목록
                  </v-btn>
                </router-link>
              </v-col>
              <v-col
                  cols="auto"
              >
                <router-link v-bind:to="{
                  path: `/boards/${boardDetail.boardId}/pwCheck`,
                  query: {action: 'modify', ...searchCondition}}">
                  <v-btn
                      color="primary"
                      outlined
                  >
                    수정
                  </v-btn>
                </router-link>
              </v-col>
              <v-col
                  cols="auto"
              >
                <router-link v-bind:to="{
                  path: `/boards/${boardDetail.boardId}/pwCheck`,
                  query: {action: 'delete', ...searchCondition}}">
                  <v-btn
                      color="primary"
                      outlined
                  >
                    삭제
                  </v-btn>
                </router-link>
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
    return {
      boardDetail: {},
      comment: {},
      searchCondition: {},
    }
  },
  async created() {
    this.searchCondition = {...this.$route.query};
    let boardId = this.$route.params.id;
    const response = await this.$_BoardService.fetchBoard(boardId);
    this.boardDetail = response.data.boardDetail;
    console.log(this.boardDetail);
  },
  methods: {
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    async writeComment() {
      try {
        this.comment.boardId = this.boardDetail.boardId;

        await this.$_BoardService.writeComment(this.comment);
        const response = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);
        this.boardDetail.commentList = response.data.commentList;

        this.initComment();
      } catch (error) {
        console.log(error)
      }
    },
    initComment() {
      this.comment.commentNickname = "";
      this.comment.commentPassword = "";
      this.comment.commentContent = "";
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