<template>
  <v-container>
    <PageTitle>
      <h2 slot="title" @click="moveToNoticeList" v-bind:style="{ cursor: 'pointer' }">
        공지사항
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
                    cols="12"
                >
                  <v-text-field
                      label="제목"
                      v-model="form.title"
                      :rules="rules.title"
                  ></v-text-field>
                </v-col>

                <v-col cols="12">
                  <v-textarea
                      outlined
                      v-model="form.content"
                      :rules="rules.content"
                      label="내용"
                      color="teal"
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
                      multiple
                      label="첨부파일"
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
                  <v-btn
                      @click="moveToNoticeList"
                      outlined
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
                      @click="resetForm"
                  >
                    초기화
                  </v-btn>
                </v-col>

                <v-col
                    cols="auto"
                >
                  <v-btn
                      color="secondary"
                      @click="writeNotice"
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
  name: "NoticeWriteView",
  components: {
    PageTitle,
  },
  data: function () {
    const defaultForm = Object.freeze({
      categoryId: '',
      title: '',
      content: '',
      multipartFiles: [],
    })
    return {
      defaultForm,
      form: Object.assign({}, defaultForm),

      rules: {
        title: [value => this.$_ItemFormValidator.validateTitle(value),],
        content: [value => this.$_ItemFormValidator.validateContent(value),],
        multipartFiles: [value => this.$_ItemFormValidator.validateMultipartFiles(value),],
      },
    }
  },
  created() {},
  computed: {},
  methods: {
    validateForm() {
      return this.$refs.form.validate()
    },

    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },

    async writeNotice() {
      if(this.validateForm()) {

        let formData = this.prepareFormData();

        let response;

        response = await this.$_NoticeService.writeNotice(formData);

        this.moveToNoticeDetail(response.headers.location);
      }
    },

    prepareFormData() {
      let formData = new FormData();

      formData.append("title", this.form.title);
      formData.append("content", this.form.content);

      if(this.form.multipartFiles.length > 0) {
        for (const multipartFile of this.form.multipartFiles) {
          formData.append("multipartFiles", multipartFile);
        }
      }

      return formData;
    },

    moveToNoticeDetail(location) {
      this.$router.push({
        path: `${location}`,
        query: this.$route.query
      }).catch(()=>{});
    },

    moveToNoticeList() {
      this.$router.push({
        name: "NoticeListView",
        query: this.$route.query
      });
    },
  },
}
</script>

<style scoped>

</style>