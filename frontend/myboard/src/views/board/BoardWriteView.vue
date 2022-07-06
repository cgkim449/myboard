<template>
    <v-container>
        <CommonPageTitle>
            <h2 slot="title" @click="$_routerPush.goToBoardList($route.query)" v-bind:style="{ cursor: 'pointer' }">
                자유게시판
            </h2>
        </CommonPageTitle>

        <v-row>
            <v-col
                cols="12"
            >
                <v-card flat outlined class="px-8 py-8">
                    <v-form
                        ref="form"
                    >
                        <v-container fluid>

                            <template v-if="!$store.getters.loggedIn">
                                <v-row>
                                    <v-col>
                                        <v-text-field
                                            v-model="form.guestNickname"
                                            :rules="rules.guestNickname"
                                            required
                                            label="닉네임"
                                            color="purple darken-2"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col>
                                        <v-text-field
                                            v-model="form.guestPassword"
                                            :rules="rules.guestPassword"
                                            required
                                            type="password"
                                            label="비밀번호"
                                            color="purple darken-2"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col>
                                        <v-text-field
                                            v-model="form.guestPasswordConfirm"
                                            :rules="rules.guestPasswordConfirm"
                                            required
                                            type="password"
                                            label="비밀번호 확인"
                                            color="blue darken-2"
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
                                        :items="categories"
                                        :rules="rules.categoryId"
                                        required
                                        item-text="categoryName"
                                        item-value="categoryId"
                                        label="카테고리"
                                        color="pink"
                                    ></v-select>
                                </v-col>
                                <v-col
                                    cols="10"
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
                                        @click="$_routerPush.goToBoardList($route.query)"
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
                                        @click="writeBoard"
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
import CommonPageTitle from "@/components/common/CommonPageTitle";

//TODO: 이미지 첨부햇을때 미리보기 기능?
/**
 * 게시물 작성 페이지
 */
export default {
    name: "BoardWriteView",

    components: {
        CommonPageTitle,
    },

    data: function () {
        const defaultForm = Object.freeze({
            guestNickname: '',
            guestPassword: '',
            guestPasswordConfirm: '',
            categoryId: '',
            title: '',
            content: '',
            multipartFiles: [],
        })
        return {
            defaultForm,
            form: Object.assign({}, defaultForm),

            rules: {
                guestNickname: [value => this.$_commonFormValidator.validateGuestNickname(value),],
                guestPassword: [value => this.$_commonFormValidator.validateGuestPassword(value),],
                guestPasswordConfirm: [value => this.$_commonFormValidator.validateGuestPasswordConfirm(value, this.form.guestPassword),],
                categoryId: [value => this.$_commonFormValidator.validateCategoryId(value),],
                title: [value => this.$_commonFormValidator.validateTitle(value),],
                content: [value => this.$_commonFormValidator.validateContent(value),],
                multipartFiles: [value => this.$_commonFormValidator.validateMultipartFiles(value),],
            },

            categories: [
                {categoryName: 'Java', categoryId: "1"},
                {categoryName: 'JavaScript', categoryId: "2"},
                {categoryName: 'Database', categoryId: "3"},
            ],
        }
    },

    created() {},

    computed: {},

    methods: {
        validateForm() {
            return this.$refs.form.validate()
        },

        resetForm() {
            this.form = Object.assign({}, this.defaultForm)
            this.$refs.form.reset()
        },

        //TODO: 예외처리
        async writeBoard() {
            if (this.validateForm()) {

                let formData = this.prepareFormData();

                let response;
                if (this.$store.getters.loggedIn) {
                    response = await this.$_boardService.writeMemberBoard(formData);
                } else {
                    response = await this.$_boardService.writeGuestBoard(formData);
                }

                await this.$_routerPush.goToBoardDetailByLocationHeader(response.headers.location, this.$route.query);
            }
        },

        prepareFormData() {
            let formData = new FormData();

            formData.append("guestNickname", this.form.guestNickname);
            formData.append("guestPassword", this.form.guestPassword);
            formData.append("guestPasswordConfirm", this.form.guestPasswordConfirm);
            formData.append("categoryId", this.form.categoryId);
            formData.append("title", this.form.title);
            formData.append("content", this.form.content);

            if (this.form.multipartFiles.length > 0) {
                for (const multipartFile of this.form.multipartFiles) {
                    formData.append("multipartFiles", multipartFile);
                }
            }

            return formData;
        },
    },
}
</script>

<style scoped>

</style>