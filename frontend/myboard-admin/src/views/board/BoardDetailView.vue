<template>
<!--  <v-container v-if="!isEmpty(boardDetail)">-->
  <v-container>
    <PageTitle>
      <h2 slot="title" @click="moveToBoardList" v-bind:style="{ cursor: 'pointer' }">
        자유게시판
      </h2>
    </PageTitle>

    <v-row justify="center">
      <v-col
        cols="12"
      >
        <ItemDetail
            v-bind:fetchedItemDetail="boardDetail"
        ></ItemDetail>

        <AttachList
            v-if="boardDetail.hasAttach"
            v-bind:fetchedAttachList="boardDetail.attachList"
            v-bind:attachOf="attachOf"
        ></AttachList>

        <v-card outlined class="px-1 pt-1 mt-3">
          <CommentList
              v-on:deleteCommentBtnClick="deleteComment"
              v-on:initCommentDeleteResponseStatus="initCommentDeleteResponseStatus"
              v-bind:commentDeleteResponseStatus="commentDeleteResponseStatus"
              v-bind:fetchedCommentList="boardDetail.commentList"
          ></CommentList>

          <CommentWriteForm
              v-on:saveCommentBtnClick="writeComment"
              v-on:initCommentSaveResponseStatus="initCommentSaveResponseStatus"
              v-bind:commentSaveResponseStatus="commentSaveResponseStatus"
          ></CommentWriteForm>
        </v-card>


        <v-card elevation="0">
          <v-card-text>
            <v-row justify="center">
              <v-col
                cols="auto"
              >
                <v-btn
                    @click="moveToBoardList"
                    color="primary"
                >
                  목록
                </v-btn>
              </v-col>

              <v-col
                cols="auto"
              >
                <v-btn
                    @click="moveToBoardModify"
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
                      @click="deleteBoard"
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
import AttachList from "@/components/common/AttachList";
import CommentList from "@/components/board/CommentList";
import CommentWriteForm from "@/components/board/CommentWriteForm";
import ItemDetail from "@/components/common/ItemDetail";
import PageTitle from "@/components/common/PageTitle";

export default {
  name: "BoardDetailView",
  components: {
    PageTitle,
    ItemDetail,
    AttachList,
    CommentList,
    CommentWriteForm
  },
  data() {
    return {
      boardDetail: {},
      attachOf: "board",

      commentSaveResponseStatus: 0,
      commentDeleteResponseStatus: 0,

    }
  },
  computed: {},
  async created() {

    let boardId = this.$route.params.boardId;

    await this.fetchBoardDetail(boardId);
  },
  methods: {
    isEmpty(obj) {
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },

    async fetchBoardDetail(boardId) {
        const {data} = await this.$_BoardService.fetchBoard(boardId);

        this.boardDetail = data.boardDetail;
    },

    async fetchCommentList(boardId) {
      const { data } = await this.$_BoardService.fetchCommentList(boardId);

      this.boardDetail.commentList = data.commentList;
    },

    async deleteComment(deleteCommentRequest){
      try {
        const {status} = await this.$_BoardService.deleteComment(deleteCommentRequest);
        this.commentDeleteResponseStatus = status;

        alert('삭제되었습니다.');

        await this.fetchCommentList(this.boardDetail.boardId);

      } catch (error) {
        alert(error.response.data.errorMessage);
      }
    },

    async writeComment(commentSaveRequest) {
      try {
        commentSaveRequest.boardId = this.boardDetail.boardId;

        let commentSaveResponse;

        commentSaveResponse = await this.$_BoardService.writeComment(commentSaveRequest);

        this.commentSaveResponseStatus = commentSaveResponse.status;

        await this.fetchCommentList(this.boardDetail.boardId)

      } catch (error) {
        const firstErrorField = error.response.data.fieldErrorDetails[0].field;
        const fieldErrorMessage = error.response.data.fieldErrorDetails[0].fieldErrorMessage;

        //TODO: alert 말고 출력으로 바꾸기
        if(firstErrorField === "content") {
          alert(`댓글은 ${fieldErrorMessage}`)
        } else if(firstErrorField === "guestPassword") {
          alert(`${fieldErrorMessage}`)
        } else if(firstErrorField === "guestNickname") {
          alert(`닉네임은 ${fieldErrorMessage}`)
        }
      }
    },
    initCommentDeleteResponseStatus() {
      this.commentDeleteResponseStatus = 0;
    },
    initCommentSaveResponseStatus() {
      this.commentSaveResponseStatus = 0;
    },

    async deleteBoard() {
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