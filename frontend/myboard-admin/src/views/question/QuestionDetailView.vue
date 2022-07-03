<template>
    <v-container>
        <PageTitle>
            <h2 slot="title" @click="moveToQuestionList" v-bind:style="{ cursor: 'pointer' }">
                Q&A
            </h2>
        </PageTitle>

        <v-row justify="center">
            <v-col
                cols="auto"
            >

                <h3>질문</h3>
            </v-col>

            <v-spacer></v-spacer>

            <v-col
                cols="12"
            >
                <ItemDetail
                    v-bind:fetchedItemDetail="questionDetail"
                ></ItemDetail>

                <AttachList
                    v-if="questionDetail.hasAttach"
                    v-bind:fetchedAttachList="questionDetail.attachList"
                    v-bind:attachOf="attachOf1"
                ></AttachList>

            </v-col>
        </v-row>


        <v-row justify="center">
            <v-col
                cols="auto"
            >
                <h3>답변</h3>
            </v-col>

            <v-spacer></v-spacer>

            <template v-if="questionDetail.answer === null && showAnswerWriteForm === false">
                <v-col
                    cols="12"
                >
                    <v-card outlined class="pa-1">
                        <v-card-text class="mt-1">
                            <v-row algin="center">
                                <v-col
                                    cols="auto"
                                >
                                    답변 처리 중입니다.
                                </v-col>
                            </v-row>
                        </v-card-text>
                    </v-card>

                </v-col>
            </template>


            <template v-if="questionDetail.answer !== null && showAnswerModifyForm === true">
                <v-col
                    cols="12"
                >
                    <v-card outlined class="pa-2">

                        <v-form
                            ref="answerModifyForm"
                        >
                            <v-container fluid>
                                <v-row>
                                    <v-col
                                        cols="12"
                                    >
                                        <v-text-field
                                            outlined
                                            label="제목"
                                            v-model="answerModifyForm.title"
                                            :rules="answerModifyRules.title"
                                        ></v-text-field>
                                    </v-col>

                                    <v-col cols="12">
                                        <v-textarea
                                            outlined
                                            v-model="answerModifyForm.content"
                                            :rules="answerModifyRules.content"
                                            color="teal"
                                            label="내용"
                                        >
                                        </v-textarea>
                                    </v-col>
                                </v-row>
                                <v-row
                                    v-if="!(questionDetail.answer.attachList.length === 0 && answerModifyForm.multipartFiles.length === 0)">
                                    <v-card-text>
                                        <v-row>
                                            <v-col>
                                                <p v-for="attach in questionDetail.answer.attachList">
                        <span v-on:click="$_AnswerService.downloadAttach(attach.attachId)" style="cursor:pointer;">

                          <v-icon>mdi-attachment</v-icon>
                          {{ attach.name }}.{{ attach.extension }} - {{ attach.size }} byte

                        </span>

                                                    <v-btn x-small text color="red"
                                                           v-on:click="removeOldAttach(attach)">x
                                                    </v-btn>
                                                </p>

                                                <p v-for="multipartFile in answerModifyForm.multipartFiles">
                                                    <v-icon>mdi-attachment</v-icon>

                                                    {{ multipartFile.name }} - {{ multipartFile.size }} byte

                                                    <v-btn x-small text color="red"
                                                           v-on:click="removeNewAttach(multipartFile)">x
                                                    </v-btn>
                                                </p>
                                            </v-col>
                                        </v-row>
                                    </v-card-text>
                                </v-row>


                                <v-row
                                    v-if="questionDetail.answer.attachList.length + answerModifyForm.multipartFiles.length < 3">
                                    <v-col>
                                        <v-file-input
                                            outlined
                                            v-on:change="addNewAttach" v-bind:key="fileInputKey"
                                            show-size
                                            label="첨부파일"
                                        ></v-file-input>
                                    </v-col>
                                </v-row>


                                <v-row>
                                    <v-col
                                        cols="auto"
                                    >
                                        <v-btn
                                            @click="showAnswerModifyForm = !showAnswerModifyForm"
                                            outlined
                                            text
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
                                            text
                                            color="primary"
                                            @click="modifyAnswer"
                                        >
                                            저장
                                        </v-btn>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-form>

                    </v-card>
                </v-col>
            </template>
            <!--TODO: 컴포넌트 분리-->
            <template v-if="questionDetail.answer === null && showAnswerWriteForm === true">
                <v-col
                    cols="12"
                >
                    <v-card outlined class="pa-1">
                        <v-form
                            ref="answerWriteForm"
                        >
                            <v-container fluid>
                                <v-row>
                                    <v-col
                                        cols="12"
                                    >
                                        <v-text-field
                                            outlined
                                            label="제목"
                                            v-model="answerWriteForm.title"
                                            :rules="answerWriteRules.title"
                                        ></v-text-field>
                                    </v-col>

                                    <v-col cols="12">
                                        <v-textarea
                                            outlined
                                            v-model="answerWriteForm.content"
                                            :rules="answerWriteRules.content"
                                            label="내용"
                                            color="teal"
                                        >
                                        </v-textarea>
                                    </v-col>
                                </v-row>

                                <v-row>
                                    <v-col>
                                        <v-file-input
                                            v-model="answerWriteForm.multipartFiles"
                                            :rules="answerWriteRules.multipartFiles"
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
                      +{{ answerWriteForm.multipartFiles.length - 3 }} File(s)
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
                                            @click="showAnswerWriteForm = !showAnswerWriteForm"
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
                                            @click="resetAnswerWriteForm"
                                        >
                                            초기화
                                        </v-btn>
                                    </v-col>

                                    <v-col
                                        cols="auto"
                                    >
                                        <v-btn
                                            color="secondary"
                                            @click="writeAnswer"
                                        >
                                            저장
                                        </v-btn>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-form>
                    </v-card>

                </v-col>
            </template>

            <!--      답변이 있으면 보여줌-->
            <template v-if="questionDetail.answer !== null && showAnswerModifyForm === false">

                <v-col
                    cols="12"
                >
                    <ItemDetail
                        v-bind:fetchedItemDetail="questionDetail.answer"
                        v-bind:itemType="itemType"
                    ></ItemDetail>

                    <AttachList
                        v-if="questionDetail.answer.hasAttach"
                        v-bind:fetchedAttachList="questionDetail.answer.attachList"
                        v-bind:attachOf="attachOf2"
                    ></AttachList>
                </v-col>
            </template>

            <v-card elevation="0">
                <v-card-text>
                    <v-row justify="center">
                        <v-col
                            cols="auto"

                        >
                            <v-btn
                                @click="moveToQuestionList"
                                color="secondary"
                            >
                                목록
                            </v-btn>
                        </v-col>

                        <v-col
                            cols="auto"
                            v-if="questionDetail.answer === null && showAnswerWriteForm === false"
                        >
                            <v-btn
                                @click="showAnswerWriteForm = !showAnswerWriteForm"
                                color="secondary"
                            >
                                답변 작성
                            </v-btn>
                        </v-col>

                        <v-col
                            v-if="questionDetail.answer !== null && showAnswerModifyForm === false"
                            cols="auto"
                        >
                            <v-btn
                                outlined
                                @click="showAnswerModifyForm = !showAnswerModifyForm"
                                color="secondary"
                            >
                                답변 수정
                            </v-btn>
                        </v-col>

                        <v-col
                            cols="auto"
                            v-if="questionDetail.answer !== null"
                        >
                            <v-btn
                                outlined
                                color="error"
                                @click="removeAnswer()"
                            >
                                답변 삭제
                            </v-btn>
                        </v-col>

                        <v-col
                            cols="auto"
                        >
                            <v-btn
                                @click="moveToQuestionModify"
                                outlined
                                color="secondary"
                            >
                                질문 수정
                            </v-btn>
                        </v-col>

                        <v-col
                            cols="auto"
                        >
                            <v-btn
                                outlined
                                color="error"
                                @click="removeQuestionAndAnswer()"
                            >
                                질문 & 답변 삭제
                            </v-btn>
                        </v-col>

                    </v-row>
                </v-card-text>
            </v-card>
        </v-row>

        <v-row>
            <v-col>

            </v-col>
        </v-row>

    </v-container>
</template>

<script>
import ItemDetail from "@/components/common/ItemDetail";
import AttachList from "@/components/common/AttachList";
import PageTitle from "@/components/common/PageTitle";

export default {
    name: "QuestionDetailView",
    components: {
        AttachList,
        ItemDetail,
        PageTitle
    },
    data() {
        const defaultAnswerWriteForm = Object.freeze({
            title: '',
            content: '',
            multipartFiles: [],
        })


        return {
            questionDetail: {
                answer: {}
            },

            //TODO: 변수명 수정
            attachOf1: "question",
            attachOf2: "answer",
            itemType: "answer",

            showAnswerWriteForm: false,
            showAnswerModifyForm: false,

            defaultAnswerWriteForm,
            answerWriteForm: Object.assign({}, defaultAnswerWriteForm),

            answerWriteRules: {
                title: [value => this.$_CommonFormValidator.validateTitle(value),],
                content: [value => this.$_CommonFormValidator.validateContent(value),],
                multipartFiles: [value => this.$_CommonFormValidator.validateMultipartFiles(value),],
            },

            answerModifyForm: {
                multipartFiles: [],
                deleteAttaches: [],
            },
            answerModifyRules: {
                title: [value => this.$_CommonFormValidator.validateTitle(value),],
                content: [value => this.$_CommonFormValidator.validateContent(value)],
            },
            fileInputKey: 0,
        }
    },
    computed: {},
    async created() {
        let questionId = this.$route.params.questionId;

        const {data} = await this.$_QuestionService.fetchQuestion(questionId);
        this.questionDetail = data.questionDetail;

        const answer = data.questionDetail.answer;

        if (answer !== null) {
            this.answerModifyForm.title = answer.title;
            this.answerModifyForm.content = answer.content;
        }

    },
    methods: {
        validateAnswerModifyForm() {
            return this.$refs.answerModifyForm.validate()
        },

        removeNewAttach(item) {
            this.answerModifyForm.multipartFiles.splice(this.answerModifyForm.multipartFiles.indexOf(item), 1);
        },

        addNewAttach(file) {
            this.answerModifyForm.multipartFiles.push(file);

            this.fileInputKey++;
        },

        removeOldAttach(attach) {
            this.answerModifyForm.deleteAttaches.push(attach.attachId);

            let index = this.questionDetail.answer.attachList.indexOf(attach);

            this.questionDetail.answer.attachList.splice(index, 1);
        },

        async modifyAnswer() {
            if (this.validateAnswerModifyForm()) {
                const validationResult = this.$_CommonFormValidator.validateMultipartFiles(this.answerModifyForm.multipartFiles);

                if (validationResult !== true) {
                    alert(validationResult);
                    return;
                }

                let formData = this.prepareAnswerModifyFormData();

                try {
                    await this.$_AnswerService.updateAnswer(formData);

                    //TODO: 새로고침 말고 걍 api 호출해 데이터 받아오는걸로 바꾸자. 질문 작성도 마찬가지고.
                    this.$router.go();
                } catch (error) {

                    alert(error.response.data.errorMessage)
                }
            }
        },
        prepareAnswerModifyFormData() {
            let formData = new FormData();

            formData.append("answerId", this.questionDetail.answer.answerId);
            formData.append("title", this.answerModifyForm.title);
            formData.append("content", this.answerModifyForm.content);
            formData.append("attachDeleteRequest", this.answerModifyForm.deleteAttaches);

            if (this.answerModifyForm.multipartFiles.length > 0) {
                for (const multipartFile of this.answerModifyForm.multipartFiles) {
                    formData.append("multipartFiles", multipartFile);
                }
            }

            return formData;
        },


        validateAnswerWriteForm() {
            return this.$refs.answerWriteForm.validate()
        },

        resetAnswerWriteForm() {
            this.answerWriteForm = Object.assign({}, this.defaultAnswerWriteForm)
            this.$refs.answerWriteForm.reset()
        },

        prepareAnswerWriteFormData() {
            let formData = new FormData();

            formData.append("questionId", this.questionDetail.questionId);
            formData.append("title", this.answerWriteForm.title);
            formData.append("content", this.answerWriteForm.content);

            if (this.answerWriteForm.multipartFiles.length > 0) {
                for (const multipartFile of this.answerWriteForm.multipartFiles) {
                    formData.append("multipartFiles", multipartFile);
                }
            }

            return formData;
        },

        async writeAnswer() {
            if (this.validateAnswerWriteForm()) {

                let formData = this.prepareAnswerWriteFormData();

                await this.$_AnswerService.writeAnswer(formData);

                this.$router.go();
            }
        },


        async removeQuestionAndAnswer() {
            try {
                await this.$_QuestionService.removeQuestion(this.questionDetail.questionId);

                alert("삭제되었습니다.")

                this.moveToQuestionList();
            } catch (error) {
                console.log(error)
            }
        },

        async removeAnswer() {
            try {
                await this.$_AnswerService.removeAnswer(this.questionDetail.answer.answerId);

                alert("삭제되었습니다.")

                this.$router.go()
            } catch (error) {
                console.log(error)
            }
        },

        moveToQuestionList() {
            this.$router.push({
                path: '/admin/questions'
                , query: this.$route.query
            });
        },

        moveToQuestionModify() {
            this.$router.push({
                    name: "QuestionModifyView",
                    params: {
                        questionId: this.questionDetail.questionId
                    },
                    query: this.$route.query,
                }
            );
        }
    }
}
</script>

<style scoped>
.v-text-field >>> input {
    font-size: 0.875em;
}

.v-text-field >>> label {
    font-size: 0.875em;
}

.v-text-field >>> button {
    font-size: 0.875em;
}

.v-text-field--outlined >>> fieldset {
    border-color: rgba(209, 209, 209, 1);
}
</style>