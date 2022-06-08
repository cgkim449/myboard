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
              <template v-if="comment.nickname == null">
                <v-col
                    cols="2"
                >
                  {{comment.guestNickname}}
                </v-col>
              </template>
              <template v-else>
                <v-col
                    cols="2"
                >
                  {{comment.nickname}}
                </v-col>
              </template>
              <v-col>
                {{comment.commentContent}}
              </v-col>



<!--익명 댓글-->
              <template v-if="comment.nickname === null">
                <v-col
                    cols="auto"
                >
                      <v-btn
                          icon
                          x-small
                          color="red lighten-2"
                          class="mb-1"
                          von="on"

                          @click.stop="removeCommentDialog = true"
                          @click="clickRemoveGuestCommentDialog(comment.commentId)"
                      >
                        x
                      </v-btn>
                </v-col>
              </template>
<!--              로그인사용자: 내댓글 삭제-->
              <template v-else-if="$_Cookie.getValueFromCookie('nickname') === comment.nickname">
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
              </template>
















              <v-col
                  cols="auto"
              >
                {{comment.commentRegisterDate}}
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
                <template v-if="$_Cookie.getValueFromCookie('token') !== ''">
                  <v-text-field  disabled dense style="height: 48px !important; " outlined  v-model="$_Cookie.getValueFromCookie('nickname')">
                  </v-text-field>
                </template>

                <template v-else>
                  <v-text-field
                      dense
                      style="height: 49px !important;"
                      label="닉네임"
                      outlined
                      v-model="comment.guestNickname"
                  >
                  </v-text-field>
                  <v-text-field
                      dense
                      style="height: 50px !important;"
                      type="password"
                      label="비밀번호"
                      outlined
                      v-model="comment.guestPassword"
                  >
                  </v-text-field>
                </template>
              </v-col>
              <v-col>
                <v-textarea
                    dense
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
                    height="90"
                    width="94"
                    color="primary"
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
                      color="primary"
                  >
                    목록
                  </v-btn>
                </router-link>
              </v-col>

<!--              1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임.-->
                <template v-if="$_Cookie.getValueFromCookie('token') !== '' && $_Cookie.getValueFromCookie('username') === boardDetail.username">
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




<!--익명댓글 삭제 모달-->
    <v-dialog
        v-model="removeCommentDialog"
        max-width="290"
    >
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
                    v-model="guestCommentPwCheckRequest.guestPassword"
                    label="비밀번호를 입력해주세요."
                    v-on:keyup.enter="removeComment(guestCommentPwCheckRequest.commentId)"
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
              @click="removeCommentDialog = false"
          >
            취소
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="removeComment(guestCommentPwCheckRequest.commentId)"
          >
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>



  </v-container>
</template>

<script>
export default {
  name: "BoardDetailView",
  data() {
    const defaultForm = Object.freeze({
      guestNickname: '',
      guestPassword: '',
      commentContent: '',
    })

    return {
      defaultForm,
      modifyBoardDialog: false,
      deleteBoardDialog: false,
      removeCommentDialog: false,
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
      guestCommentPwCheckRequest: {
        guestPassword: "",//TODO: 없애기. 왜냐면 회원 댓글 삭제할때도 이거 날라감.
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
    closeRemoveCommentDialog() {
      return this.initGuestCommentPwCheckRequest(this.removeCommentDialog);
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
//TODO: 댓글 작성 유효성 검증 만들어야됨. 중복 코드임.
    validateBoardPw(guestPassword) {
      if(!(4 <= guestPassword.length && guestPassword.length < 16)) {
        return false;
      }
      let alphabet = 0;
      let number = 0;
      let specialSymbol = 0;

      for (let i = 0; i < guestPassword.length; i++) {
        let ascii = guestPassword.charCodeAt(i);

        if (!('!'.charCodeAt(0) <= ascii && ascii <= '~'.charCodeAt(0))) {
          return false;
        } else if (('A'.charCodeAt(0) <= ascii && ascii <= 'Z'.charCodeAt(0)) || ('a'.charCodeAt(0) <= ascii && ascii <= 'z'.charCodeAt(0))) {
          alphabet++;
        } else if ('0'.charCodeAt(0) <= ascii && ascii <= '9'.charCodeAt(0)) {
          number++;
        } else {
          specialSymbol++;
        }
      }

      return alphabet != 0 && number != 0 && specialSymbol != 0;
    },
    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit () {
      this.resetForm()
    },
    initGuestBoardPwCheckRequest(dialog) {
      if(!dialog) {
        this.guestBoardPwCheckRequest = {};
      }
    },
    initGuestCommentPwCheckRequest(dialog) {
      if(!dialog) {
        this.guestCommentPwCheckRequest = {};
      }
    },
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    async writeComment() {
      if(this.validateForm()) {
        try {
          this.comment.boardId = this.boardDetail.boardId;

          await this.$_BoardService.writeComment(this.comment);

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
      this.comment.commentContent = "";
      this.comment.guestNickname = "";
      this.comment.guestPassword = "";
    },
    async clickRemoveGuestCommentDialog(commentId) {
      console.log(commentId)
      this.guestCommentPwCheckRequest.commentId = commentId;
    },
    async removeComment(commentId) {
      this.guestCommentPwCheckRequest.commentId = commentId;
      try {
          await this.$_BoardService.removeComment(this.guestCommentPwCheckRequest);
          alert('삭제되었습니다.');
          this.removeCommentDialog = false
          const response = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);
          this.boardDetail.commentList = response.data.commentList;
          this.guestCommentPwCheckRequest = {};
      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },
    async pwCheck(action) {
      this.guestBoardPwCheckRequest.boardId = this.boardDetail.boardId;
      try {
        if(action === 'delete') {
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