<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            outlined
            width="460"
        >
          <v-card-text class="text-center px-12 py-16">

            <v-form
                ref="form"
                @submit.prevent="submit"
            >

              <div class="text-h4 font-weight-black mb-10">
                회원가입
              </div>
              <v-text-field
                  v-model="username"
                  :rules="rules.username"
                  label="이메일"
                  clearable
                  prepend-icon="mdi-email"
              ></v-text-field>
              <v-text-field
                  v-model="nickname"
                  :rules="rules.nickname"
                  label="이름"
                  clearable
                  prepend-icon="mdi-email"
              ></v-text-field>
              <v-text-field
                  v-model="password"
                  :rules="rules.password"
                  label="비밀번호"
                  clearable
                  prepend-icon="mdi-lock-outline"
              >
              </v-text-field>
              <v-text-field
                  v-model="passwordConfirm"
                  :rules="rules.passwordConfirm"
                  label="비밀번호 확인"
                  clearable
                  prepend-icon="mdi-lock-outline"
                  v-on:keyup.enter="signUp"
              >
              </v-text-field>
              <v-btn
                  v-on:click="signUp"
                  class="mt-6"
                  block
                  x-large
                  color="primary"
              >
                가입하기
              </v-btn>

            </v-form>

          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "SignUpPage",
  data() {
    const defaultForm = Object.freeze({
      username: "",
      nickname: "",
      password: "",
      passwordConfirm: "",
    })
    return {
      username: "",
      nickname: "",
      password: "",
      passwordConfirm: "",
      rules: {
        nickname: [val => (3 <= (val || '').length && (val || '').length < 5) || '3글자 이상, 5글자 미만입니다.',],
        username: [val => (val || '').length > 0 || '필수 값입니다.'],
        password: [val => (this.validatePassword(val || '') || '영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다.'),],
        passwordConfirm: [val => this.password === val || `비밀번호가 일치하지 않습니다.`,],
      },
      defaultForm,
    }
  },
  methods: {
    validateForm() {
      return this.$refs.form.validate()
    },
    resetForm () {
      this.form = Object.assign({}, this.defaultForm)
      this.$refs.form.reset()
    },
    submit () {
      this.resetForm()
    },
    moveToLoginPage(username) {
      console.log(username)
      this.$router.push({
        path:`/login`,
        query: {"username": username}
      }).catch(()=>{});
    },
    async signUp() {
      if(this.validateForm()) {
        const user = {
          username: this.username,
          nickname: this.nickname,
          password: this.password,
          passwordConfirm: this.passwordConfirm,
        };
        try {
          const response = await this.$_UserService.signUp(user);
          alert(`${response.data.signUpResult.nickname}님, 회원가입 되셨습니다.`)
          this.moveToLoginPage(response.data.signUpResult.username);
        } catch (error) {
          alert(error.response.data.errorMessage)
        }
      }
    },
    validatePassword(password) {
      if(!(4 <= password.length && password.length < 16)) {
        return false;
      }
      let alphabet = 0;
      let number = 0;
      let specialSymbol = 0;

      for (let i = 0; i < password.length; i++) {
        let ascii = password.charCodeAt(i);

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
    },
  },
}
</script>

<style scoped>

</style>