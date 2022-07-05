<template>
    <v-container>

        <CommonPageTitle>
            <h2 slot="title" @click="moveToQuestionList" v-bind:style="{ cursor: 'pointer' }">
                Q&A
            </h2>
        </CommonPageTitle>

        <v-card flat outlined class="px-8 py-8">
            <v-form
                ref="form"
            >
                <v-container fluid>
                    <v-row>
                        <v-col
                            cols="2"
                            align-self="center"
                        >
                            <div>
                                공개 여부
                            </div>
                        </v-col>

                        <v-col cols="10">
                            <v-radio-group
                                v-model="isSecret"
                                row
                            >
                                <v-radio
                                    value="0"
                                >
                                    <template v-slot:label>
                                        <div>공개</div>
                                    </template>
                                </v-radio>

                                <v-radio
                                    value="1"
                                >
                                    <template v-slot:label>
                                        <div>비공개</div>
                                    </template>
                                </v-radio>
                            </v-radio-group>
                        </v-col>

                        <v-col
                            cols="2"
                        >
                            <v-select
                                v-model="form.categoryId"
                                :items="categories"

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
                                v-model="form.title"
                                :rules="rules.title"
                            ></v-text-field>
                        </v-col>

                        <v-col cols="12">
                            <v-textarea
                                outlined
                                v-model="form.content"
                                :rules="rules.content"
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
                            <v-btn
                                @click="moveToQuestionList"
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
                                color="secondary"
                                @click="writeQuestion"
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
import CommonPageTitle from "@/components/common/CommonPageTitle";

export default {
    name: "QuestionWriteView",
    components: {
        CommonPageTitle
    },
    data: function () {
        return {
            isSecret: "1",

            form: {
                categoryId: '',
                title: '',
                content: '',
                multipartFiles: [],
            },

            rules: {
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
    created() {
    },
    computed: {},
    methods: {
        validateForm() {
            return this.$refs.form.validate()
        },

        async writeQuestion() {
            if (this.validateForm()) {
                let formData = this.prepareFormData();

                let {headers} = await this.$_questionService.writeQuestion(formData);

                this.moveToQuestionDetail(headers.location);
            }
        },

        prepareFormData() {
            let formData = new FormData();

            formData.append("categoryId", this.form.categoryId);
            formData.append("title", this.form.title);
            formData.append("content", this.form.content);
            formData.append("isSecret", this.isSecret);

            if (this.form.multipartFiles.length > 0) {
                for (const multipartFile of this.form.multipartFiles) {
                    formData.append("multipartFiles", multipartFile);
                }
            }

            return formData;
        },

        moveToQuestionList() {
            this.$router.push({
                path: '/questions'
                , query: this.$route.query
            });
        },

        moveToQuestionDetail(location) {
            this.$router.push({
                path: location,
                query: this.$route.query
            }).catch(() => {
            });
        },
    },
}
</script>

<style scoped>

</style>