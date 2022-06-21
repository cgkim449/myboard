<template>
<!--  <v-container v-if="!isEmpty(boardDetail)">-->
  <v-container>

    <v-row justify="center">
      <v-col
        cols="11"
      >
        <BoardDetail
            v-bind:fetchedBoardDetail="boardDetail"
        ></BoardDetail>

        <AttachList
            v-if="boardDetail.hasAttach"
            v-bind:fetchedAttachList="boardDetail.attachList"
        ></AttachList>

        <v-card outlined class="px-1 pt-1 mt-3">
          <CommentList
              v-on:deleteCommentBtnClick="deleteComment"
              v-on:initCommentDeleteResponseStatus="initCommentDeleteResponseStatus"
              v-bind:commentDeleteResponseStatus="commentDeleteResponseStatus"
              v-bind:fetchedCommentList="boardDetail.commentList"
          ></CommentList>

          <CommentWriteForm
              v-on:saveCommentBtnClick="saveComment"
              v-on:initCommentSaveResponseStatus="initCommentSaveResponseStatus"
              v-bind:commentSaveResponseStatus="commentSaveResponseStatus"
          ></CommentWriteForm>
        </v-card>


<!--        TODO: 컴포넌트로 분리-->
        <v-card elevation="0">
          <v-card-text>
            <v-row justify="center">
              <v-col
                cols="auto"
              >
                <router-link v-bind:to="{
                  name: 'BoardListView',
                  query: $route.query
                }">
                  <v-btn
                      color="primary"
                  >
                    목록
                  </v-btn>
                </router-link>
              </v-col>

<!--              1.(로그인 사용자)본인 글이면 수정 삭제 버튼 보임.-->
              <template v-if="boardDetail.memberNickname !== null && $store.state.username === boardDetail.memberUsername">
                <v-col
                  cols="auto"
                >
                  <router-link v-bind:to="{
                    name: 'BoardModifyView',
                    params: {
                      boardId: boardDetail.boardId
                    },
                    query: $route.query
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
                        @click="deleteMyBoard"
                    >
                      삭제
                    </v-btn>
                </v-col>
              </template>

<!--                2. 익명 글이면 누구나 수정 삭제 버튼 눌렀을시 모달 뜸-->
              <template v-else-if="boardDetail.guestNickname !== null">

                <v-col
                    cols="auto"
                >
                  <v-dialog
                      v-model="showModifyGuestBoardDialog"
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
                                  v-model="guestPasswordCheckRequest.guestPassword"
                                  label="비밀번호를 입력해주세요."
                                  v-on:keyup.enter="guestPasswordCheck('modify')"
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
                            @click="showModifyGuestBoardDialog = false"
                        >
                          취소
                        </v-btn>

                        <v-btn
                            color="blue darken-1"
                            text
                            @click="guestPasswordCheck('modify')"
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
                      v-model="showDeleteGuestBoardDialog"
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
                                  v-model="guestPasswordCheckRequest.guestPassword"
                                  label="비밀번호를 입력해주세요."
                                  v-on:keyup.enter="guestPasswordCheck('delete')"
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
                            @click="showDeleteGuestBoardDialog = false"
                        >
                          취소
                        </v-btn>

                        <v-btn
                            color="blue darken-1"
                            text
                            @click="guestPasswordCheck('delete')"
                        >
                          확인
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-col>
              </template>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

  </v-container>
</template>

<script>
import BoardDetail from "@/components/board/BoardDetail";
import AttachList from "@/components/board/AttachList";
import CommentList from "@/components/board/CommentList";
import CommentWriteForm from "@/components/board/CommentWriteForm";

export default {
  name: "BoardDetailView",
  components: {
    BoardDetail,
    AttachList,
    CommentList,
    CommentWriteForm
  },
  data() {
    return {
      boardDetail: {},

      commentSaveResponseStatus: 0,
      commentDeleteResponseStatus: 0,

      guestPasswordCheckRequest: {
        guestPassword: "",
      },

      showModifyGuestBoardDialog: false,
      showDeleteGuestBoardDialog: false,
    }
  },
  computed: {},
  async created() {
    let boardId = this.$route.params.boardId;

    const response = await this.$_BoardService.fetchBoard(boardId);

    this.boardDetail = response.data.boardDetail;
  },
  methods: {
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },

    async deleteComment(deleteCommentRequest){
      try {
        const {status} = await this.$_BoardService.deleteComment(deleteCommentRequest);
        this.commentDeleteResponseStatus = status;
        console.log(this.commentDeleteResponseStatus)
        alert('삭제되었습니다.');

        const { data } = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);

        this.boardDetail.commentList = data.commentList;
      } catch (error) {

        alert(error.response.data.errorMessage);
      }
    },

    async saveComment(commentSaveRequest) {
      try {
        commentSaveRequest.boardId = this.boardDetail.boardId;

        let commentSaveResponse;
        if(this.$store.getters.loggedIn) {
          commentSaveResponse = await this.$_BoardService.saveMemberComment(commentSaveRequest);
        } else {
          commentSaveResponse = await this.$_BoardService.saveGuestComment(commentSaveRequest)
        }
        this.commentSaveResponseStatus = commentSaveResponse.status;

        const {data} = await this.$_BoardService.fetchCommentList(this.boardDetail.boardId);
        this.boardDetail.commentList = data.commentList;
      } catch (error) {
        //TODO: 프론트에서 유효성 검증
        alert(error.response.data.fieldErrorDetails[0].fieldErrorMessage)
      }
    },
    initCommentDeleteResponseStatus() {
      this.commentDeleteResponseStatus = 0;
    },
    initCommentSaveResponseStatus() {
      this.commentSaveResponseStatus = 0;
    },

    async deleteMyBoard() {
      try {
          await this.$_BoardService.removeBoard({
            boardId: this.boardDetail.boardId
          });
          alert('삭제되었습니다.');

          this.moveToBoardList();
      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },

    async guestPasswordCheck(action) {
      this.guestPasswordCheckRequest.boardId = this.boardDetail.boardId;

      try {
        if(action === 'delete') {

          await this.$_BoardService.removeBoard(this.guestPasswordCheckRequest);
          alert('삭제되었습니다.');

          this.moveToBoardList();
        } else if(action === 'modify') {

          await this.$_BoardService.checkBoardPw(this.guestPasswordCheckRequest);

          this.moveToBoardModify();
        }
      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },

    moveToBoardList() {
      this.$router.push({
        name: "BoardListView",
        query: this.$route.query
      });
    },

    moveToBoardModify() {
      this.$router.push({
        name: "BoardModifyView",
        params: {
          boardId: this.boardDetail.boardId
        },
        query: this.$route.query,
      });
    },
  },
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