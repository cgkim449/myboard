<template>
  <v-container>
    <v-card flat>

      <v-form
          ref="form"
          @submit.prevent="submit"
      >
        <v-container fluid>
          <v-row>
            <v-col
                cols="2"
            >
              <v-select
                  disabled
                  readonly
                  v-model="boardDetail.categoryId"
                  :items="items"

                  item-text="categoryName"
                  item-value="categoryId"

                  color="pink"
                  label="카테고리"
              ></v-select>
            </v-col>
            <v-col
                cols="10"
            >
              <v-text-field
                  label="제목"
                  v-model="form.title"
                  :rules="rules.title"
              ></v-text-field>
            </v-col>

            <v-col cols="12">
              <v-textarea
                  v-model="form.content"
                  :rules="rules.content"
                  color="teal"
                  label="내용"
              >
              </v-textarea>
            </v-col>
          </v-row>
          <v-row>
            <v-card-text >
              <v-row>
                <v-col>
                  <p v-for="attach in boardDetail.attachList">
                    <span v-on:click="$_BoardService.downloadAttach(attach.attachId)" style="cursor:pointer;">
                      <v-icon>mdi-attachment</v-icon>
                      {{attach.name}}.{{attach.extension}} - {{attach.size}} byte

                    </span>
                    <v-btn x-small text color="red" v-on:click="removeAttach(attach)">x</v-btn>
                  </p>
                  <p v-for="multipartFile in form.multipartFiles">
                      <v-icon>mdi-attachment</v-icon>
                    {{multipartFile.name}} - {{multipartFile.size}} byte
                    <v-btn x-small text color="red" v-on:click="removeFile(multipartFile)">x</v-btn>
                  </p>
                </v-col>
              </v-row>
            </v-card-text>
          </v-row>

          <v-row v-if="boardDetail.attachList.length + form.multipartFiles.length < 3">
            <v-file-input
                v-on:change="selectFile" v-bind:key="fileInputKey"
                show-size
                label="첨부파일"
            ></v-file-input>
          </v-row>

          <v-row>
            <v-col
                cols="auto"
            >
              <router-link v-bind:to="{
                    path: `/boards/${boardDetail.boardId}`,
                    query: this.searchCondition
                  }">
                <v-btn
                    outlined
                    text
                >
                  취소
                </v-btn>
              </router-link>
            </v-col>
            <v-spacer></v-spacer>

            <v-col
                cols="auto"
            >
              <v-btn
                  outlined
                  text
                  color="primary"
                  @click="modifyBoard"
              >
                저장
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </v-card>

  </v-container>
</template>

<script>
export default {
  name: "BoardModifyView",
  data() {
    return {
      fileInputKey: 0,
      form: {
        guestPassword: "",
        multipartFiles: [],
        deleteAttaches: [],
      },
      rules: {
        guestPassword: [val => (val || '').length > 0 || '비밀번호를 입력해주세요.'],
        title: [val => (4 <= (val || '').length && (val || '').length < 20) || '4글자 이상, 20글자 미만입니다.'],
        content: [val => (4 <= (val || '').length && (val || '').length < 2000) || '4글자 이상, 2000글자 미만입니다.'],
      },
      items: [
        {categoryName: 'Java', categoryId: 1},
        {categoryName: 'JavaScript', categoryId: 2},
        {categoryName: 'Database', categoryId: 3},
      ],
      searchCondition: {},
      boardDetail: {
        attachList: [],
      },
    }
  },
  async created() {
    this.searchCondition = {...this.$route.query};
    let boardId = this.$route.params.id;
    const response = await this.$_BoardService.fetchBoard(boardId);
    this.boardDetail = response.data.boardDetail;
    this.form.title = response.data.boardDetail.title;
    this.form.content = response.data.boardDetail.content;
  },
  methods: {
    removeFile(item) {
      this.form.multipartFiles.splice(this.form.multipartFiles.indexOf(item), 1);
    },
    selectFile(file) {
      console.log(file)
      this.form.multipartFiles.push(file);
      this.fileInputKey++;
    },
    removeAttach(attach) {
      this.form.deleteAttaches.push(attach.attachId);
      let index = this.boardDetail.attachList.indexOf(attach);
      this.boardDetail.attachList.splice(index, 1);
    },
    isEmpty(obj) {
      console.log(2)
      return Object.keys(obj).length === 0 && obj.constructor === Object;
    },
    resetForm () {},
    submit () {
      this.resetForm()
    },
    async modifyBoard () {
      if(this.validateForm()) {
        console.log(3)
        if(!this.validateMultipartFiles(this.form.multipartFiles)) {
          return;
        }
        let formData = this.prepareFormData();

        try {
          const {data} = await this.$_BoardService.updateBoard(formData);
          this.moveToBoardDetail(this.boardDetail.boardId);
        } catch (error) {
          alert(error.response.data.errorMessage)
        }

      }
    },
    moveToBoardDetail(boardId) {
      this.$router.push({
        path:`/boards/${boardId}`,
        query: this.searchCondition
      }).catch(()=>{});
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    prepareFormData() {
      let formData = new FormData();

      formData.append("guestPassword", this.form.guestPassword);
      formData.append("boardId", this.boardDetail.boardId);
      formData.append("categoryId", this.boardDetail.categoryId);
      formData.append("title", this.form.title);
      formData.append("content", this.form.content);
      formData.append("attachDeleteRequest", this.form.deleteAttaches);

      if(this.form.multipartFiles.length > 0) {
        for (const multipartFile of this.form.multipartFiles) {
          formData.append("multipartFiles", multipartFile);
        }
      }

      return formData;
    },
    validateMultipartFile(multipartFile) {
      let regex = new RegExp("(.*?)\.(jsp|jspx|jsw|jsv|jspf|htm|html)$");

      let maxSize = 10 * 1024 * 1024; // 10MB


      let fileSize = multipartFile.size;
      let fileName = multipartFile.name;
      if(fileSize >= maxSize) {
        alert("10MB이상의 파일은 업로드할 수 없습니다.");
        return false;
      }
      if(regex.test(fileName)) {
        alert("해당 확장자의 파일은 업로드할 수 없습니다.");
        return false;
      }

      return true;
    },

    validateMultipartFiles(multipartFiles) {
      if(multipartFiles.length === 0) {
        return true;
      }
      let maxTotalSize = 30 * 1024 * 1024; // 30MB
      let totalSize = 0;

      if(multipartFiles.length === 0) {
        return true;
      }
      for (const multipartFile of multipartFiles) {
        totalSize += multipartFile.size;

        if(!this.validateMultipartFile(multipartFile)) {
          return false;
        }
      }

      if(totalSize >= maxTotalSize) {
        alert("첨부파일은 총 30MB이상 업로드할 수 없습니다.");
        return false;
      }
      return true;
    },
  }
}
</script>

<style scoped>

</style>



