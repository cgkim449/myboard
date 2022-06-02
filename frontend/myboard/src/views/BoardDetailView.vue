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

        <template v-if="boardDetail.boardHasAttach">
          <v-card outlined class="px-1 pt-1 mt-3">
            <v-card-title class="text-subtitle-1 grey--text">
              첨부파일 {{ boardDetail.attachList.length }}개
            </v-card-title>
            <v-card-text>
              <p v-for="attach in boardDetail.attachList">
                <span v-on:click="$_BoardService.downloadAttach(attach.attachId)" v-bind:style="{cursor: 'pointer'}">
                  <v-icon>mdi-attachment</v-icon>
                  {{attach.attachName}}.{{attach.attachExtension}}
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
            <v-row>
              <template v-if="comment.nickname == null">
                <v-col
                    cols="1"
                >
                  {{comment.guestNickname}}
                </v-col>
              </template>
              <template v-else>
                <v-col
                    cols="1"
                >
                  {{comment.nickname}}
                </v-col>
              </template>
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
                <template v-if="$store.getters.isLogin">
                  <v-text-field  disabled dense  outlined style="height: 48px !important;" v-model="$store.state.nickname">
                  </v-text-field>
                </template>
                <template v-else>
                  <v-text-field label="닉네임" dense  outlined style="height: 48px !important;" v-model="comment.guestNickname">
                  </v-text-field>
                  <v-text-field  label="비밀번호" dense  outlined style="height: 48px !important;" v-model="comment.guestPassword">
                  </v-text-field>
                </template>
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

<!--              1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임.-->
                <template v-if="$store.getters.isLogin && $store.state.username === boardDetail.username">
                  <v-col
                    cols="auto"
                  >
                    <router-link v-bind:to="{
                      path: `/boards/${boardDetail.boardId}/modify`,
                      query: this.searchCondition
                    }">
                      <v-btn
                          outlined
                          color="primary"
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
                          color="primary"
                          @click="pwCheck('delete')"
                      >
                        삭제
                      </v-btn>
                  </v-col>
                </template>
<!--              2.(모든 사용자)다른 회원 글이면 수정 삭제 버튼안보임.-->
              <template v-else-if="boardDetail.username !== null">

              </template>
<!--                3. 익명 글이면 누구나 수정 삭제 버튼 눌렀을시 모달 뜸-->
                <template v-else>
                  <v-col
                      cols="auto"
                  >
                    <v-dialog
                        v-model="modifyBoardDialog"
                        max-width="400px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                            outlined
                            color="primary"
                            v-bind="attrs"
                            v-on="on"
                        >
                          수정
                        </v-btn>
                      </template>
                      <v-card>
                        <v-card-title>
                          <span class="text-h5">비밀번호 확인</span>
                        </v-card-title>
                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-col
                                  cols="12"
                              >
                                <v-text-field
                                    type="password"
                                    clearable
                                    prepend-icon="mdi-lock-outline"
                                    v-model="guestBoardPwCheckRequest.guestPassword"
                                    label="비밀번호를 입력해주세요."
                                    v-on:keyup.enter="pwCheck('modify')"
                                ></v-text-field>
                              </v-col>

                            </v-row>
                          </v-container>
                        </v-card-text>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn
                              color="blue darken-1"
                              text
                              @click="modifyBoardDialog = false"
                          >
                            취소
                          </v-btn>
                          <v-btn
                              color="blue darken-1"
                              text
                              @click="pwCheck('modify')"
                          >
                            확인
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-col>


                  <v-col
                      cols="auto"
                  >
                    <v-dialog
                        v-model="deleteBoardDialog"
                        max-width="400px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                            outlined
                            color="primary"
                            v-bind="attrs"
                            v-on="on"
                        >
                          삭제
                        </v-btn>
                      </template>
                      <v-card>
                        <v-card-title>
                          <span class="text-h5">비밀번호 확인</span>
                        </v-card-title>
                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-col
                                  cols="12"
                              >
                                <v-text-field
                                    clearable
                                    type="password"
                                    prepend-icon="mdi-lock-outline"
                                    v-model="guestBoardPwCheckRequest.guestPassword"
                                    label="비밀번호를 입력해주세요."
                                    v-on:keyup.enter="pwCheck('delete')"
                                ></v-text-field>
                              </v-col>

                            </v-row>
                          </v-container>
                        </v-card-text>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn
                              color="blue darken-1"
                              text
                              @click="deleteBoardDialog = false"
                          >
                            취소
                          </v-btn>
                          <v-btn
                              color="blue darken-1"
                              text
                              @click="pwCheck('delete')"
                          >
                            확인
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-col>
                </template>
              <!--                비회원 모달 끝-->


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
      modifyBoardDialog: false,
      deleteBoardDialog: false,
      boardDetail: {},
      comment: {
        boardId: 0,
        commentContent: "",
        guestPassword: "",
        guestNickname: "",
      },
      searchCondition: {},
      guestBoardPwCheckRequest: {
        guestPassword: "",
      },
    }
  },
  computed: {
    closeModifyBoardDialog() {
      return this.initGuestBoardPwCheckRequest(this.modifyBoardDialog);
    },
    closeDeleteBoardDialog() {
      return this.initGuestBoardPwCheckRequest(this.deleteBoardDialog);
    },
  },
  async created() {
    this.searchCondition = {...this.$route.query};
    let boardId = this.$route.params.id;
    const response = await this.$_BoardService.fetchBoard(boardId);
    this.boardDetail = response.data.boardDetail;
    console.log(this.boardDetail);
  },
  methods: {
    initGuestBoardPwCheckRequest(dialog) {
      if(!dialog) {
        this.guestBoardPwCheckRequest = {};
      }
    },
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
      this.comment.commentContent = "";
      this.comment.guestNickname = "";
      this.comment.guestPassword = "";
    },
    async pwCheck(action) {
      this.guestBoardPwCheckRequest.boardId = this.boardDetail.boardId;
      try {
        if(action === 'delete') {
          // TODO: 회원 게시글 삭제 해야됨..
          await this.$_BoardService.removeBoard(this.guestBoardPwCheckRequest);
          alert('삭제되었습니다.');
          this.goToBoardList();
        } else if(action === 'modify') {
          await this.$_BoardService.checkBoardPw(this.guestBoardPwCheckRequest);
          this.goToBoardModify();
        }
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
    goToBoardModify() {
      this.$router.push({
        path: `/boards/${this.boardDetail.boardId}/modify`
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