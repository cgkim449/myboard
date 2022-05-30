<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="auto">
        <v-card
            width="460"
        >
          <v-card-text class="text-center px-12 py-16">
            <div class="text-h4 font-weight-black mb-10">
              로그인
            </div>
            <v-text-field
                v-model="username"
                label="이메일"
                clearable
                prepend-icon="mdi-email"
            ></v-text-field>
            <v-text-field
                label="비밀번호"
                clearable
                prepend-icon="mdi-lock-outline"
                v-on:keyup.enter="login"
            ></v-text-field>
            <v-btn
                class="mt-6"
                block
                x-large
                rounded
                color="primary"
            >로그인</v-btn>
          </v-card-text>
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
      username: "",
    }
  },
  created() {
    this.username = this.$route.query.username;
  },
  methods: {
    login() {
      console.log("login 엔터!")
    }
  }
}
</script>

<style scoped>

</style>