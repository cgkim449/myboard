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
//TODO: 토큰 만료됐을때 뭔가 요청을 보내면 로그아웃? 아니면 로그아웃되셨습니다 alert 보여주고 현재 있는 페이지 새로고침?
export default {
  name: "LoginPage",
  data() {
    const defaultForm = Object.freeze({
      username: "",
      password: "",
    })
    return {
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
      console.log("login 엔터!")
      if (this.validateForm()) {
        const user = {
          username: this.username,
          password: this.password,
        };
        try {
          const data = await this.$store.dispatch("LOGIN", user);
          await this.$router.push("/boards");
          //TODO: 원래 가려던 페이지로 이동해야함
        } catch (error) {
          alert(error.response.data.errorMessage)
        }
      }
    },
  }
}
</script>

<style scoped>

</style>