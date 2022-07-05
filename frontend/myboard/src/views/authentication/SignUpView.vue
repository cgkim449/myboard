<template>
    <v-container class="fill-height">
        <v-row justify="center">
            <v-col cols="auto">
                <v-card
                    outlined
                    width="460"
                >
                    <validation-observer
                        ref="observer"
                        v-slot="{ invalid }"
                    >
                        <v-form
                            ref="form"
                        >
                            <v-card-text class="text-center px-12 py-16">
                                <div class="text-h4 font-weight-black mb-10">
                                    회원가입
                                </div>

                                <validation-provider
                                    v-slot="{ errors }"
                                    name="email"
                                    rules="required|email"
                                >
                                    <v-text-field
                                        v-model="username"
                                        :error-messages="errors"

                                        label="이메일*"
                                        prepend-icon="mdi-email"
                                    ></v-text-field>
                                </validation-provider>

                                <v-text-field
                                    v-model="nickname"
                                    :rules="rules.nickname"

                                    label="이름*"
                                    hint="3글자 이상, 5글자 미만입니다."
                                    prepend-icon="mdi-email"
                                ></v-text-field>
                                <v-text-field
                                    v-model="password"
                                    :rules="rules.password"
                                    :append-icon="showPasswordText ? 'mdi-eye' : 'mdi-eye-off'"
                                    :type="showPasswordText ? 'text' : 'password'"
                                    @click:append="showPasswordText = !showPasswordText"

                                    label="비밀번호*"
                                    hint="영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다."
                                    prepend-icon="mdi-lock-outline"
                                    counter
                                >
                                </v-text-field>
                                <v-text-field
                                    v-model="passwordConfirm"
                                    :rules="rules.passwordConfirm"
                                    :append-icon="showPasswordConfirmText ? 'mdi-eye' : 'mdi-eye-off'"
                                    :type="showPasswordConfirmText ? 'text' : 'password'"
                                    @click:append="showPasswordConfirmText = !showPasswordConfirmText"
                                    v-on:keyup.enter="signUp"

                                    label="비밀번호 확인*"
                                    prepend-icon="mdi-lock-outline"
                                    counter
                                >
                                </v-text-field>

                                <small>*필수 입력</small>

                                <v-btn
                                    v-on:click="signUp"
                                    class="mt-6"
                                    block
                                    x-large
                                    color="secondary"
                                >
                                    가입하기
                                </v-btn>
                            </v-card-text>
                        </v-form>
                    </validation-observer>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import {required, email} from 'vee-validate/dist/rules'
import {extend, setInteractionMode} from "vee-validate";

setInteractionMode('eager')

extend('required', {
    ...required,
    message: '{_field_}을 입력해주세요.',
})

extend('email', {
    ...email,
    message: 'email 형식이 맞지 않습니다.',
})

export default {
    name: "SignUpPage",
    data() {
        return {
            showPasswordText: false,
            showPasswordConfirmText: false,

            username: "",
            nickname: "",
            password: "",
            passwordConfirm: "",

            rules: {
                nickname: [val => (3 <= (val || '').length && (val || '').length < 5) || '3글자 이상, 5글자 미만입니다.',],
                password: [val => (this.validatePassword(val || '') || '영문/숫자/특수문자 포함 4글자 이상, 16글자 미만입니다.'),],
                passwordConfirm: [val => this.password === val || `비밀번호가 일치하지 않습니다.`,],
            },
        }
    },
    methods: {
        async signUp() {
            if (this.validateForm()) {

                const member = {
                    username: this.username,
                    nickname: this.nickname,
                    password: this.password,
                    passwordConfirm: this.passwordConfirm,
                };

                try {
                    const response = await this.$_memberService.signUp(member);
                    const username = response.headers.location.split("/")[2];
                    this.moveToLoginPage(username);

                } catch (error) {
                    alert(error.response.data.errorMessage)
                }
            }
        },
        validateForm() {
            return this.$refs.form.validate()
        },
        moveToLoginPage(username) {
            this.$router.push({
                path: `/login`,
                query: {"username": username}
            }).catch(() => {
            });
        },
        validatePassword(password) {
            if (!(4 <= password.length && password.length < 16)) {
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

            return alphabet !== 0 && number !== 0 && specialSymbol !== 0;
        },
    },
}
</script>

<style scoped>

</style>