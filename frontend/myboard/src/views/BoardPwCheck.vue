<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            width="460"
        >
          <v-card-text class="text-center px-12 py-16">
            <div class="text-h4 font-weight-black mb-10">
              비밀번호 확인
            </div>
            <v-text-field
                v-model="board.guestPassword" v-on:keydown.enter.exact.prevent="pwCheck"
                label="비밀번호를 입력해주세요."
                clearable
                prepend-icon="mdi-lock-outline"
            ></v-text-field>

            <router-link v-bind:to="{path: `/boards/${board.boardId}`, query: searchCondition}">
              <v-btn
                  class="mt-6"
                  large
                  outlined
                  color="primary"
              >취소</v-btn>
            </router-link>
            <v-btn
                v-on:click="pwCheck()"
                class="mt-6"
                large
                color="primary"
            >확인</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
export default {
  name: "PwCheck",
  data() {
    return {
      searchCondition: {},
      board: {},
      action: ''
    }
  },
  computed: {
  },
  created() {
    this.board.boardId = this.$route.params.id;
    this.action = this.$route.query.action; // 수정 or 삭제

    this.searchCondition.page = this.$route.query.page;
    this.searchCondition.categoryId = this.$route.query.categoryId;
    this.searchCondition.keyword = this.$route.query.keyword;
    this.searchCondition.fromDate = this.$route.query.fromDate;
    this.searchCondition.toDate = this.$route.query.toDate;
  },
  methods: {
    async pwCheck() {
      try {
        if(this.action === 'delete') {
            await this.$_BoardService.removeBoard(this.board);
            alert('삭제되었습니다.');
            this.goToBoardList();
        } else if(this.action === 'modify') {
          console.log(this.board.guestPassword)
          await this.$_BoardService.checkBoardPw(this.board);
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
        path: `/boards/${this.board.boardId}/modify`
        , query: this.searchCondition});
    }
  }
}
</script>

<style scoped>

</style>