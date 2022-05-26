<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            width="460"
        >
          <v-card-text class="text-center px-12 py-16">
            <div class="text-h4 font-weight-black mb-10">
              회원가입
            </div>
            <v-text-field
                v-model="username"
                label="이메일"
                clearable
                prepend-icon="mdi-email"
            ></v-text-field>
            <v-text-field
                v-model="nickname"
                label="이름"
                clearable
                prepend-icon="mdi-email"
            ></v-text-field>
            <v-text-field
                v-model="password"
                label="비밀번호"
                clearable
                prepend-icon="mdi-lock-outline"
            >
            </v-text-field>
            <v-text-field
                v-model="passwordConfirm"
                label="비밀번호 확인"
                clearable
                prepend-icon="mdi-lock-outline"
            >
            </v-text-field>
            <v-btn
                v-on:click="signUp"
                class="mt-6"
                block
                x-large
                rounded
                color="primary"
            >
              가입하기
            </v-btn>
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
    return {
      username: "",
      nickname: "",
      password: "",
      passwordConfirm: "",
    }
  },
  methods: {
    async signUp() {
      const user = {
        username: this.username,
        nickname: this.nickname,
        password: this.password,
      };
      const response = await this.$_UserService.signUp(user);
      console.log(response);
      this.initForm();
    },
    initForm() {
      this.username = "";
      this.password = "";
      this.passwordConfirm = "";
      this.nickname = "";
    }
  },
}
</script>

<style scoped>

</style>