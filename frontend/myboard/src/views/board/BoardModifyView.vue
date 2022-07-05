<template>
    <v-container>
        <CommonPageTitle>
            <h2 slot="title">
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

                            <!--          TODO: computed 로 분리-->
                            <template v-if="boardDetail.guestNickname !== null">
                                <v-row>
                                    <v-col>
                                        <v-text-field
                                            disabled
                                            v-model="boardDetail.guestNickname"
                                            required
                                            color="purple darken-2"
                                        ></v-text-field>
                                    </v-col>

                                    <v-col>
                                        <v-text-field
                                            type="password"
                                            v-model="form.guestPassword"
                                            :rules="rules.guestPassword"
                                            required
                                            label="비밀번호"
                                            hint="작성하셨을 때의 비밀번호를 입력해주세요."
                                            color="purple darken-2"
                                        ></v-text-field>
                                    </v-col>
                                </v-row>
                            </template>

                            <v-row>
                                <v-col
                                    cols="3"
                                >
                                    <v-select
                                        disabled
                                        readonly
                                        v-model="boardDetail.categoryId"
                                        :items="categories"
                                        item-text="categoryName"
                                        item-value="categoryId"
                                        label="카테고리"
                                        color="pink"
                                    ></v-select>
                                </v-col>

                                <v-col
                                    cols="9"
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

                            <v-row v-if="!(boardDetail.attachList.length === 0 && form.multipartFiles.length === 0)">
                                <v-card-text>
                                    <v-row>
                                        <v-col>
                                            <p v-for="attach in boardDetail.attachList">
                        <span v-on:click="$_boardService.downloadAttach(attach.attachId)" style="cursor:pointer;">

                          <v-icon>mdi-attachment</v-icon>
                          {{ attach.name }}.{{ attach.extension }} - {{ attach.size }} byte

                        </span>

                                                <v-btn x-small text color="red" v-on:click="removeOldAttach(attach)">x
                                                </v-btn>
                                            </p>

                                            <p v-for="multipartFile in form.multipartFiles">
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


                            <v-row v-if="boardDetail.attachList.length + form.multipartFiles.length < 3">
                                <v-col>
                                    <v-file-input
                                        v-on:change="addNewAttach" v-bind:key="fileInputKey"
                                        show-size
                                        outlined
                                        label="첨부파일"
                                    ></v-file-input>
                                </v-col>
                            </v-row>


                            <v-row>
                                <v-col
                                    cols="auto"
                                >
                                    <v-btn
                                        @click="moveToBoardDetail(boardDetail.boardId)"
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
                                        @click="modifyBoard"
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

export default {
    name: "BoardModifyView",
    components: {
        CommonPageTitle
    },
    data() {
        return {
            form: {
                guestPassword: "",
                multipartFiles: [],
                deleteAttaches: [],
            },

            rules: {
                //TODO: validator에 메서드 만들기
                guestPassword: [value => (value || '').length > 0 || "필수 항목입니다.",],
                title: [value => this.$_commonFormValidator.validateTitle(value),],
                content: [value => this.$_commonFormValidator.validateContent(value)],
            },

            boardDetail: {
                attachList: [],
            },

            fileInputKey: 0,

            categories: [
                {categoryName: 'Java', categoryId: 1},
                {categoryName: 'JavaScript', categoryId: 2},
                {categoryName: 'Database', categoryId: 3},
            ],
        }
    },
    async created() {
        let boardId = this.$route.params.boardId;

        const {data} = await this.$_boardService.fetchBoard(boardId);

        this.boardDetail = data.boardDetail;
        this.form.title = data.boardDetail.title;
        this.form.content = data.boardDetail.content;
    },
    methods: {
        validateForm() {
            return this.$refs.form.validate()
        },

        removeNewAttach(item) {
            this.form.multipartFiles.splice(this.form.multipartFiles.indexOf(item), 1);
        },

        addNewAttach(file) {
            this.form.multipartFiles.push(file);

            this.fileInputKey++;
        },

        removeOldAttach(attach) {
            this.form.deleteAttaches.push(attach.attachId);

            let index = this.boardDetail.attachList.indexOf(attach);

            this.boardDetail.attachList.splice(index, 1);
        },

        async modifyBoard() {
            if (this.validateForm()) {
                const validationResult = this.$_commonFormValidator.validateMultipartFiles(this.form.multipartFiles);

                if (validationResult !== true) {
                    alert(validationResult);
                    return;
                }

                let formData = this.prepareFormData();

                try {
                    await this.$_boardService.updateBoard(formData);

                    this.moveToBoardDetail(this.boardDetail.boardId);
                } catch (error) {

                    alert(error.response.data.errorMessage)
                }
            }
        },

        moveToBoardDetail(boardId) {
            this.$router.push({
                name: "BoardDetailView",
                params: {
                    boardId: boardId
                },
                query: this.$route.query
            }).catch(() => {
            });
        },

        prepareFormData() {
            let formData = new FormData();

            formData.append("guestPassword", this.form.guestPassword);
            formData.append("boardId", this.boardDetail.boardId);
            formData.append("categoryId", this.boardDetail.categoryId);
            formData.append("title", this.form.title);
            formData.append("content", this.form.content);
            formData.append("attachDeleteRequest", this.form.deleteAttaches);

            if (this.form.multipartFiles.length > 0) {
                for (const multipartFile of this.form.multipartFiles) {
                    formData.append("multipartFiles", multipartFile);
                }
            }

            return formData;
        },
    }
}
</script>

<style scoped>

</style>



