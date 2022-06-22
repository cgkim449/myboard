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
          >
            <v-card-text class="text-center px-12 py-16">
              <div class="text-h4 font-weight-black mb-10">
                관리자 로그인
              </div>
              <v-text-field
                  v-model="username"
                  :rules="rules.username"
                  label="이메일"
                  prepend-icon="mdi-email"
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  :rules="rules.password"
                  :type="showPasswordText ? 'text' : 'password'"
                  @click:append="showPasswordText = !showPasswordText"
                  :append-icon="showPasswordText ? 'mdi-eye' : 'mdi-eye-off'"
                  v-on:keyup.enter="login"
                  label="비밀번호"
                  prepend-icon="mdi-lock-outline"
              ></v-text-field>
              <v-btn
                  @click="login"
                  class="mt-6"
                  block
                  x-large
                  color="secondary"
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
    return {
      showPasswordText: false,

      toPath: "",

      username: "",
      password: "",

      rules: {
        username: [val => (val || '').length > 0 || '이메일을 입력해주세요.'],
        password: [val => (val || '').length > 0 || '비밀번호를 입력해주세요.'],
      },
    }
  },
  created() {
    this.username = this.$route.query.username;
    this.toPath = this.$route.query.toPath;

    if(this.toPath === undefined) {
      this.toPath = "/boards";
    }
  },
  methods: {
    async login() {
      if (this.validateForm()) {

        const member = {
          username: this.username,
          password: this.password,
        };

        try {
          await this.$store.dispatch("LOGIN", member);

          await this.$router.push({
            path: this.toPath,
            query: this.searchCondition
          });

        } catch (error) {
          alert(error.response.data.errorMessage)
        }
      }
    },
    validateForm() {
      return this.$refs.form.validate()
    },
  }
}
</script>

<style scoped>

</style>