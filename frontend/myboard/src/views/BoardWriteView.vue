<template>
  <v-container>
      <v-card flat>
        <v-form
            ref="form"
            @submit.prevent="submit"
        >
          <v-container fluid>
            <template v-if="$store.getters.isLogin">

            </template>

            <template v-else>
              <v-row>
                <v-col>
                  <v-text-field
                      v-model="form.guestNickname"
                      :rules="rules.guestNickname"
                      color="purple darken-2"
                      label="닉네임"
                      required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                      v-model="form.guestPassword"
                      :rules="rules.guestPassword"
                      color="purple darken-2"
                      type="password"
                      label="비밀번호"
                      required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                      v-model="form.guestPasswordConfirm"
                      :rules="rules.guestPasswordConfirm"
                      color="blue darken-2"
                      type="password"
                      label="비밀번호 확인"
                      required
                  ></v-text-field>
                </v-col>
              </v-row>
            </template>

            <v-row>
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
                    v-model="form.boardTitle"
                    :rules="rules.boardTitle"
                ></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-textarea
                    v-model="form.boardContent"
                    :rules="rules.boardContent"
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
                <router-link v-bind:to="{
                    path: `/boards`,
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
                    @click="writeBoard"
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
  name: "BoardWriteView",
  data: function () {
    const defaultForm = Object.freeze({
      guestNickname: '',
      guestPassword: '',
      guestPasswordConfirm: '',
      categoryId: '',
      boardTitle: '',
      boardContent: '',
      multipartFiles: [],
    })

    return {
      form: Object.assign({}, defaultForm),
      rules: {
        guestNickname: [val => (3 <= (val || '').length && (val || '').length < 5) || '3글자 이상, 5글자 미만입니다.',],
        guestPassword: [val => (this.validateBoardPw(val || '') || '영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다.'),],
        guestPasswordConfirm: [val => this.form.guestPassword === val || `비밀번호가 일치하지 않습니다.`,],
        categoryId: [val => (val || '').length > 0 || '카테고리를 선택해주세요.'],
        boardTitle: [val => (4 <= (val || '').length && (val || '').length < 20) || '제목은 4글자 이상, 20글자 미만입니다.'],
        boardContent: [val => (4 <= (val || '').length && (val || '').length < 2000) || '내용은 4글자 이상, 2000글자 미만입니다.'],
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
    async writeBoard() {
      if(this.validateForm()) {
        let formData = this.prepareFormData();

        const response = await this.$_BoardService.writeBoard(formData);
        this.moveToBoardDetail(response.headers.location);
      }
    },
    moveToBoardDetail(location) {
      this.$router.push({
        path:location,
        query: this.searchCondition
      }).catch(()=>{});
    },
    prepareFormData() {
      let formData = new FormData();

      formData.append("guestNickname", this.form.guestNickname);
      formData.append("guestPassword", this.form.guestPassword);
      formData.append("guestPasswordConfirm", this.form.guestPasswordConfirm);
      formData.append("categoryId", this.form.categoryId);
      formData.append("boardTitle", this.form.boardTitle);
      formData.append("boardContent", this.form.boardContent);

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
    validateBoardPw(guestPassword) {
      if(!(4 <= guestPassword.length && guestPassword.length < 16)) {
        return false;
      }
      let alphabet = 0;
      let number = 0;
      let specialSymbol = 0;

      for (let i = 0; i < guestPassword.length; i++) {
        let ascii = guestPassword.charCodeAt(i);

        if (!('!'.charCodeAt(0) <= ascii && ascii <= '~'.charCodeAt(0))) {
          return false;
        } else if (('A'.charCodeAt(0) <= ascii && ascii <= 'Z'.charCodeAt(0)) || ('a'.charCodeAt(0) <= ascii && ascii <= 'z'.charCodeAt(0))) {
          alphabet++;
        } else if ('0'.charCodeAt(0) <= ascii && ascii <= '9'.charCodeAt(0)) {
          number++;
        } else {
          specialSymbol++;
        }
      }

      return alphabet != 0 && number != 0 && specialSymbol != 0;
    }
  },
}
</script>

<style scoped>

</style>