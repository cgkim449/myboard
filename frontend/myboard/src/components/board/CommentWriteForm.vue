<template>
  <v-form
      ref="form"
  >
    <v-card-text>
      <v-row dense>
        <v-col
            cols="2"
        >
          <template v-if="$store.getters.loggedIn">
            <!--                  TODO: state 에 직접 v-model 걸지 말아야 -->
            <v-text-field  disabled dense style="height: 48px !important; " outlined  v-model="$store.state.nickname">
            </v-text-field>
          </template>

          <template v-else>
            <v-text-field
                dense
                style="height: 49px !important;"
                label="닉네임"
                outlined
                v-model="saveCommentRequest.guestNickname"
            >
            </v-text-field>

            <v-text-field
                dense
                style="height: 50px !important;"
                type="password"
                label="비밀번호"
                outlined
                v-model="saveCommentRequest.guestPassword"
            >
            </v-text-field>
          </template>
        </v-col>

        <v-col>
          <v-textarea
              dense
              v-model="saveCommentRequest.content"
              outlined
              rows="3"
              label="
                          댓글을 입력해주세요.
                          "
              v-on:keydown.enter.exact.prevent="clickSaveCommentBtn"
              v-on:keydown.enter.shift.exact.prevent="saveCommentRequest.content += '\n'"
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
      saveCommentRequest: {},
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
      //TODO: 프론트에서 유효성 검증
      // if(this.validateForm()) {
        this.$emit("saveCommentBtnClick", this.saveCommentRequest);
      // }
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    initComment() {
      this.saveCommentRequest.content = "";
      this.saveCommentRequest.guestNickname = "";
      this.saveCommentRequest.guestPassword = "";
    },
  },
}
</script>

<style scoped>

</style>