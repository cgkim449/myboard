<template>
  <v-container>
    <PageTitle>
      <h2 slot="title">
        Q&A
      </h2>
    </PageTitle>

    <v-row>
      <v-col
          cols="12"
      >
        <v-card flat outlined class="px-8 py-8">
          <v-form
              ref="form"
          >
            <v-container fluid>
              <v-row>
                <v-col
                    cols="2"
                    align-self="center"
                >
                  <div>
                    공개 여부
                  </div>
                </v-col>

                <v-col cols="10">
                  <v-radio-group
                      v-model="form.isSecret"
                      row
                  >
                    <v-radio
                        value="0"
                    >
                      <template v-slot:label>
                        <div>공개</div>
                      </template>
                    </v-radio>

                    <v-radio
                        value="1"
                    >
                      <template v-slot:label>
                        <div>비공개</div>
                      </template>
                    </v-radio>
                  </v-radio-group>
                </v-col>


                <v-col
                    cols="2"
                >
                  <v-select
                      disabled
                      readonly
                      v-model="questionDetail.categoryId"
                      :items="categories"
                      item-text="categoryName"
                      item-value="categoryId"
                      label="카테고리"
                      color="pink"
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
                <v-card-text>
                  <v-row>
                    <v-col>
                      <p v-for="attach in questionDetail.attachList">
                        <span v-on:click="$_QuestionService.downloadAttach(attach.attachId)" style="cursor:pointer;">

                          <v-icon>mdi-attachment</v-icon>
                          {{attach.name}}.{{attach.extension}} - {{attach.size}} byte

                        </span>

                        <v-btn x-small text color="red" v-on:click="removeOldAttach(attach)">x</v-btn>
                      </p>

                      <p v-for="multipartFile in form.multipartFiles">
                        <v-icon>mdi-attachment</v-icon>

                        {{multipartFile.name}} - {{multipartFile.size}} byte

                        <v-btn x-small text color="red" v-on:click="removeNewAttach(multipartFile)">x</v-btn>
                      </p>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-row>


              <v-row v-if="questionDetail.attachList.length + form.multipartFiles.length < 3">
                <v-file-input
                    v-on:change="addNewAttach" v-bind:key="fileInputKey"
                    show-size
                    label="첨부파일"
                ></v-file-input>
              </v-row>


              <v-row>
                <v-col
                    cols="auto"
                >
                  <v-btn
                      @click="moveToQuestionDetail(questionDetail.questionId)"
                      outlined
                      text
                  >
                    취소
                  </v-btn>
                </v-col>

                <v-spacer></v-spacer>

                <v-col
                    cols="auto"
                >
                  <v-btn
                      outlined
                      text
                      color="primary"
                      @click="modifyQuestion"
                  >
                    저장
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import PageTitle from "@/components/common/PageTitle";

export default {
  name: "QuestionModifyView",
  components: {
    PageTitle
  },
  data() {
    return {
      form: {
        isSecret: "1",
        multipartFiles: [],
        deleteAttaches: [],
      },

      questionDetail: {
        attachList: [],
      },

      rules: {
        title: [value => this.$_ItemFormValidator.validateTitle(value),],
        content: [value => this.$_ItemFormValidator.validateContent(value)],
      },

      fileInputKey: 0,

      categories: [
        {categoryName: 'Java', categoryId: 1},
        {categoryName: 'JavaScript', categoryId: 2},
        {categoryName: 'Database', categoryId: 3},
      ],
    }
  },
  async created() {
    let questionId = this.$route.params.questionId;

    const {data} = await this.$_QuestionService.fetchQuestion(questionId);

    this.questionDetail = data.questionDetail;

    this.form.isSecret = String(data.questionDetail.isSecret);
    this.form.title = data.questionDetail.title;
    this.form.content = data.questionDetail.content;
  },
  methods: {
    validateForm() {
      return this.$refs.form.validate()
    },

    removeNewAttach(item) {
      this.form.multipartFiles.splice(this.form.multipartFiles.indexOf(item), 1);
    },

    addNewAttach(file) {
      console.log(file)

      this.form.multipartFiles.push(file);

      this.fileInputKey++;
    },

    removeOldAttach(attach) {
      this.form.deleteAttaches.push(attach.attachId);

      let index = this.questionDetail.attachList.indexOf(attach);

      this.questionDetail.attachList.splice(index, 1);
    },

    async modifyQuestion() {
      if (this.validateForm()) {
        const validationResult = this.$_ItemFormValidator.validateMultipartFiles(this.form.multipartFiles);

        if (validationResult !== true) {
          alert(validationResult);
          return;
        }

        let formData = this.prepareFormData();

        try {

          await this.$_QuestionService.updateQuestion(formData);
        } catch (error) {

          alert(error.response.data.errorMessage)
        }

        this.moveToQuestionDetail(this.questionDetail.questionId);
      }
    },

    moveToQuestionDetail(questionId) {
      this.$router.push({
        name: "QuestionDetailView",
        params: {
          questionId: questionId
        },
        query: this.$route.query
      }).catch(() => {
      });
    },

    prepareFormData() {
      let formData = new FormData();

      formData.append("questionId", this.questionDetail.questionId);
      formData.append("isSecret", this.form.isSecret);
      formData.append("title", this.form.title);
      formData.append("content", this.form.content);
      formData.append("attachDeleteRequest", this.form.deleteAttaches);

      if (this.form.multipartFiles.length > 0) {
        for (const multipartFile of this.form.multipartFiles) {
          formData.append("multipartFiles", multipartFile);
        }
      }

      return formData;
    },
  }
}
</script>

<style scoped>

</style>