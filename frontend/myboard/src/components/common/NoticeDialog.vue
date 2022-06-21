<template>
  <v-dialog
      v-model="showNoticeDialog"
      width="600px"
  >
    <v-card>
      <v-card-title class="text-h5">
        공지사항
      </v-card-title>

      <v-img
          src="http://localhost:8080/upload/2022/06/e24a95e4-f763-4a4e-8d38-41590f13adb4.png"
      >

      </v-img>

      <v-card-text>

        {{ fetchedNoticeDetail.content }}
        
      </v-card-text>

      <v-card-actions>
        <v-btn
            color="green darken-1"
            text
            @click="blockNoticeDialog"
        >
          하루동안 안보기
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn
            color="green darken-1"
            text
            @click="closeNoticeDialog"
        >
          닫기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "NoticeDialog",

  data() {
    return {
      showNoticeDialog: false,
    }
  },

  props: ["fetchedNoticeDetail"],

  created() {
    const blockNoticeCookie = this.$cookies.get("block_notice");

    console.log(blockNoticeCookie);

    if(blockNoticeCookie === null) {
      this.$emit("blockNoticeCookieNotFound")
    }
  },
  watch: {
    fetchedNoticeDetail: {
      handler() {
        this.showNoticeDialog = true;
      },

      deep: true
    },
  },
  methods: {
    blockNoticeDialog() {
      this.$cookies.set("block_notice", "true", "1d");

      this.showNoticeDialog = false;
    },
    closeNoticeDialog() {

      this.showNoticeDialog = false;
    }
  }
}
</script>

<style scoped>

</style>