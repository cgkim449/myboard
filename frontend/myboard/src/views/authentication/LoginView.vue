<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            width="460"
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
                  rounded
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
export default {
  name: "LoginPage",
  data() {
    const defaultForm = Object.freeze({
      username: "",
      password: "",
    })
    return {
      username: "",
      password: "",
      rules: {
        username: [val => (val || '').length > 0 || '필수 값입니다.'],
        password: [val => (val || '').length > 0 || '필수 값입니다.'],
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
          const response = await this.$_UserService.login(user);
          alert(`${response.data.loginResult.nickname}님, 로그인 되셨습니다.`)
          // TODO: 원래 가려던 페이지로 이동해야함
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