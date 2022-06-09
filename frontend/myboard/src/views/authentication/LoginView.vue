<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            width="460"
            outlined
        >
          <v-form
              ref="form"
              @submit.prevent="submit"
          >
            <v-card-text class="text-center px-12 py-16">
              <div class="text-h4 font-weight-black mb-10">
                로그인
              </div>
              <v-text-field
                  v-model="username"
                  :rules="rules.username"
                  label="이메일"
                  clearable
                  prepend-icon="mdi-email"
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  :append-icon="showPasswordText ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPasswordText ? 'text' : 'password'"
                  @click:append="showPasswordText = !showPasswordText"
                  :rules="rules.password"
                  label="비밀번호"
                  clearable
                  prepend-icon="mdi-lock-outline"
                  v-on:keyup.enter="login"
              ></v-text-field>
              <v-btn
                  @click="login"
                  class="mt-6"
                  block
                  x-large
                  color="primary"
              >로그인</v-btn>
            </v-card-text>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {loginMember} from "@/api/auth";

export default {
  name: "LoginPage",
  data() {
    const defaultForm = Object.freeze({
      username: "",
      password: "",
    })
    return {
      toPath: "",
      prevRoute: null,
      showPasswordText: false,
      username: "",
      password: "",
      rules: {
        username: [val => (val || '').length > 0 || '이메일을 입력해주세요.'],
        password: [val => (val || '').length > 0 || '비밀번호를 입력해주세요.'],
      },
      defaultForm,
    }
  },
  created() {
    this.username = this.$route.query.username;
    this.toPath = this.$route.query.toPath
  },
  methods: {
    validateForm() {
      return this.$refs.form.validate()
    },
    resetForm() {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit() {
      this.resetForm()
    },
    async login() {
      if (this.validateForm()) {
        const member = {
          username: this.username,
          password: this.password,
        };
        try {
          const { data } = await loginMember(member);
          //TODO: 이거도 없앨수잇을듯. 서버에서 걍 쿠키 내려주면 돼잖슴
          this.$cookies.set("token", data.token);
          this.$cookies.set("username", data.username);
          this.$cookies.set("nickname", data.nickname);
          this.$router.push({
            path: this.toPath
            , query: this.searchCondition
          });
        } catch (error) {
          alert(error.response.data.errorMessage)
        }
      }
    },
    goToBoardList() {
      this.$router.push({
        path: '/boards'
        , query: this.searchCondition
      });
    },
  }
}
</script>

<style scoped>

</style>