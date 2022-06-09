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
                  align-self="center"
              >
                <div>
                  공개 여부
                </div>
              </v-col>
              <v-col cols="10">
                <v-radio-group
                    v-model="isSecret"
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
                    v-model="form.categoryId"
                    :items="items"

                    item-text="categoryName"
                    item-value="categoryId"

                    :rules="rules.categoryId"
                    color="pink"
                    label="카테고리"
                    required
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
              <v-col>
                <v-file-input
                    v-model="form.multipartFiles"
                    :rules="rules.multipartFiles"
                    color="deep-purple accent-4"
                    counter
                    label="첨부파일"
                    multiple
                    placeholder="파일 찾기"
                    prepend-icon="mdi-paperclip"
                    outlined
                    :show-size="1000"
                >
                  <template v-slot:selection="{ index, text }">
                    <v-chip
                        v-if="index < 3"
                        color="deep-purple accent-4"
                        dark
                        label
                        small
                    >
                      {{ text }}
                    </v-chip>

                    <span
                        v-else-if="index === 3"
                        class="text-overline grey--text text--darken-3 mx-2"
                    >
                      +{{ form.multipartFiles.length - 3 }} File(s)
                    </span>
                  </template>
                </v-file-input>
              </v-col>
            </v-row>

            <v-row>
              <v-col
                  cols="auto"
              >
<!--                TODO: 메서드로 바꾸기-->
                <router-link v-bind:to="{
                    path: `/questions`,
                    query: this.searchCondition
                  }">
                  <v-btn
                      outlined
                      text
                      @click="resetForm"
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
                    @click="resetForm"
                >
                  초기화
                </v-btn>
              </v-col>


              <v-col
                cols="auto"
              >
                <v-btn
                    outlined
                    text
                    color="primary"
                    @click="write"
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
  name: "QuestionWriteView",
  data: function () {
    const defaultForm = Object.freeze({
      categoryId: '',
      title: '',
      content: '',
      multipartFiles: [],
    })

    return {
      isSecret: "1",
      form: Object.assign({}, defaultForm),
      rules: {
        categoryId: [val => (val || '').length > 0 || '카테고리를 선택해주세요.'],
        title: [val => (4 <= (val || '').length && (val || '').length < 20) || '제목은 4글자 이상, 20글자 미만입니다.'],
        content: [val => (4 <= (val || '').length && (val || '').length < 2000) || '내용은 4글자 이상, 2000글자 미만입니다.'],
        multipartFiles: [value => {
          return this.validateMultipartFiles(value);
        },],
      },
      defaultForm,

      searchCondition: {},
      items: [
        {categoryName: 'Java', categoryId: "1"},
        {categoryName: 'JavaScript', categoryId: "2"},
        {categoryName: 'Database', categoryId: "3"},
      ],
    }
  },
  created() {
    this.searchCondition = {...this.$route.query};
  },

  computed: {
  },

  methods: {
    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit () {
      this.resetForm()
    },
    async write() {
      if(this.validateForm()) {
        let formData = this.prepareFormData();
        let response;
        response = await this.$_QuestionService.writeQuestion(formData);
        this.moveToDetail(response.headers.location);
      }
    },
    moveToDetail(location) {
      this.$router.push({
        path:location,
        query: this.searchCondition
      }).catch(()=>{});
    },
    prepareFormData() {
      let formData = new FormData();

      formData.append("categoryId", this.form.categoryId);
      formData.append("title", this.form.title);
      formData.append("content", this.form.content);
      formData.append("isSecret", this.isSecret);

      if(this.form.multipartFiles.length > 0) {
        for (const multipartFile of this.form.multipartFiles) {
          formData.append("multipartFiles", multipartFile);
        }
      }

      return formData;
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    validateMultipartFiles(multipartFiles) {

      if(multipartFiles.length > 3) {
        return '첨부파일은 3개까지 업로드 가능합니다.';
      } else {
        const regex = new RegExp("(.*?)\.(jsp|jspx|jsw|jsv|jspf|htm|html)$");
        const maxSize = 10 * 1024 * 1024; // 10MB
        const maxTotalSize = 30 * 1024 * 1024; // 30MB
        let totalSize = 0;

        for (const multipartFile of multipartFiles) {
          totalSize += multipartFile.size;

          if(multipartFile.size >= maxSize) {
            return "10MB이상의 파일은 업로드할 수 없습니다.";
          }

          if(regex.test(multipartFile.name)) {
            return "해당 확장자의 파일은 업로드할 수 없습니다.";
          }
        }

        if(totalSize >= maxTotalSize) {
          return "첨부파일은 총 30MB이상 업로드할 수 없습니다.";
        }

        return true;
      }
    },
  },
}
</script>

<style scoped>

</style>