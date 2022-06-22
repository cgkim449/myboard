<template>
  <v-form
      ref="form"
  >
    <v-card-text>
      <v-row dense>
        <v-col
            cols="2"
        >
          <!--                  TODO: state 에 직접 v-model 걸지 말아야 -->
          <v-text-field  disabled dense style="height: 48px !important; " outlined  v-model="$store.state.nickname">
          </v-text-field>
        </v-col>

        <v-col>
          <v-textarea
              counter
              dense
              v-model="commentSaveRequest.content"
              outlined
              rows="3"
              label="댓글을 입력해주세요."
              v-on:keydown.enter.exact.prevent="clickSaveCommentBtn"
              v-on:keydown.enter.shift.exact.prevent="commentSaveRequest.content += '\n'"
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
              v-on:click="clickSaveCommentBtn"
          >
            등록
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-form>
</template>

<script>
export default {
  name: "CommentWriteForm",
  props: ["commentSaveResponseStatus"],
  data() {
    return {
      commentSaveRequest: {
        content: "",
      },
    }
  },
  watch: {
    commentSaveResponseStatus() {
      if(this.commentSaveResponseStatus === 201) {
        this.initComment();
        this.$emit("initCommentSaveResponseStatus")
      }
    }
  },
  methods: {
    clickSaveCommentBtn() {
        this.$emit("saveCommentBtnClick", this.commentSaveRequest);
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    initComment() {
      this.commentSaveRequest.content = "";
    },
  },
}
</script>

<style scoped>

</style>