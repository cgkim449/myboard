<template>
    <v-container>
        <PageTitle>
            <h2 slot="title">
                공지사항
            </h2>
        </PageTitle>

        <v-row>
            <v-col
                cols="12"
            >
                <v-card flat outlined class="px-8 py-8">

                    <v-form
                        ref="form"
                    >
                        <v-container fluid>
                            <v-row>
                                <v-col
                                    cols="12"
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

                            <v-row v-if="!(noticeDetail.attachList.length === 0 && form.multipartFiles.length === 0)">
                                <v-card-text>
                                    <v-row>
                                        <v-col>
                                            <p v-for="attach in noticeDetail.attachList">
                        <span v-on:click="$_NoticeService.downloadAttach(attach.attachId)" style="cursor:pointer;">

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


                            <v-row v-if="noticeDetail.attachList.length + form.multipartFiles.length < 3">
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
                                        @click="moveToNoticeDetail(noticeDetail.noticeId)"
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
                                        @click="modifyNotice"
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
import PageTitle from "@/components/common/PageTitle";

export default {
    name: "NoticeModifyView",
    components: {
        PageTitle
    },
    data() {
        return {
            form: {
                multipartFiles: [],
                deleteAttaches: [],
            },

            rules: {
                title: [value => this.$_CommonFormValidator.validateTitle(value),],
                content: [value => this.$_CommonFormValidator.validateContent(value)],
            },

            noticeDetail: {
                attachList: [],
            },

            fileInputKey: 0,
        }
    },
    async created() {
        let noticeId = this.$route.params.noticeId;

        const {data} = await this.$_NoticeService.fetchNoticeDetail(noticeId);

        this.noticeDetail = data.noticeDetail;
        this.form.title = data.noticeDetail.title;
        this.form.content = data.noticeDetail.content;
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

            let index = this.noticeDetail.attachList.indexOf(attach);

            this.noticeDetail.attachList.splice(index, 1);
        },

        async modifyNotice() {
            if (this.validateForm()) {
                const validationResult = this.$_CommonFormValidator.validateMultipartFiles(this.form.multipartFiles);

                if (validationResult !== true) {
                    alert(validationResult);
                    return;
                }

                let formData = this.prepareFormData();

                try {
                    await this.$_NoticeService.updateNotice(formData);

                    this.moveToNoticeDetail(this.noticeDetail.noticeId);
                } catch (error) {

                    alert(error.response.data.errorMessage)
                }
            }
        },

        moveToNoticeDetail(noticeId) {
            this.$router.push({
                name: "NoticeDetailView",
                params: {
                    noticeId: noticeId
                },
                query: this.$route.query
            }).catch(() => {
            });
        },

        prepareFormData() {
            let formData = new FormData();

            formData.append("noticeId", this.noticeDetail.noticeId);
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



