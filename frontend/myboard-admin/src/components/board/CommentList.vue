<template>
  <div>
    <v-card-title class="text-subtitle-1 grey--text">
      댓글 {{ commentTotalCount }}개
    </v-card-title>
    <v-card-text v-for="comment in fetchedCommentList">
      <v-row dense>
        <v-col
            cols="2"
        >
          <template v-if="comment.guestNickname != null">
            {{comment.guestNickname}}
          </template>
          <template v-else-if="comment.memberNickname != null">
            {{comment.memberNickname}}
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
              @click="clickDeleteCommentBtn(comment.commentId)"
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

  </div>
</template>

<script>
export default {
  name: "CommentList",
  props: ["fetchedCommentList", "commentDeleteResponseStatus"],
  data() {
    return {
      deleteCommentRequest: {
        commentId: 0,
      },
    }
  },
  computed: {
    commentTotalCount() {
      if(this.fetchedCommentList === undefined) {
        return
      }
      return this.fetchedCommentList.length
    },
  },
  watch: {

    commentDeleteResponseStatus() {
      if(this.commentDeleteResponseStatus === 204) {
        this.deleteCommentRequest = {};
        this.$emit("initCommentDeleteResponseStatus")
      }
    },
  },
  methods: {
    initDeleteCommentRequest(dialog) {
      if(!dialog) {
        this.deleteCommentRequest = {};
      }
    },

    clickDeleteCommentBtn(commentId) {
      this.deleteCommentRequest.commentId = commentId;

      this.$emit("deleteCommentBtnClick", this.deleteCommentRequest);


    },
  },
}
</script>

<style scoped>

</style>