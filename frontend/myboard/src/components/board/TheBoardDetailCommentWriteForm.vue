<template>
    <v-form
        ref="form"
    >
        <v-card-text>
            <v-row dense>
                <v-col
                    cols="2"
                >
                    <template v-if="$store.getters.loggedIn">
                        <v-text-field
                            disabled dense style="height: 48px !important; " outlined
                            v-model="$store.state.nickname"
                        />
                    </template>

                    <template v-else>
                        <v-text-field
                            dense
                            style="height: 49px !important;"
                            label="닉네임"
                            outlined
                            v-model="commentSaveRequest.guestNickname"
                        />

                        <v-text-field
                            dense
                            style="height: 50px !important;"
                            type="password"
                            label="비밀번호"
                            outlined
                            v-model="commentSaveRequest.guestPassword"
                        />
                    </template>
                </v-col>

                <v-col>
                    <v-textarea
                        counter
                        dense
                        v-model="commentSaveRequest.content"
                        outlined
                        rows="3"
                        label="댓글을 입력해주세요."
                        v-on:keydown.enter.exact.prevent="clickSaveCommentBtn"
                        v-on:keydown.enter.shift.exact.prevent="commentSaveRequest.content += '\n'"
                    />
                </v-col>

                <v-col
                    cols="auto"
                >
                    <v-btn
                        outlined
                        height="90"
                        width="94"
                        color="primary"
                        v-on:click="clickSaveCommentBtn"
                    >
                        등록
                    </v-btn>
                </v-col>
            </v-row>
        </v-card-text>
    </v-form>
</template>

<script>
/**
 * 자유게시판 상세 조회 댓글 입력 폼
 */
export default {
    name: "TheBoardDetailCommentWriteForm",

    props: {
        commentSaveResponseStatus: {
            type: Number,
        },
    },

    data() {
        return {
            commentSaveRequest: {
                guestNickname: "",
                guestPassword: "",
                content: "",
            },
        }
    },

    watch: {
        /**
         * 댓글 등록 성공시 댓글 입력 폼 초기화
         */
        commentSaveResponseStatus() {
            if (this.commentSaveResponseStatus === 201) {

                this.initCommentWriteForm();

                this.$emit("initCommentSaveResponseStatus")
            }
        }
    },

    methods: {
        /**
         * 댓글 저장 버튼 클릭
         */
        clickSaveCommentBtn() {
            this.$emit("saveCommentBtnClick", this.commentSaveRequest);
        },

        /**
         * 댓글 입력 폼 초기화
         */
        initCommentWriteForm() {
            this.commentSaveRequest.content = "";
            this.commentSaveRequest.guestNickname = "";
            this.commentSaveRequest.guestPassword = "";
        },
    },
}
</script>
